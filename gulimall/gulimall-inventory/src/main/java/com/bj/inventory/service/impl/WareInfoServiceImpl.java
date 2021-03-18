package com.bj.inventory.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bj.inventory.feign.MemberFeignService;
import com.bj.inventory.vo.FareVo;
import com.bj.inventory.vo.MemberAddressVo;
import com.bj.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bj.utils.PageUtils;
import com.bj.utils.Query;

import com.bj.inventory.dao.WareInfoDao;
import com.bj.inventory.entity.WareInfoEntity;
import com.bj.inventory.service.WareInfoService;
import org.springframework.util.StringUtils;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {
    @Autowired
    private MemberFeignService memberFeignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                new QueryWrapper<WareInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByContidion(Map<String, Object> params) {
        QueryWrapper<WareInfoEntity> wrapper=new QueryWrapper<WareInfoEntity>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and(w->{
                w.eq("id",key).or().like("name",key)
                        .or().like("address",key)
                        .or().eq("address",key);
            });
        }
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public FareVo getFare(Long addrId) {
        R info = memberFeignService.addrInfo(addrId);
        FareVo fareVo = new FareVo();
        Object memberReceiveAddress = info.get("memberReceiveAddress");
        String s = JSON.toJSONString(memberReceiveAddress);
        MemberAddressVo addressVo = JSON.parseObject(s, MemberAddressVo.class);
        fareVo.setMemberAddressVo(addressVo);
        if(addressVo != null){
            String phone = addressVo.getPhone();
            if(phone == null || phone.length() < 2){
                phone = new Random().nextInt(100) + "";
            }
            BigDecimal decimal = new BigDecimal(phone.substring(phone.length() - 1));
            fareVo.setFare(decimal);
        }else{
            fareVo.setFare(new BigDecimal("20"));
        }
        return fareVo;
    }

}