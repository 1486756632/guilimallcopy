package com.bj.product.service.impl;

import com.bj.product.entity.SkuImagesEntity;
import com.bj.product.entity.SpuInfoDescEntity;
import com.bj.product.entity.SpuInfoEntity;
import com.bj.product.service.AttrGroupService;
import com.bj.product.service.SkuImagesService;
import com.bj.product.service.SpuInfoDescService;
import com.bj.product.vo.ItemSaleAttrVo;
import com.bj.product.vo.SkuItemVo;
import com.bj.product.vo.SpuItemAttrGroup;
import com.bj.utils.PageUtils;
import com.bj.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bj.product.dao.SkuInfoDao;
import com.bj.product.entity.SkuInfoEntity;
import com.bj.product.service.SkuInfoService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private AttrGroupService attrGroupService;
    @Resource
    private  SkuInfoDao skuInfoDao;
    @Autowired
    private ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByContion(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(w -> {
                w.eq("sku_id", key).or().like("sku_name", key)
                        .or().like("sku_subtitle", key);
            });
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId)) {
            wrapper.eq("brand_id", brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }
        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)) {
            wrapper.ge("price", min);
        }
        String max = (String) params.get("max");
        if (!StringUtils.isEmpty(max)) {
            wrapper.le("price", max);
        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkuBySpuId(Long spuId) {
        List<SkuInfoEntity> skus = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return skus;
    }

    @Override
    public SkuItemVo getItem(Long skuId) {
        //采用多线程异步编排方式执行，减少rt
        SkuItemVo skuItemVo = new SkuItemVo();
        CompletableFuture<SkuInfoEntity> completableFuture = CompletableFuture.supplyAsync(() -> {
            /* 1.查询sku基本信息 */
            SkuInfoEntity skuInfoEntity = getById(skuId);
            skuItemVo.setInfo(skuInfoEntity);
            return skuInfoEntity;
        }, executor);

        //2.查询sku的图片信息
        CompletableFuture<Void> skuImgFu = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> entities = skuImagesService.getImgBySkuId(skuId);
            skuItemVo.setImages(entities);
        }, executor);

        //3.获得spu的销售属性组合
        CompletableFuture<Void> saleFu = completableFuture.thenAcceptAsync((res) -> {
            List<ItemSaleAttrVo> saleAttrVos = getSaleAttrVosBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(saleAttrVos);
        }, executor);

        //4.获取spu的介绍
        CompletableFuture<Void> spuDescFu = completableFuture.thenAcceptAsync((res) -> {
            SpuInfoDescEntity descEntity = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setDesc(descEntity);
        }, executor);

        //获取spu的规格参数
        CompletableFuture<Void> spuAttrFu = completableFuture.thenAcceptAsync((res) -> {
            List<SpuItemAttrGroup> spuItemAttrGroups = attrGroupService.getAttrBySpuIdAndCatId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setGroupAttrs(spuItemAttrGroups);
        }, executor);
        CompletableFuture.allOf(skuImgFu,saleFu,spuAttrFu,spuDescFu).join();
        return skuItemVo;
    }

    private List<ItemSaleAttrVo> getSaleAttrVosBySpuId(Long spuId) {
       return skuInfoDao.getSaleAttrVosBySpuId(spuId);
    }

}