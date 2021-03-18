package com.bj.inventory.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.bj.inventory.vo.MergeVo;
import com.bj.inventory.vo.PurchaseFinshedVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bj.inventory.entity.PurchaseEntity;
import com.bj.inventory.service.PurchaseService;
import com.bj.utils.PageUtils;
import com.bj.utils.R;



/**
 * 采购信息
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:40:11
 */
@RestController
@RequestMapping("purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("inventory:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }
    @RequestMapping("unreceive/list")
    //@RequiresPermissions("inventory:purchase:list")
    public R unreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageByUnreceive(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("inventory:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("inventory:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }
    /**
     * 合并采购单
     */
    @RequestMapping("/merge")
    //@RequiresPermissions("inventory:purchase:save")
    public R merge(@RequestBody MergeVo mergeVo){
		return purchaseService.merge(mergeVo);

    }
    /**
     * 领取采购单
     */
    @RequestMapping("/receive")
    //@RequiresPermissions("inventory:purchase:save")
    public R receive(@RequestBody List<Long> ids){
		return purchaseService.receive(ids);

    }
    /**
     * 完成采购单
     */
    @RequestMapping("/finshed")
    //@RequiresPermissions("inventory:purchase:save")
    public R finshed(@RequestBody PurchaseFinshedVo finshedVo){
		return purchaseService.finshed(finshedVo);

    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("inventory:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("inventory:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
