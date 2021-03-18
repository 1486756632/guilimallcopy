package com.bj.product.service.impl;

import com.bj.utils.PageUtils;
import com.bj.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bj.product.dao.SkuSaleAttrValueDao;
import com.bj.product.entity.SkuSaleAttrValueEntity;
import com.bj.product.service.SkuSaleAttrValueService;
import org.springframework.util.CollectionUtils;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<String> getSkuSaleAttrValuesAsStringList(Long skuId) {
        List<SkuSaleAttrValueEntity> valueEntities = baseMapper.selectList(new QueryWrapper<SkuSaleAttrValueEntity>().eq("sku_id", skuId));
        if(!CollectionUtils.isEmpty(valueEntities)){
            List<String> collect = valueEntities.stream().map(item -> {
                return item.getAttrName() + ":" + item.getAttrValue();
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }

}