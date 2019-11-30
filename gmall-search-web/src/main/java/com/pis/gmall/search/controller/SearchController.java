package com.pis.gmall.search.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pis.gmall.bean.*;
import com.pis.gmall.service.AttrService;
import com.pis.gmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class SearchController {

    @Reference
    SearchService searchService;
    @Reference
    AttrService attrService;

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap) {//catalog3Id、keyword

        List<PmsSearchSkuInfo> searchList = searchService.getSearchList(pmsSearchParam);
        modelMap.put("skuLsInfoList", searchList);

        //抽取检索结果所包含的平台属性集合
        Set<String> valueIdSet = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : searchList) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                String valueId = pmsSkuAttrValue.getValueId();
                valueIdSet.add(valueId);
            }
        }
        //根据valueId将属性列表查询出来
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.getAttrInfoByValueId(valueIdSet);
        modelMap.put("attrList", pmsBaseAttrInfos);

        //对平台属性集合进一步处理，去掉当前条中valueId所在的属性组
        String[] valueIds = pmsSearchParam.getValueIds();

        if (valueIds != null) {
            //面包屑
            List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
            for (String valueId : valueIds) {
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                pmsSearchCrumb.setUrlParam(getUrlParamForCrumb(pmsSearchParam, valueId));
                pmsSearchCrumb.setValueId(valueId);
                pmsSearchCrumbs.add(pmsSearchCrumb);
                while (iterator.hasNext()) {
                    PmsBaseAttrInfo next = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = next.getAttrValueList();
                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        String id = pmsBaseAttrValue.getId();
                        pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                        if (id.equals(valueId)) {
                            iterator.remove();
                        }
                    }
                }
            }
            modelMap.put("attrValueSelectedList", pmsSearchCrumbs);
        }


        String urlParam = getUrlParam(pmsSearchParam);
        modelMap.put("urlParam", urlParam);
        String keyword = pmsSearchParam.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {

        }
        modelMap.put("keword", keyword);
        return "list";
    }

    private String getUrlParamForCrumb(PmsSearchParam pmsSearchParam, String valueId) {
        String urlParam = "";

        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] pmsSkuAttrValues = pmsSearchParam.getValueIds();

        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }
        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }
        if (pmsSkuAttrValues != null) {
            for (String pmsSkuAttrValue : pmsSkuAttrValues) {
                if (!pmsSkuAttrValue.equals(valueId)) {
                    urlParam = urlParam + "&valuedId=" + pmsSkuAttrValue;
                }
            }
        }
        return null;
    }

    private String getUrlParam(PmsSearchParam pmsSearchParam) {
        String urlParam = "";

        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] pmsSkuAttrValues = pmsSearchParam.getValueIds();

        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }
        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }
        if (pmsSkuAttrValues != null) {
            for (String pmsSkuAttrValue : pmsSkuAttrValues) {
                String valueId = pmsSkuAttrValue;
                urlParam = urlParam + "&valuedId=" + valueId;
            }
        }
        return null;
    }


    @RequestMapping("index")
    public String index() {
        return "index";
    }
}
