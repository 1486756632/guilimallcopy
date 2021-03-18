package com.storetemplate.search;

import com.alibaba.fastjson.JSON;
import com.atguigu.common.to.es.SkuEsModel;
import com.storetemplate.search.config.EsConfig;
import com.storetemplate.search.constant.EsConstant;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchApplicationTests {
    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Test
    public void contextLoads() throws IOException {
        SkuEsModel skuEsModel=new SkuEsModel();
        skuEsModel.setSkuId(1234L);
        skuEsModel.setBrandName("testEs");
        skuEsModel.setHotScore(1000L);
        BulkRequest bulkRequest = new BulkRequest();
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String s = JSON.toJSONString(skuEsModel);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, EsConfig.COMMOM_OPTIONS);
        System.out.println(bulk);
    }
    @Test
    public void test2(){
        long total=2;
        int totalPages = (int)(total / 5 + (total%5==0?0:1));
        System.out.println(total/5);
        System.out.println(totalPages);
    }

}
