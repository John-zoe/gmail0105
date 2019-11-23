package com.pis.gmall.manage.service.serviceImpl;


import com.alibaba.dubbo.config.annotation.Service;
import com.pis.gmall.bean.PmsBaseAttrInfo;
import com.pis.gmall.bean.PmsBaseAttrValue;
import com.pis.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.pis.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.pis.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;
    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Override
    public List<PmsBaseAttrInfo> getAttrInfo(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        return pmsBaseAttrInfos;
    }

    @Override
    //增加和修改
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo) {
        //根据id判断增加还是修改
        String id = pmsBaseAttrInfo.getId();
        if (StringUtils.isBlank(id)) {
            //id为空则为增加
            //添加属性  id/attr_name
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
            //在根据属性值外键(属性的id)增加属性值  添加属性值 id/attr_id/vaule_name
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue value : attrValueList) {
                value.setId(id);
                pmsBaseAttrValueMapper.insertSelective(value);
            }

        } else {
            //id非空为修改
            //修改属性
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("id", id);
            pmsBaseAttrInfoMapper.updateByExample(pmsBaseAttrInfo, example);
            //先删除
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(id);
            pmsBaseAttrValueMapper.delete(pmsBaseAttrValue);
            //再插入新数据
            List<PmsBaseAttrValue> values = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue value : values) {
                pmsBaseAttrValueMapper.insertSelective(value);
            }

        }
        return "save success!!";
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);

        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
        return pmsBaseAttrValues;
    }


}
