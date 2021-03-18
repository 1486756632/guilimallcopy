package com.bj.cupon.service.impl;

import com.bj.cupon.entity.MemberPriceEntity;
import com.bj.cupon.entity.SkuLadderEntity;
import com.bj.cupon.service.MemberPriceService;
import com.bj.cupon.service.SkuLadderService;
import com.bj.dto.MemberPrice;
import com.bj.dto.SkuDeduceTo;
import com.bj.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bj.utils.PageUtils;
import com.bj.utils.Query;

import com.bj.cupon.dao.SkuFullReductionDao;
import com.bj.cupon.entity.SkuFullReductionEntity;
import com.bj.cupon.service.SkuFullReductionService;
import org.springframework.transaction.annotation.Transactional;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {
    @Autowired
    private SkuLadderService skuLadderService;
    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public R saveSkuReduce(SkuDeduceTo skuDeduceTo) {
        //6.4 sku的优惠满减等信息 `sms_sku_full_reduction``sms_sku_ladder``sms_member_price`
        if(skuDeduceTo.getFullPrice().compareTo(new BigDecimal("0"))==1){
            SkuFullReductionEntity fullReductionEntity = new SkuFullReductionEntity();
            BeanUtils.copyProperties(skuDeduceTo, fullReductionEntity);
            fullReductionEntity.setAddOther(skuDeduceTo.getCountStatus());
            this.save(fullReductionEntity);
        }
        if(skuDeduceTo.getDiscount().compareTo(new BigDecimal("0"))==1){
            SkuLadderEntity ladderEntity = new SkuLadderEntity();
            BeanUtils.copyProperties(skuDeduceTo, ladderEntity);
            ladderEntity.setAddOther(skuDeduceTo.getCountStatus());
            skuLadderService.save(ladderEntity);
        }
        List<MemberPrice> memberPriceList = skuDeduceTo.getMemberPrice();
        List<MemberPriceEntity> collect = memberPriceList.stream().filter(item->{
           return item.getPrice().compareTo(new BigDecimal("0"))==1;
        }).map(memberPrice -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setAddOther(1);
            memberPriceEntity.setMemberLevelId(memberPrice.getId());
            memberPriceEntity.setMemberLevelName(memberPrice.getName());
            memberPriceEntity.setMemberPrice(memberPrice.getPrice());
            memberPriceEntity.setSkuId(skuDeduceTo.getSkuId());
            return memberPriceEntity;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(collect);
        return R.ok();
    }
}