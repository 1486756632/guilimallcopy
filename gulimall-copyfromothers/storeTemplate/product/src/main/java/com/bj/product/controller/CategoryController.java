package com.bj.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bj.utils.PageUtils;
import com.bj.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bj.product.entity.CategoryEntity;
import com.bj.product.service.CategoryService;



/**
 * 商品三级分类
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:category:list")
    public R list(@RequestParam Map<String, Object> params){
        //PageUtils page = categoryService.queryPage(params);
        List<CategoryEntity> categoryEntityList=categoryService.getTreeCategorys();

        return R.ok().put("data",categoryEntityList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    //保证缓存一致性，使该数据改动所有该缓存分区下的缓存失效
    @CacheEvict(value = "category",allEntries = true)
    @RequestMapping("/save")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateDetail(category);
        return R.ok();
    }
    //保证缓存一致性，使该数据改动所有该缓存分区下的缓存失效
    @CacheEvict(value = "category",allEntries = true)
    @RequestMapping("/updateSort")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity[] categorys){
		categoryService.updateBatchById(Arrays.asList(categorys));
        return R.ok();
    }

    /**
     * 删除
     */
    //保证缓存一致性，使该数据改动所有该缓存分区下的缓存失效
    @CacheEvict(value = "category",allEntries = true)
    @RequestMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
		//categoryService.removeByIds(Arrays.asList(catIds));
		categoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
