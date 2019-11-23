package com.pis.gmall.manage.service.serviceImpl;

import com.pis.gmall.bean.PmsBaseAttrInfo;
import com.pis.gmall.bean.PmsBaseSaleAttr;
import com.pis.gmall.bean.PmsProductInfo;
import com.pis.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.pis.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.pis.gmall.manage.mapper.PmsProductInfoMapper;
import com.pis.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SpuServiceImpl implements SpuService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.select(pmsProductInfo);
        return pmsProductInfos;
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }
}
