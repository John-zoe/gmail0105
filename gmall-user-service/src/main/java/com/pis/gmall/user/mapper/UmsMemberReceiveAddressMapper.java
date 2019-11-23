package com.pis.gmall.user.mapper;

import com.pis.gmall.bean.UmsMemberReceiveAddress;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UmsMemberReceiveAddressMapper extends Mapper<UmsMemberReceiveAddress>{

    List<UmsMemberReceiveAddress> selectUmsReceiveAddress() ;
}
