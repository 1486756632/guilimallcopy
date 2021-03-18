package com.bj.vip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.utils.PageUtils;
import com.bj.vip.entity.MemberEntity;
import com.bj.vip.exception.PhoneExistException;
import com.bj.vip.exception.UserNameExistException;
import com.bj.vip.vo.MemberLoginVo;
import com.bj.vip.vo.SocialUser;
import com.bj.vip.vo.UserRegisterVo;

import java.util.Map;

/**
 * 会员
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 22:24:55
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(UserRegisterVo userRegisterVo) throws PhoneExistException, UserNameExistException;

    void checkPhone(String phone) throws PhoneExistException;

    void checkUserName(String username) throws UserNameExistException;

    /**
     * 普通登录
     */
    MemberEntity login(MemberLoginVo vo);

    /**
     * 社交登录
     */
    MemberEntity login(SocialUser socialUser);
}

