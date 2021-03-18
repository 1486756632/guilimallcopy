package com.bj.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bj.constant.ProductConstant;
import com.bj.product.dao.AttrAttrgroupRelationDao;
import com.bj.product.dao.AttrGroupDao;
import com.bj.product.entity.AttrAttrgroupRelationEntity;
import com.bj.product.entity.AttrEntity;
import com.bj.product.entity.AttrGroupEntity;
import com.bj.product.service.AttrAttrgroupRelationService;
import com.bj.product.service.AttrGroupService;
import com.bj.product.service.AttrService;
import com.bj.product.vo.SpuItemAttrGroup;
import com.bj.utils.PageUtils;
import com.bj.utils.Query;
import com.bj.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, String catId) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        String keys = (String) params.get("keys");
        if (!StringUtils.isEmpty(keys)) {
            wrapper.and((attr) -> {
                attr.eq("attr_group_id", keys).or().like("attr_group_name ", keys)
                        .or().like("descript", keys);
            });

        }
        if ("0".equals(catId)) {
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);

        } else {
            wrapper.eq("catelog_id", catId);
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }

    }

    @Override
    public R relation(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id ", attrGroupId));
        if (CollectionUtils.isEmpty(relationEntities)){
            return R.ok().put("data",new ArrayList<>());
        }
        List<Long> attrIds = relationEntities.stream().map(relation -> {
            return relation.getAttrId();
        }).collect(Collectors.toList());

        List<AttrEntity> attrEntities = attrService.listByIds(attrIds);
        return R.ok().put("data",attrEntities);
    }

    @Override
    public R removeRelation(List<AttrAttrgroupRelationEntity> entities) {
        if (CollectionUtils.isEmpty(entities)){
            throw new RuntimeException("非法操作"); // TODO: 2020/9/13
        }
     relationDao.deleteBatch(entities);
        return R.ok();
    }

    @Override
    public R getNoRelation(Long attrGroupId, Map<String, Object> params) {
        //1.首先查出该分组信息
        AttrGroupEntity attrGroupEntity = this.getById(attrGroupId);
        //2/查出该分组所属分类下所有未被关联过的属性
        Long catelogId = attrGroupEntity.getCatelogId();
        List<AttrGroupEntity> groupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> groupIds = groupEntities.stream().map(item -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id ", groupIds));
        List<Long> collect = relationEntities.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        QueryWrapper<AttrEntity> queryWrapper=new QueryWrapper();
        queryWrapper.eq("catelog_id", catelogId).eq("attr_type", ProductConstant.ATTR_TYPE_BASE.getCode());
        if(!CollectionUtils.isEmpty(collect)){
            queryWrapper.notIn("attr_id",collect);
        }
         if(params.get("key")!=null){
            String key =(String) params.get("key");
            queryWrapper.and(que->{
                que.eq("attr_id",key).or().like("attr_name",key);
            });
         }
        IPage<AttrEntity> page = attrService.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        return R.ok().put("page",pageUtils);
    }

    @Override
    public List<SpuItemAttrGroup> getAttrBySpuIdAndCatId(Long spuId, Long catalogId) {
       return relationDao.getAttrBySpuIdAndCatId( spuId,  catalogId);
    }


}