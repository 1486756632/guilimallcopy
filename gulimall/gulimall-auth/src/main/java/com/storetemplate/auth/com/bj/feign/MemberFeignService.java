package com.storetemplate.auth.com.bj.feign;


import com.atguigu.common.utils.R;
import com.storetemplate.auth.com.bj.vo.SocialUser;
import com.storetemplate.auth.com.bj.vo.UserLoginVo;
import com.storetemplate.auth.com.bj.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>Title: MemberFeignService</p>
 * Description：
 * date：2020/6/25 20:31
 */
@FeignClient("storeTemplate.vip")
public interface MemberFeignService {

	@PostMapping("/member/member/register")
	R register(@RequestBody UserRegisterVo userRegisterVo);

	@PostMapping("/member/member/login")
	R login(@RequestBody UserLoginVo vo);

	@PostMapping("/member/member/oauth2/login")
	R login(@RequestBody SocialUser socialUser);
}
