package com.storetemplate.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName EsConfig
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/4 18:28
 * @Version 1.0
 **/
@Configuration
public class EsConfig {
    public  static final RequestOptions COMMOM_OPTIONS;
    static {
        RequestOptions.Builder builder=RequestOptions.DEFAULT.toBuilder();
        COMMOM_OPTIONS = builder.build();
    }
    public RestHighLevelClient restHighLevelClient(){
        RestClientBuilder builder=null;
        builder= RestClient.builder(new HttpHost("localhost",9200,"http"));
        RestHighLevelClient client=new RestHighLevelClient(builder);
        return client;
    }
}
