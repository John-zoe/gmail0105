package com.pis.gmall.item.controller;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class test {

    @RequestMapping("index")
    public String index() {
        return "index";
    }

}
