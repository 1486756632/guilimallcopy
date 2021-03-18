package com.bj.outgateway;

import com.aliyun.oss.OSSClient;
import com.bj.outgateway.sms.component.SmsComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class OutgatewayApplicationTests {
    @Autowired
    private OSSClient ossClient;
    @Resource
    private SmsComponent smsComponent;

    @Test
    public void testUploadByAlicloudOss() throws FileNotFoundException {
        String bucketName = "store-template";
        String localFile = "D:\\BaiduNetdiskDownload\\谷粒商城基础篇资料源码\\docs\\pics\\huawei.png";
        String fileKeyName = "aini.png";
        InputStream inputStream = new FileInputStream(localFile);
        ossClient.putObject(bucketName, fileKeyName, inputStream);
        ossClient.shutdown();
    }

    @Test
    public void testSendSms() {
        smsComponent.sendSmsCode("1","123456");
    }
}
