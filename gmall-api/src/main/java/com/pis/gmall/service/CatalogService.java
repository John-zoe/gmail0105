package com.pis.gmall.service;

import com.pis.gmall.bean.*;

import java.io.Serializable;
import java.util.List;

public interface CatalogService extends Serializable {
    List<PmsBaseCatalog1> getCatalog1();

    List<PmsBaseCatalog2> getCatalog2(String catalog1Id);

    List<PmsBaseCatalog3> getCatalog3(String catalog2Id);
}
