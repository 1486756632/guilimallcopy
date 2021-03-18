package com.bj.product.service.impl;

import com.bj.product.entity.AttrGroupEntity;
import com.bj.product.entity.CategoryBrandRelationEntity;
import com.bj.product.service.CategoryBrandRelationService;
import com.bj.utils.PageUtils;
import com.bj.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bj.product.dao.BrandDao;
import com.bj.product.entity.BrandEntity;
import com.bj.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String keys = (String) params.get("keys");
        if (!StringUtils.isEmpty(keys)) {
            QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("brand_id", keys).or().like("name ", keys)
                    .or().like("descript", keys).or().eq("show_status", keys)
                    .or().eq("first_letter", keys);
            IPage<BrandEntity> page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
        );
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void updateDetail(BrandEntity brand) {
        this.updateById(brand);
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setBrandName(brand.getName());
        categoryBrandRelationService.update(relationEntity,
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brand.getBrandId()));
    }

}