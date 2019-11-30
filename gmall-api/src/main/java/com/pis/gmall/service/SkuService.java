package com.pis.gmall.service;

import com.pis.gmall.bean.PmsSkuInfo;

import java.io.Serializable;
import java.util.List;

public interface SkuService extends Serializable {
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId);

    PmsSkuInfo getSkuByIdFromDb(String skuId);

    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId);

    List<PmsSkuInfo> getAllSku();


}