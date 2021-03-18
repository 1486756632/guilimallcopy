package com.bj.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.inventory.vo.MergeVo;
import com.bj.inventory.vo.PurchaseFinshedVo;
import com.bj.utils.PageUtils;
import com.bj.inventory.entity.PurchaseEntity;
import com.bj.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:40:11
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByUnreceive(Map<String, Object> params);

    R merge(MergeVo mergeVo);

    R receive(List<Long> ids);

    R finshed(PurchaseFinshedVo finshedVo);
}

