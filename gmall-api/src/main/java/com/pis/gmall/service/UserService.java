package com.pis.gmall.service;

import com.pis.gmall.bean.UmsMember;
import com.pis.gmall.bean.UmsMemberReceiveAddress;

import java.io.Serializable;
import java.util.List;

public interface UserService extends Serializable {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getRecieveAddressByUmsId(String memberId);
}
