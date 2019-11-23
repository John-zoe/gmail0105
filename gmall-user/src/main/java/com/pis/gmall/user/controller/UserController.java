package com.pis.gmall.user.controller;

import com.pis.gmall.service.UserService;
import com.pis.gmall.bean.UmsMember;
import com.pis.gmall.bean.UmsMemberReceiveAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("getRecieveAddressesByUmsId")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getRecieveAddressesByUmsId(String memberId){
        List<UmsMemberReceiveAddress> Addresses = userService.getRecieveAddressByUmsId(memberId);
        return Addresses;
    }

    @RequestMapping("getAllUsers")
    @ResponseBody
    public List<UmsMember> getAllUsers(){
        List<UmsMember> allUser = userService.getAllUser();
        return allUser;
    }


    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return "Hello user";
    }

}
