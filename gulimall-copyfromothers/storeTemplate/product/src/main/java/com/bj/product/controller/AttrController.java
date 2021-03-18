package com.bj.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bj.constant.ProductConstant;
import com.bj.product.dao.AttrAttrgroupRelationDao;
import com.bj.product.entity.AttrAttrgroupRelationEntity;
import com.bj.product.service.CategoryService;
import com.bj.product.vo.AttrRspVo;
import com.bj.product.vo.AttrVo;
import com.bj.utils.PageUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.bj.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bj.product.entity.AttrEntity;
import com.bj.product.service.AttrService;

import javax.annotation.Resource;


/**
 * 商品属性
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
    @Autowired
    private CategoryService categoryService;
    @Resource
    private AttrAttrgroupRelationDao relationDao;

    /**
     * 列表
     */
    @RequestMapping("/list/{catId}")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catId") Long catId){
        PageUtils page = attrService.queryPage(params,catId);

        return R.ok().put("page", page);
    }
    @RequestMapping("/{type}/list/{catId}")
    //@RequiresPermissions("product:attr:list")
    public R getDatas(@RequestParam Map<String, Object> params,@PathVariable("catId") Long catId,
                      @PathVariable("type") String type){
        PageUtils datas = attrService.getDatas(params, catId,type);

        return R.ok().put("page", datas);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrEntity attr = attrService.getById(attrId);
        List<Long> catelogPath = categoryService.getCatelogPath(attr.getCatelogId());
        AttrRspVo attrRspVo=new AttrRspVo();
        BeanUtils.copyProperties(attr,attrRspVo);
        if(attr.getAttrType()== ProductConstant.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if(relationEntity!=null){
                attrRspVo.setAttrGroupId(relationEntity.getAttrGroupId());
            }
        }
        attrRspVo.setCatelogPath(catelogPath);

        return R.ok().put("attr", attrRspVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		return  attrService.saveDetail(attr);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttrById(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }
    /**
     * 查询spu的规格
     */
    @RequestMapping("/base/listforspu/{spuId}")
    //@RequiresPermissions("product:attr:delete")
    public R listForSpu(@PathVariable("spuId")  Long spuId){
        return attrService.listForSpu(spuId);

    }



}
