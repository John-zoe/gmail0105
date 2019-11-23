package com.pis.gmall.service;

import com.pis.gmall.bean.PmsBaseAttrInfo;
import com.pis.gmall.bean.PmsBaseSaleAttr;
import com.pis.gmall.bean.PmsProductInfo;

import java.util.List;

public interface SpuService {
    List<PmsProductInfo> spuList(String catalog3Id);

    List<PmsBaseSaleAttr> baseSaleAttrList();
}
