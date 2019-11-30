package com.pis.gmall.service;

import com.pis.gmall.bean.PmsSearchParam;
import com.pis.gmall.bean.PmsSearchSkuInfo;

import java.io.Serializable;
import java.util.List;

public interface SearchService extends Serializable{
    List<PmsSearchSkuInfo> getSearchList(PmsSearchParam pmsSearchParam);
}
