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

import com.bj.product.dao.SpuImagesDao;
import com.bj.product.entity.SpuImagesEntity;
import com.bj.product.service.SpuImagesService;
import org.springframework.util.CollectionUtils;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveImgs(Long id, List<String> images) {
        if(id==null|| CollectionUtils.isEmpty(images)){
            return;
        }
        List<SpuImagesEntity> collect = images.stream().map(img -> {
            SpuImagesEntity imagesEntity = new SpuImagesEntity();
            imagesEntity.setSpuId(id);
            imagesEntity.setImgUrl(img);
            return imagesEntity;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

}