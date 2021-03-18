package com.bj.product.service.impl;


import com.bj.product.entity.AttrEntity;
import com.bj.product.entity.AttrGroupEntity;
import com.bj.product.service.AttrAttrgroupRelationService;
import com.bj.product.service.AttrGroupService;
import com.bj.product.service.AttrService;
import com.bj.product.vo.BaseAttrsVo;
import com.bj.utils.PageUtils;
import com.bj.utils.Query;
import com.bj.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bj.product.dao.AttrAttrgroupRelationDao;
import com.bj.product.entity.AttrAttrgroupRelationEntity;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {
    @Resource
    private AttrAttrgroupRelationDao relationDao;
    @Resource
    private AttrGroupService attrGroupService;
    @Resource
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R addRelation(List<AttrAttrgroupRelationEntity> entities) {
        this.saveBatch(entities);
        return R.ok();
    }

    @Override
    public R showBaseAttrsByCatId(Long catalogId) {
        //根据分类id查询该分类下所有分组

       // List<AttrGroupEntity> attrGroupEntityList=relationDao.getAttrBycatalogId(catalogId);//关联表方式查询所有记录，此处弃用
        List<AttrGroupEntity> groupEntities = attrGroupService.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catalogId));
        // TODO: 2020/9/19 //此处循环查库在生产中是不可取的，后续可以考虑缓存或者从上述关联sql取出数据整理返回
        //查出所有分组下的基本属性
        List<BaseAttrsVo> collect = groupEntities.stream().map(item -> {
            BaseAttrsVo baseAttrsVo = new BaseAttrsVo();
            BeanUtils.copyProperties(item, baseAttrsVo);
            List<AttrAttrgroupRelationEntity> list = this.list(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id ", item.getAttrGroupId()));
           if(!CollectionUtils.isEmpty(list)){
               List<Long> attrIds = list.stream().map(itemAtr -> {
                   return itemAtr.getAttrId();
               }).collect(Collectors.toList());
               List<AttrEntity> attrEntities = attrService.list(new QueryWrapper<AttrEntity>().in("attr_id ", attrIds));
               baseAttrsVo.setAttrs(attrEntities);
           }
           else {
               baseAttrsVo.setAttrs(new ArrayList<>());
           }
            return baseAttrsVo;
        }).collect(Collectors.toList());


        return R.ok().put("data",collect);
    }

}