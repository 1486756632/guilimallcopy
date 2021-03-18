package com.bj.product.service.impl;

import com.bj.product.dao.BrandDao;
import com.bj.product.dao.CategoryDao;
import com.bj.product.entity.BrandEntity;
import com.bj.product.entity.CategoryEntity;
import com.bj.utils.PageUtils;
import com.bj.utils.Query;
import com.bj.utils.R;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bj.product.dao.CategoryBrandRelationDao;
import com.bj.product.entity.CategoryBrandRelationEntity;
import com.bj.product.service.CategoryBrandRelationService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Resource
    private BrandDao brandDao;
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private CategoryBrandRelationDao relationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        if (StringUtils.isEmpty(brandId) || StringUtils.isEmpty(catelogId)) {
            throw new RuntimeException("请选择正确的关联关系");
            // TODO: 2020/9/3  此处抛出异常，具体业务可设置对应功能码返回，规范业务代码
        }
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        baseMapper.insert(categoryBrandRelation);
    }

    @Override
    public R getBrandsByCatId(Long catId) {
       //List<BrandEntity> brandEntityList= relationDao.getBrandsByCatId(catId);
        List<CategoryBrandRelationEntity> categoryBrandRelationEntityList = baseMapper.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<BrandEntity> brandEntities = categoryBrandRelationEntityList.stream().map(item -> {
            BrandEntity brandEntity = new BrandEntity();
            brandEntity.setName(item.getBrandName());
            brandEntity.setBrandId(item.getBrandId());
            return brandEntity;
        }).collect(Collectors.toList());
        return R.ok().put("data",brandEntities);
    }

}