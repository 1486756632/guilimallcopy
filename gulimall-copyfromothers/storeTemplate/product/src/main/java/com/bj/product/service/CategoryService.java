package com.bj.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.product.entity.CategoryEntity;
import com.bj.product.vo.Catelog2Vo;
import com.bj.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> getTreeCategorys();

    void removeMenuByIds(List<Long> asList);

    List<Long> getCatelogPath(Long catId);

    void updateDetail(CategoryEntity category);

    List<CategoryEntity> selectFirstCategory();

    Map<String, List<Catelog2Vo>> getCatlog();
}

