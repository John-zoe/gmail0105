package com.pis.gmall.service;

import com.pis.gmall.bean.PmsBaseAttrInfo;
import com.pis.gmall.bean.PmsBaseAttrValue;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface AttrService extends Serializable{
     List<PmsBaseAttrInfo> getAttrInfo(String catalog3Id);
    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);
     List<PmsBaseAttrValue> getAttrValueList(String attrId);
    List<PmsBaseAttrInfo> getAttrInfoByValueId(Set<String> valueIdSet);
}
