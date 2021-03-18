package com.bj.outgateway.sms.controller;

import com.bj.outgateway.sms.component.SmsComponent;
import com.bj.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SendSmsController
 * @Description TODO
 * @Author 13011
 * @Date 2021/1/1 18:27
 * @Version 1.0
 **/
@RestController
@RequestMapping("/sms")
public class SendSmsController {
    @Autowired
    private SmsComponent smsComponent;
    @RequestMapping("/sendSms")
    public R sendSms(@RequestParam("phone") String phone,@RequestParam("code") String code){
        smsComponent.sendSmsCode(phone,code);
        return R.ok();
    }

}
