package com.bj.product.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bj.dto.BrandTo;
import com.bj.utils.PageUtils;
import com.bj.utils.R;
import com.bj.valid.AddGroup;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.bj.product.entity.BrandEntity;
import com.bj.product.service.BrandService;


/**
 * 品牌
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Validated(AddGroup.class) @RequestBody BrandEntity brand){
		brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@RequestBody BrandEntity brand){
		brandService.updateDetail(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

    @RequestMapping("/infos")
    public @ResponseBody List<BrandTo> info(@RequestBody List<Long> brandIds) {
        List<BrandEntity> brand = brandService.listByIds(brandIds);
        List<BrandTo> brandTos=new ArrayList<>();
        if(!CollectionUtils.isEmpty(brand)){
            for (BrandEntity brandEntity : brand) {
                BrandTo brandTo=new BrandTo();
                brandTo.setBrandId(brandEntity.getBrandId());
                brandTo.setBrandName(brandEntity.getName());
                brandTos.add(brandTo);
            }
        }
        return brandTos;
    }

}
