package com.pis.gmall.manage.mapper;

import com.pis.gmall.bean.PmsSkuSaleAttrValue;
import tk.mybatis.mapper.common.Mapper;

public interface PmsSkuSaleAttrValueMapper extends Mapper<PmsSkuSaleAttrValue> {
    int insertSelective(PmsSkuSaleAttrValue pmsSkuSaleAttrValue);
}
