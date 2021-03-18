package com.bj.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.inventory.entity.PurchaseEntity;
import com.bj.utils.PageUtils;
import com.bj.inventory.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:40:11
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByCondition(Map<String, Object> params);

    List<PurchaseDetailEntity> listBypurIds(List<Long> purIds);
}

