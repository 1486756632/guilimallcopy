package com.bj.product.service.impl;

import com.bj.utils.PageUtils;
import com.bj.utils.Query;
import com.bj.utils.R;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bj.product.dao.ProductAttrValueDao;
import com.bj.product.entity.ProductAttrValueEntity;
import com.bj.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {
    @Resource
    private ProductAttrValueDao productAttrValueDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }
    @Transactional
    @Override
    public R updateForSpu(Long spuId, List<ProductAttrValueEntity> attrValueEntities) {
        productAttrValueDao.updateBatchBySpuId(spuId,attrValueEntities);
        return R.ok();
    }

}