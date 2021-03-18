package com.bj.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.bj.constant.ProductConstant;
import com.bj.dto.SkuDeduceTo;
import com.bj.dto.SpuBoundsTo;
import com.bj.dto.es.SkuEsModel;
import com.bj.product.entity.*;
import com.bj.product.feign.CouponFeignService;
import com.bj.product.feign.SearchFeignService;
import com.bj.product.feign.WareFeignService;
import com.bj.product.service.*;
import com.bj.product.vo.*;
import com.bj.utils.PageUtils;
import com.bj.utils.Query;
import com.bj.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bj.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    private SpuInfoDescService descService;
    @Autowired
    private SpuImagesService imagesService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private ProductAttrValueService productAttrValueService;
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private CouponFeignService couponFeignService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private WareFeignService wareFeignService;
    @Autowired
    private SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public R saveSpu(SpuSaveVo spuSaveVo) {
        //1.保存spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.save(spuInfoEntity);
        //2.保存spu的描述图片 pms_spu_info_desc
        List<String> decript = spuSaveVo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(spuInfoEntity.getId());
        if (!CollectionUtils.isEmpty(decript)) {
            descEntity.setDecript(String.join(",", decript));
        }
        descService.save(descEntity);
        //3.保存spu的图片集 pms_spu_images
        imagesService.saveImgs(spuInfoEntity.getId(), spuSaveVo.getImages());
        // 4.保存spu的规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        if (!CollectionUtils.isEmpty(baseAttrs)) {
            List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
                ProductAttrValueEntity attrValueEntity = new ProductAttrValueEntity();
                attrValueEntity.setAttrId(attr.getAttrId());
                // TODO: 2020/9/20 注意此处循环查库
                AttrEntity attrEntity = attrService.getById(attr.getAttrId());
                attrValueEntity.setAttrName(attrEntity.getAttrName());
                attrValueEntity.setAttrValue(attr.getAttrValues());
                attrValueEntity.setSpuId(spuInfoEntity.getId());
                attrValueEntity.setQuickShow(attr.getShowDesc());
                attrValueEntity.setAttrId(attr.getAttrId());
                return attrValueEntity;
            }).collect(Collectors.toList());
            productAttrValueService.saveBatch(collect);
        }

        //5保存spu的积分信息 sms_pu_bounds
        Bounds bounds = spuSaveVo.getBounds();
        if (bounds != null) {
            SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
            BeanUtils.copyProperties(bounds, spuBoundsTo);
            spuBoundsTo.setSpuId(spuInfoEntity.getId());
            couponFeignService.save(spuBoundsTo);
        }
        //6.保存当前spu对应的sku信息
        List<Skus> skus = spuSaveVo.getSkus();
        //6.1 sku基本信息 pms_sku_info
        SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
        skus.stream().forEach(sku -> {
            BeanUtils.copyProperties(sku, skuInfoEntity);
            skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
            skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
            skuInfoEntity.setSaleCount(0l);
            skuInfoEntity.setSpuId(spuInfoEntity.getId());
            String ddefaultImg = "";
            for (Image image : sku.getImages()) {
                if (image.getDefaultImg() == 1) {
                    ddefaultImg = image.getImgUrl();
                    skuInfoEntity.setSkuDefaultImg(ddefaultImg);
                    break;
                }
            }
            skuInfoService.save(skuInfoEntity);
            //6.2 sku的图片信息 pms_sku_images
            List<SkuImagesEntity> collect = sku.getImages().stream().filter(item -> {
                return !StringUtils.isEmpty(item.getImgUrl());
            }).map(image -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                BeanUtils.copyProperties(image, skuImagesEntity);
                skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                return skuImagesEntity;
            }).collect(Collectors.toList());
            skuImagesService.saveBatch(collect);
            //6.3 sku的销售属性信息 pms_sku_sale_attr_value
            List<Attr> attrs = sku.getAttr();
            List<SkuSaleAttrValueEntity> saleAttrValueEntities = attrs.stream().map(attr -> {
                SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                return skuSaleAttrValueEntity;
            }).collect(Collectors.toList());
            skuSaleAttrValueService.saveBatch(saleAttrValueEntities);

            //6.4 sku的优惠满减等信息 `sms_sku_full_reduction``sms_sku_ladder``sms_member_price`
            SkuDeduceTo skuDeduceTo = new SkuDeduceTo();
            BeanUtils.copyProperties(sku, skuDeduceTo);
            if (skuDeduceTo.getFullCount() > 0 || skuDeduceTo.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
                skuDeduceTo.setSkuId(skuInfoEntity.getSkuId());
                couponFeignService.saveSkuReduce(skuDeduceTo);
            }

        });

        return R.ok();
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<SpuInfoEntity>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(w -> {
                w.eq("id", key).or().like("spu_name", key)
                        .or().like("spu_description", key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("publish_status", status);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId)) {
            wrapper.eq("brand_id", brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public R up(Long spuId) {
        //查询当前spu可以被用来检索的规格属性
        List<ProductAttrValueEntity> attrValueEntities = productAttrValueService.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        List<Long> attrIds = attrValueEntities.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        List<Long> searchAttr = attrService.getSearchAttr(attrIds);
        Set set = new HashSet(searchAttr);
        List<SkuEsModel.Attrs> attrsList = attrValueEntities.stream().filter(attrValue -> {
            return set.contains(attrValue.getAttrId());
        }).map(attrValue -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(attrValue, attrs);
            return attrs;
        }).collect(Collectors.toList());

        List<SkuInfoEntity> skuInfoEntityList = skuInfoService.getSkuBySpuId(spuId);
        //远程查询sku对应的商品库存
        List<Long> skuIds = skuInfoEntityList.stream().map(skuInfoEntity -> {
            return skuInfoEntity.getSkuId();
        }).collect(Collectors.toList());
        R r = wareFeignService.hasStockBySku(skuIds);
        Map<Long, Boolean> stockMap = new HashMap<>();
        if (0 == (Integer) r.get("code")) {
            List<Object> data = (List<Object>) r.get("data");
            data.stream().forEach(obj -> {
                String s = JSON.toJSONString(obj);
                Map map = JSON.parseObject(s, Map.class);
                Integer skuId = (Integer) map.get("skuId");
                stockMap.put(Long.parseLong(skuId.toString()), (Boolean) map.get("hasStock"));
            });
        }
        List<SkuEsModel> esModels = skuInfoEntityList.stream().map(skuInfoEntity -> {
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(skuInfoEntity, esModel);
            esModel.setSkuPrice(skuInfoEntity.getPrice());
            esModel.setSkuImg(skuInfoEntity.getSkuDefaultImg());
            BrandEntity brandEntity = brandService.getById(skuInfoEntity.getBrandId());
            esModel.setBrandName(brandEntity.getName());
            esModel.setBrandImg(brandEntity.getLogo());
            CategoryEntity categoryEntity = categoryService.getById(skuInfoEntity.getCatalogId());
            esModel.setCatalogName(categoryEntity.getName());
            esModel.setHasStock(stockMap.get(skuInfoEntity.getSkuId()));
            // TODO: 2020/11/3 暂时先赋值0
            esModel.setHotScore(0l);
            esModel.setAttrs(attrsList);
            return esModel;
        }).collect(Collectors.toList());
        //存储到es服务
        R r1 = searchFeignService.productUp(esModels);
        if (0 == (Integer) r1.get("code")) {
            SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
            spuInfoEntity.setUpdateTime(new Date());
            spuInfoEntity.setPublishStatus(ProductConstant.SPU_UP.getCode());
            this.update(spuInfoEntity, new QueryWrapper<SpuInfoEntity>().eq("id", spuId));
        }
        return r1;
    }

    @Override
    public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {
        return getById(skuInfoService.getById(skuId).getSpuId());
    }


}