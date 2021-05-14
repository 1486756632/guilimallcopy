package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;
import com.atguigu.gulimall.product.dao.WntIpDao;
import com.atguigu.gulimall.product.dao.WntParameterDao;
import com.atguigu.gulimall.product.entity.WntIpEntity;
import com.atguigu.gulimall.product.entity.WntParameterEntity;
import com.atguigu.gulimall.product.service.WntIpService;
import com.atguigu.gulimall.product.service.WntParameterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("wntIpService")
public class WntIpServiceImpl extends ServiceImpl<WntIpDao, WntIpEntity> implements WntIpService {

    @Autowired
    WntIpService wntIpService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WntIpEntity> page = this.page(
                new Query<WntIpEntity>().getPage(params),
                new QueryWrapper<WntIpEntity>()
        );

        return new PageUtils(page);
    }


}