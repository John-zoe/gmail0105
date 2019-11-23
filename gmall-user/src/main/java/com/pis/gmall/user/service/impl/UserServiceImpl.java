package com.pis.gmall.user.service.impl;

import com.pis.gmall.bean.UmsMember;
import com.pis.gmall.service.UserService;



import com.pis.gmall.bean.UmsMemberReceiveAddress;
import com.pis.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.pis.gmall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UmsMemberReceiveAddressMapper receiveAddressMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public List<UmsMember> getAllUser() {
        List<UmsMember> umsMemberList = userMapper.selectAllUser();
        return umsMemberList;
    }

    @Override
    public List<UmsMemberReceiveAddress> getRecieveAddressByUmsId(String memberId) {

        //封装sql参数在address
        UmsMemberReceiveAddress address = new UmsMemberReceiveAddress();
        address.setId(memberId);

        //传入address
        List<UmsMemberReceiveAddress> addresses = receiveAddressMapper.select(address);
        return addresses;
    }
}
