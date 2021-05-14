package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.entity.SkuImagesEntity;
import com.atguigu.gulimall.product.entity.SpuInfoDescEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import com.atguigu.gulimall.product.service.SkuImagesService;
import com.atguigu.gulimall.product.service.SpuInfoDescService;
import com.atguigu.gulimall.product.vo.ItemSaleAttrVo;
import com.atguigu.gulimall.product.vo.SkuItemVo;
import com.atguigu.gulimall.product.vo.SpuItemAttrGroup;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.SkuInfoDao;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.service.SkuInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired(required=true)
    SkuImagesService skuImagesService;

    @Autowired(required=true)
    SpuInfoDescService spuInfoDescService;

    @Autowired(required=true)
    AttrGroupService attrGroupService;

    @Resource
    ThreadPoolExecutor excutor;

    @Resource
    SkuInfoDao skuInfoDao;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        /**
         * key:
         * catelogId: 0
         * brandId: 0
         * min: 0
         * max: 0
         */
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((wrapper) -> {
                wrapper.eq("sku_id", key).or().like("sku_name", key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {

            queryWrapper.eq("catalog_id", catelogId);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(catelogId)) {
            queryWrapper.eq("brand_id", brandId);
        }

        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)) {
            queryWrapper.ge("price", min);
        }

        String max = (String) params.get("max");

        if (!StringUtils.isEmpty(max)) {
            try {
                BigDecimal bigDecimal = new BigDecimal(max);

                if (bigDecimal.compareTo(new BigDecimal("0")) == 1) {
                    queryWrapper.le("price", max);
                }
            } catch (Exception e) {

            }

        }


        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkuBySpuId(Long spuId) {
        List<SkuInfoEntity> skuList =list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return skuList;
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
        }, excutor);

        //2.查询sku的图片信息
        CompletableFuture<Void> skuImgFu = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> entities = skuImagesService.getImgBySkuId(skuId);
            skuItemVo.setImages(entities);
        }, excutor);

        //3.获得spu的销售属性组合
        CompletableFuture<Void> saleFu = completableFuture.thenAcceptAsync((res) -> {
            List<ItemSaleAttrVo> saleAttrVos = getSaleAttrVosBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(saleAttrVos);
        }, excutor);

        //4.获取spu的介绍
        CompletableFuture<Void> spuDescFu = completableFuture.thenAcceptAsync((res) -> {
            SpuInfoDescEntity descEntity = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setDesc(descEntity);
        }, excutor);

        //获取spu的规格参数
        CompletableFuture<Void> spuAttrFu = completableFuture.thenAcceptAsync((res) -> {
            List<SpuItemAttrGroup> spuItemAttrGroups = attrGroupService.getAttrBySpuIdAndCatId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setGroupAttrs(spuItemAttrGroups);
        }, excutor);
        CompletableFuture.allOf(skuImgFu,saleFu,spuAttrFu,spuDescFu).join();
        return skuItemVo;
    }
    private List<ItemSaleAttrVo> getSaleAttrVosBySpuId(Long spuId) {
        return skuInfoDao.getSaleAttrVosBySpuId(spuId);
    }
}