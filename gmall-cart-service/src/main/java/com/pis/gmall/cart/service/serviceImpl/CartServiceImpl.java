package com.pis.gmall.cart.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pis.gmall.bean.OmsCartItem;
import com.pis.gmall.cart.service.mapper.OmsCartItemMapper;
import com.pis.gmall.service.CartService;
import com.pis.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    OmsCartItemMapper omsCartItemMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public OmsCartItem ifUserCartExistThisCartItem(String memberId, String skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        OmsCartItem omsCartItem1 = omsCartItemMapper.selectOne(omsCartItem);
        return omsCartItem1;
    }

    @Override
    public void addToCart(OmsCartItem omsCartItem) {
        if(StringUtils.isNotBlank(omsCartItem.getMemberId())) {
            //避免空值
            omsCartItemMapper.insert(null);
        }
    }

    @Override
    public void updateCart(OmsCartItem omsCartItemFromDb) {
        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("memeberId",omsCartItemFromDb.getMemberId());
        omsCartItemMapper.updateByExample(omsCartItemFromDb,example);
    }

    @Override
    public void synCartCache(String memberId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> cartItemList = omsCartItemMapper.select(omsCartItem);

        Jedis jedis = redisUtil.getJedis();
        Map<String,String> map = new HashMap<>();
        for (OmsCartItem cartItem : cartItemList) {
            cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
            map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
        }

        jedis.del("user:"+memberId+":cart");
        jedis.hmset("user:"+memberId+":cart",map);

        jedis.close();
    }

    @Override
    public List<OmsCartItem> getCartList(String memberId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);

        List<OmsCartItem> cartItems = omsCartItemMapper.select(omsCartItem);
        return cartItems;
    }

    @Override
    public void checkCart(OmsCartItem omsCartItem) {
        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("memberId",omsCartItem.getMemberId()).andEqualTo("productSkuId",omsCartItem.getProductId());

        omsCartItemMapper.updateByExampleSelective(omsCartItem,example);
        synCartCache(omsCartItem.getMemberId());
    }
}
