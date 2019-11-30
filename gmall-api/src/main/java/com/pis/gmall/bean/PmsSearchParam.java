package com.pis.gmall.bean;

import java.util.List;

public class PmsSearchParam {
    private String catalog3Id;
    private String keyword;
    private String[] valueIds;

    public String[] getValueIds() {
        return valueIds;
    }

    public void setValueIds(String[] valueIds) {
        this.valueIds = valueIds;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
