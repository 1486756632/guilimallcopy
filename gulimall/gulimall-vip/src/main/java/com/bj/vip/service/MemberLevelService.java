package com.bj.vip.service;

import com.atguigu.common.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.vip.entity.MemberLevelEntity;

import java.util.Map;

/**
 * 会员等级
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 22:24:55
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

