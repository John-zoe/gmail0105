package com.pis.gmall.manage.mapper;

import com.pis.gmall.bean.PmsSkuInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo>{
    List<PmsSkuInfo> selectSaleAttrValueListByspu(String productId);

    List<PmsSkuInfo> selectSkuSaleAttrValueListBySpu(String productId);
}
