package com.storetemplate.search.service;


import com.atguigu.common.to.es.SkuEsModel;

import java.util.List;

public interface ProductService {
    Boolean productUp(List<SkuEsModel> skuEsModels) ;
}
