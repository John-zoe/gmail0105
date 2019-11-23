package com.pis.gmall.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pis.gmall.bean.PmsBaseAttrInfo;
import com.pis.gmall.bean.PmsBaseAttrValue;
import com.pis.gmall.service.AttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AttrController {

    @Reference
    AttrService attrService;


    @RequestMapping("getAttrInfo")
    @ResponseBody
    public List<PmsBaseAttrInfo> getAttrInfo(String catalog3Id) {
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.getAttrInfo(catalog3Id);
        return pmsBaseAttrInfos;
    }


    @RequestMapping("getAttrValue")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValue(String attrId) {

        List<PmsBaseAttrValue> pmsBaseAttrValues = attrService.getAttrValueList(attrId);
        return pmsBaseAttrValues;
    }

    public void saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo) {
        attrService.saveAttrInfo(pmsBaseAttrInfo);
    }

    @RequestMapping("getAttrValuelist")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValuelist(String attrId) {

        List<PmsBaseAttrValue> pmsBaseAttrValues = attrService.getAttrValueList(attrId);
        return pmsBaseAttrValues;
    }

}
