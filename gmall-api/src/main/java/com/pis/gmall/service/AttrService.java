package com.pis.gmall.service;

import com.pis.gmall.bean.PmsBaseAttrInfo;
import com.pis.gmall.bean.PmsBaseAttrValue;

import java.util.List;

public interface AttrService {
     List<PmsBaseAttrInfo> getAttrInfo(String catalog3Id);
    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);
     List<PmsBaseAttrValue> getAttrValueList(String attrId);

}
