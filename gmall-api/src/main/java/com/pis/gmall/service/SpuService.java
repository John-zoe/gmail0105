package com.pis.gmall.service;

import com.pis.gmall.bean.PmsBaseAttrInfo;
import com.pis.gmall.bean.PmsBaseSaleAttr;
import com.pis.gmall.bean.PmsProductInfo;
import com.pis.gmall.bean.PmsProductSaleAttr;

import java.io.Serializable;
import java.util.List;

public interface SpuService extends Serializable {
    List<PmsProductInfo> spuList(String catalog3Id);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String spuId, String skuId);
}
