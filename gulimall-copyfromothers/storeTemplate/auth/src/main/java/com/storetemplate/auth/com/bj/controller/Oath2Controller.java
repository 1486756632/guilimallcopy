package com.storetemplate.auth.com.bj.controller;

import com.alibaba.fastjson.JSON;


import com.alibaba.fastjson.JSONObject;
import com.bj.constant.AuthServerConstant;
import com.bj.exceptionCode.BASICTRANCODE;
import com.bj.utils.HttpUtils;
import com.bj.utils.R;
import com.bj.vo.MemberRsepVo;
import com.storetemplate.auth.com.bj.feign.MemberFeignService;
import com.storetemplate.auth.com.bj.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: Oath2Controller</p>
 * Description：
 * date：2020/6/26 14:14
 */
@Slf4j
@Controller
@RequestMapping("/oauth2.0")
public class Oath2Controller {

	@Autowired
	private MemberFeignService memberFeignService;

	@GetMapping("/logout")
	public String login(HttpSession session){
		if(session.getAttribute(AuthServerConstant.LOGIN_USER) != null){
			log.info("\n[" + ((MemberRsepVo)session.getAttribute(AuthServerConstant.LOGIN_USER)).getUsername() + "] 已下线");
		}
		session.invalidate();
		return "redirect:http://auth.glmall.com/login.html";
	}

	/**
	 * 登录成功回调
	 * {
	 *     "access_token": "2.00b5w4HGbwxc6B0e3d62c666DlN1DD",
	 *     "remind_in": "157679999",
	 *     "expires_in": 157679999,
	 *     "uid": "5605937365",
	 *     "isRealName": "true"
	 * }
	 * 	汀西氟的我是你	---		2.00b5w4HGbwxc6B0e3d62c666DlN1DD
	 */
	@GetMapping("/weibo/success")
	public String weiBo(@RequestParam("code") String code, HttpSession session) throws Exception {

		// 根据code换取 Access Token
		Map<String,String> map = new HashMap<>();
		map.put("client_id", "2678252012");
		map.put("client_secret", "09accc256bd829eb8fe9b09d92f80aea");
		map.put("grant_type", "authorization_code");
		map.put("redirect_uri", "http://auth.storeTemplate.com/oauth2.0/weibo/success");
		map.put("code", code);
		Map<String, String> headers = new HashMap<>();
		HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", headers, null, map);
		if(response.getStatusLine().getStatusCode() == 200){
			// 获取到了 Access Token
			String json = EntityUtils.toString(response.getEntity());
			SocialUser socialUser = JSON.parseObject(json, SocialUser.class);

			// 相当于我们知道了当前是那个用户
			// 1.如果用户是第一次进来 自动注册进来(为当前社交用户生成一个会员信息 以后这个账户就会关联这个账号)
			R login = memberFeignService.login(socialUser);
			if((Integer) login.get("code") == 0){
				Object data = login.get("data");
				String s = JSON.toJSONString(data);
				MemberRsepVo rsepVo = JSONObject.parseObject(s, MemberRsepVo.class);
				log.info("\n欢迎 [" + rsepVo.getUsername() + "] 使用社交账号登录");
				// 第一次使用session 命令浏览器保存这个用户信息 JESSIONSEID 每次只要访问这个网站就会带上这个cookie
				// 在发卡的时候扩大session作用域 (指定域名为父域名)
				// TODO 1.默认发的当前域的session (需要解决子域session共享问题)
				// TODO 2.使用JSON的方式序列化到redis
			    new Cookie("JSESSIONID","").setDomain("storeTemplate.com");
				session.setAttribute(AuthServerConstant.LOGIN_USER, rsepVo);
				// 登录成功 跳回首页
				return "redirect:http://storeTemplate.com";
			}else{
				return "redirect:http://auth.storeTemplate.com/index.html";
			}
		}else{
			return "redirect:http://auth.storeTemplate.com/index.html";
		}
	}
}
