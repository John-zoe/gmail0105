package com.pis.gmall.manage.mapper;

import com.pis.gmall.bean.PmsSkuImage;
import tk.mybatis.mapper.common.Mapper;

public interface PmsSkuImageMapper extends Mapper<PmsSkuImage> {
    int insertSelective(PmsSkuImage pmsSkuImage);
}
