package com.pis.gmall.service;

import com.pis.gmall.bean.UmsMember;
import com.pis.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getRecieveAddressByUmsId(String memberId);
}
