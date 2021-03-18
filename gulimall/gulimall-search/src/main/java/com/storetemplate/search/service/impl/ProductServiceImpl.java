package com.storetemplate.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.common.to.es.SkuEsModel;
import com.storetemplate.search.config.EsConfig;
import com.storetemplate.search.constant.EsConstant;
import com.storetemplate.search.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName ProductServiceImpl
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/4 11:29
 * @Version 1.0
 **/
@Slf4j
@Service("searchService")
public class ProductServiceImpl implements ProductService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public Boolean productUp(List<SkuEsModel> skuEsModels) {
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String s = JSON.toJSONString(skuEsModel);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        try {
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, EsConfig.COMMOM_OPTIONS);
            log.info("es返回值："+bulk.getItems().toString());
            return true;
        } catch (IOException e) {
            log.error("上架失败");
            e.printStackTrace();
            return false;
        }

    }
}
