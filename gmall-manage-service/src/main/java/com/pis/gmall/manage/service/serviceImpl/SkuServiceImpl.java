package com.pis.gmall.manage.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pis.gmall.bean.PmsSkuAttrValue;
import com.pis.gmall.bean.PmsSkuImage;
import com.pis.gmall.bean.PmsSkuInfo;
import com.pis.gmall.bean.PmsSkuSaleAttrValue;
import com.pis.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.pis.gmall.manage.mapper.PmsSkuImageMapper;
import com.pis.gmall.manage.mapper.PmsSkuInfoMapper;
import com.pis.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.pis.gmall.service.SkuService;
//import com.pis.gmall.util.RedisUtil;
import com.pis.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    RedisUtil redisUtil;

    //@Autowired
    //RedisUtil redisUtil;


    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {

        // 插入skuInfo
        int i = pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String skuId = pmsSkuInfo.getId();

        // 插入平台属性关联
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        // 插入销售属性关联
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

        // 插入图片信息
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }

    }



    @Override
    public PmsSkuInfo getSkuById(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        //链接缓存
        Jedis jedis = redisUtil.getJedis();

        //查询缓存
        String skuKey = "sku:" + skuId + ":info";
        String skuJson = jedis.get(skuKey);

        if(StringUtils.isNotBlank(skuJson)) { //if(skuJson != null && !skuJson.equals(""))
            JSON.parseObject(skuJson, PmsSkuInfo.class);
        }else {//如果缓存中没有，查询mysql
            String token = UUID.randomUUID().toString();
            String ok = jedis.set("sku:" + skuId + ":lock", token, "nx", "px", 3*1000);
            if(StringUtils.isBlank(ok)&&ok.equals("OK")) {
                pmsSkuInfo =  getSkuByIdFromDb(skuId);

                if(pmsSkuInfo!=null){
                    //mysql查询结果存入redis
                    jedis.set(skuKey,JSON.toJSONString(pmsSkuInfo));
                }else{
                    //数据库不存在sku
                    //为了防止缓存击穿，将null或者空字符串设置给redis
                    jedis.setex(skuKey,60*3,JSON.toJSONString(""));
                }
                //释放锁
                String lockToken = jedis.get("sku:" + skuId + ":lock");
                if(StringUtils.isBlank(lockToken)&&lockToken.equals(token)){
                    //利用lua脚本判断并同时删除
                    jedis.del("sku:" + skuId + ":lock");
                }

            }else{
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getSkuById(skuId);//这里的return不能丢，否则将创建子线程（孤儿线程）
            }

        }
        jedis.close();
        return pmsSkuInfo;
    }


    public PmsSkuInfo getSkuByIdFromDb(String skuId){
        // sku商品对象
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        // sku的图片集合
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        skuInfo.setSkuImageList(pmsSkuImages);
        return skuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {

        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);

        return pmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getAllSku() {

        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();
        return pmsSkuInfos;
    }

}
