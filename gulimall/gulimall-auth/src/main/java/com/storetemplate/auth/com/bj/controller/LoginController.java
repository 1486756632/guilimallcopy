package com.storetemplate.auth.com.bj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bj.constant.AuthServerConstant;
import com.bj.exceptionCode.BASICTRANCODE;
import com.bj.utils.R;
import com.bj.vo.MemberRsepVo;
import com.storetemplate.auth.com.bj.feign.MemberFeignService;
import com.storetemplate.auth.com.bj.feign.OutgatewayFeignClint;
import com.storetemplate.auth.com.bj.vo.UserLoginVo;
import com.storetemplate.auth.com.bj.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author 13011
 * @Date 2021/1/1 19:05
 * @Version 1.0
 **/
@Controller
public class LoginController {
    @Autowired
   private OutgatewayFeignClint outgatewayFeignClint;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private MemberFeignService memberFeignService;

    @ResponseBody
    @RequestMapping("sendSms")
    public R sendSms(@RequestParam("phone") String phone){
        //验证码防刷，
        //防止发送过于频繁，发送期为一分钟
        String s = stringRedisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(s)){
            String s1 = s.split("_")[1];
            long l = Long.parseLong(s1);
            if(System.currentTimeMillis()-l<60000){
                return R.error(BASICTRANCODE.SMSEXCEPTION.getCode(),BASICTRANCODE.SMSEXCEPTION.getMsg());
            }
        }
        //生成验证码
        String s1 = UUID.randomUUID().toString().substring(0, 5);
        String code = "123456"+"_"+System.currentTimeMillis();
        //验证码存入redis，有效期十分钟 由于免费模板只会发送123456，此处存固定值123456
        // TODO: 2021/1/2 后续将123456，改成自己真实值
        stringRedisTemplate.opsForValue().set(phone,code,10, TimeUnit.MINUTES);
        R r = outgatewayFeignClint.sendSms(phone, s1);
        return r;
    }
    /**
     * TODO 重定向携带数据,利用session原理 将数据放在sessoin中 取一次之后删掉
     *
     * TODO 1. 分布式下的session问题
     * 校验
     * RedirectAttributes redirectAttributes ： 模拟重定向带上数据
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterVo vo, BindingResult result, RedirectAttributes redirectAttributes){

        if(result.hasErrors()){

            // 将错误属性与错误信息一一封装
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, fieldError -> fieldError.getDefaultMessage()));
            // addFlashAttribute 这个数据只取一次
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.storetemplate.com//reg.html";
        }
        // 开始注册 调用远程服务
        // 1.校验验证码
        String code = vo.getCode();

        String redis_code = stringRedisTemplate.opsForValue().get(vo.getPhone());
        if(!StringUtils.isEmpty(redis_code)){
            // 验证码通过
            if(code.equals(redis_code.split("_")[0])){
                // 删除验证码
                stringRedisTemplate.delete(vo.getPhone());
                // 调用远程服务进行注册
                R r = memberFeignService.register(vo);
                if((Integer)r.get("code") == 0){
                    // 成功
                    return "redirect:http://auth.storetemplate.com/index.html";
                }else{
                    Map<String, Object> errors = new HashMap<>();
                    errors.put("msg",r.get("msg"));
                    redirectAttributes.addFlashAttribute("errors",errors);
                    return "redirect:http://auth.storetemplate.com/reg.html";
                }
            }else{
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码错误");
                // addFlashAttribute 这个数据只取一次
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.storetemplate.com/reg.html";
            }
        }else{
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            // addFlashAttribute 这个数据只取一次
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.storetemplate.com/reg.html";
        }
    }

    @PostMapping("/login")
    public String login(UserLoginVo userLoginVo, RedirectAttributes redirectAttributes, HttpSession session){
        // 远程登录
        R r = memberFeignService.login(userLoginVo);
        if((Integer)r.get("code") == 0){
            // 登录成功
            Object data = r.get("data");
            String s = JSON.toJSONString(data);
            MemberRsepVo rsepVo = JSONObject.parseObject(s, MemberRsepVo.class);
            session.setAttribute(AuthServerConstant.LOGIN_USER, rsepVo);
            return "redirect:http://storeTemplate.com";
        }else {
            HashMap<String, Object> error = new HashMap<>();
            // 获取错误信息
            error.put("msg", r.get("msg"));
            redirectAttributes.addFlashAttribute("errors", error);
            return "redirect:http://auth.storeTemplate.com/index.html";
        }
    }

    @GetMapping({"/","/index","/index.html"})
    public String loginPage(HttpSession session){
        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
        if(attribute == null){
            return "index";
        }
        return "redirect:http://storeTemplate.com";
    }
}
