package com.bj.inventory.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.bj.exceptionCode.BASICTRANCODE;
import com.bj.exceptionCode.NotStockException;
import com.bj.inventory.vo.WareSkuLockVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bj.inventory.entity.WareSkuEntity;
import com.bj.inventory.service.WareSkuService;
import com.bj.utils.PageUtils;
import com.bj.utils.R;



/**
 * 商品库存
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:40:11
 */
@Slf4j
@RestController
@RequestMapping("waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("inventory:waresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPageByContidion(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("inventory:waresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("inventory:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("inventory:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("inventory:waresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 查询sku对应商品是否有库存
     */
    @RequestMapping("/hasStock")
    //@RequiresPermissions("inventory:waresku:delete")
    public R hasStockBySku(@RequestBody List<Long> skuIds){
		return wareSkuService.hasStockBySku(skuIds);

    }
    @PostMapping("/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVo vo){
        try {
            wareSkuService.orderLockStock(vo);
            return R.ok();
        } catch (NotStockException e) {
            log.warn("\n" + e.getMessage());
        }
        return R.error(BASICTRANCODE.NOT_STOCK_EXCEPTION.getCode(),BASICTRANCODE.NOT_STOCK_EXCEPTION.getMsg());
    }
}
