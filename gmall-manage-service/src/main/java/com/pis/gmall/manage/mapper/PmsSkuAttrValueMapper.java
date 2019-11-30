package com.pis.gmall.manage.mapper;

import com.pis.gmall.bean.PmsSkuAttrValue;
import tk.mybatis.mapper.common.Mapper;

public interface PmsSkuAttrValueMapper extends Mapper<PmsSkuAttrValue> {
    int insertSelective(PmsSkuAttrValue pmsSkuAttrValue);
}
