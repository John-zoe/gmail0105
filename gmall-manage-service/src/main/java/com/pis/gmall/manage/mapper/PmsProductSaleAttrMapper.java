package com.pis.gmall.manage.mapper;

import com.pis.gmall.bean.PmsProductSaleAttr;
        import org.apache.ibatis.annotations.Param;
        import tk.mybatis.mapper.common.Mapper;

        import java.util.List;

public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr>{
    //不加Param() mabatis就会报错
    List<PmsProductSaleAttr> selectSpuSaleAttrListCheckBySku(@Param("spuId") String spuId, @Param("skuId") String skuId);
}
