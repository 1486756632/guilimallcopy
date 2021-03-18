package com.bj.vip.controller;

import java.util.Arrays;
import java.util.Map;


import com.bj.exceptionCode.BASICTRANCODE;
import com.bj.vip.exception.PhoneExistException;
import com.bj.vip.exception.UserNameExistException;
import com.bj.vip.vo.MemberLoginVo;
import com.bj.vip.vo.SocialUser;
import com.bj.vip.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bj.vip.entity.MemberEntity;
import com.bj.vip.service.MemberService;
import com.bj.utils.PageUtils;
import com.bj.utils.R;



/**
 * 会员
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 22:24:55
 */
@RestController
@RequestMapping("member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("vip:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("vip:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("vip:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("vip:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("vip:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @PostMapping("/register")
    public R register(@RequestBody UserRegisterVo userRegisterVo){

        try {
            memberService.register(userRegisterVo);
        } catch (PhoneExistException e) {
                return R.error(BASICTRANCODE.PHONE_EXIST_EXCEPTION.getCode(), BASICTRANCODE.PHONE_EXIST_EXCEPTION.getMsg());
        } catch (UserNameExistException e) {
            return R.error(BASICTRANCODE.USER_EXIST_EXCEPTION.getCode(), BASICTRANCODE.USER_EXIST_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    @PostMapping("/oauth2/login")
    public R login(@RequestBody SocialUser socialUser){

        MemberEntity memberEntity = memberService.login(socialUser);
        if(memberEntity != null){
            return R.ok().put("data",memberEntity);
        }else {
            return R.error(BASICTRANCODE.SOCIALUSER_LOGIN_ERROR.getCode(), BASICTRANCODE.SOCIALUSER_LOGIN_ERROR.getMsg());
        }
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo vo){

        MemberEntity memberEntity = memberService.login(vo);
        if(memberEntity != null){
            return R.ok().put("data",memberEntity);
        }else {
            return R.error(BASICTRANCODE.LOGINACTT_PASSWORD_ERROR.getCode(), BASICTRANCODE.LOGINACTT_PASSWORD_ERROR.getMsg());
        }
    }

}
