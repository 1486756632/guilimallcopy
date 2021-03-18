package com.storetemplate.search.service;

import com.bj.dto.es.SkuEsModel;

import java.io.IOException;
import java.util.List;


public interface ProductService {
    Boolean productUp(List<SkuEsModel> skuEsModels) ;
}
