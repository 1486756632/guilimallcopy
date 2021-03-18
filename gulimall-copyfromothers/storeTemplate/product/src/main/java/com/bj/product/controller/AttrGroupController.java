package com.bj.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bj.product.entity.AttrAttrgroupRelationEntity;
import com.bj.product.service.AttrAttrgroupRelationService;
import com.bj.product.service.CategoryService;
import com.bj.utils.PageUtils;
import com.bj.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bj.product.entity.AttrGroupEntity;
import com.bj.product.service.AttrGroupService;




/**
 * 属性分组
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrAttrgroupRelationService relationService;

    /**
     * 列表
     */
    @RequestMapping("/list/{catId}")
   // @RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catId") String catId){
        //PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params, catId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        List<Long> catelogPath = categoryService.getCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    /**
     * 获取分组的关联关系列表
     */
    @RequestMapping("/{attrGroupId}/attr/relation")
    //@RequiresPermissions("product:attrgroup:info")
    public R relation(@PathVariable("attrGroupId") Long attrGroupId){
        return attrGroupService.relation(attrGroupId);
    }


    /**
     * 移除关联关系
     */
    @RequestMapping("/attr/relation/delete")
    public R batchDeleteRelation(@RequestBody List<AttrAttrgroupRelationEntity> entities){
        return  attrGroupService.removeRelation(entities);

    }
    /**
     * 获取该分组可以关联的属性
     */
    @RequestMapping("/{attrGroupId}/noattr/relation")
    //@RequiresPermissions("product:attrgroup:info")
    public R getNoRelation(@PathVariable("attrGroupId") Long attrGroupId,@RequestParam Map<String, Object> params){
        return attrGroupService.getNoRelation(attrGroupId,params);
    }

    /**
     * 新增分组关联
     */
    @RequestMapping("/attr/addRelation")
    //@RequiresPermissions("product:attrgroup:info")
    public R addRelation(@RequestBody List<AttrAttrgroupRelationEntity> entities){
        return relationService.addRelation(entities);
    }
    @RequestMapping("/{catalogId}/withattr")
    //@RequiresPermissions("product:attrgroup:info")
    public R showBaseAttrsByCatId(@PathVariable("catalogId")Long catalogId){
        return relationService.showBaseAttrsByCatId(catalogId);
    }
}
