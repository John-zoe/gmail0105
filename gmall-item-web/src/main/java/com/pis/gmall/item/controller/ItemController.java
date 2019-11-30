package com.pis.gmall.item.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pis.gmall.bean.PmsProductSaleAttr;
import com.pis.gmall.bean.PmsSkuInfo;
import com.pis.gmall.bean.PmsSkuSaleAttrValue;
import com.pis.gmall.service.SkuService;
import com.pis.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Reference
    SkuService skuService;
    @Reference
    SpuService spuService;

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap modelMap){
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        //sku对象,放到request域中
        modelMap.put("skuInfo",pmsSkuInfo);

        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getSpuId(), skuId);
        modelMap.put("spuSaleAttrListCheckBySku", pmsProductSaleAttrs);


        //查询当前sku的spu的其他sku的hash列表集合
        List<PmsSkuInfo> skuInfos = skuService.getSkuSaleAttrValueListByspu(pmsSkuInfo.getProductId());

        Map<Object, Object> skuSaleAttrmap = new HashMap<>();

        for (PmsSkuInfo skuInfo : skuInfos) {
            String k = "";
            String v = skuInfo.getId();

            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();

            for (PmsSkuSaleAttrValue value : skuSaleAttrValueList) {
                k = value.getSaleAttrValueId() + "|";  //"239|245"
            }

            skuSaleAttrmap.put(k,v);
        }

        //讲sku的销售属性hash表放到页面上
        String jsonString = JSON.toJSONString(skuSaleAttrmap);
        modelMap.put("", jsonString);

        return "item";
    }

}
















