package com.bj.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.inventory.vo.FareVo;
import com.bj.utils.PageUtils;
import com.bj.inventory.entity.WareInfoEntity;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:40:11
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByContidion(Map<String, Object> params);

    FareVo getFare(Long addrId);
}

