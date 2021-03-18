package com.bj.product.service.impl;

import com.bj.constant.ProductConstant;
import com.bj.product.dao.AttrAttrgroupRelationDao;
import com.bj.product.entity.AttrAttrgroupRelationEntity;
import com.bj.product.entity.ProductAttrValueEntity;
import com.bj.product.service.CategoryService;
import com.bj.product.service.ProductAttrValueService;
import com.bj.product.vo.AttrRspVo;
import com.bj.product.vo.AttrVo;
import com.bj.utils.PageUtils;
import com.bj.utils.Query;
import com.bj.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bj.product.dao.AttrDao;
import com.bj.product.entity.AttrEntity;
import com.bj.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Resource
    private AttrAttrgroupRelationDao relationDao;
    @Resource
    private AttrDao attrDao;
    @Resource
    private CategoryService categoryService;
    @Resource
    private ProductAttrValueService attrValueService;

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catId) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>();
        String keys = (String) params.get("keys");
        if (!StringUtils.isEmpty(keys)) {
            wrapper.and((attr) -> {
                attr.eq("attr_id", keys).or().like("attr_name ", keys)
                        .or().like("value_select", keys);
            });

        }
        if (catId != 0) {
            wrapper.eq("catelog_id", catId);
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper);
        return new PageUtils(page);


    }

    @Transactional
    @Override
    public R saveDetail(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.save(attrEntity);
        if (attr.getAttrType() == ProductConstant.ATTR_TYPE_BASE.getCode()&&attr.getAttrGroupId()!=null) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationDao.insert(relationEntity);
        }
        return R.ok();
    }

    @Override
    public PageUtils getDatas(Map<String, Object> params, Long catId, String type) {
        if (catId != 0) {
            params.put("catId", catId);
        }
        //区分销售属性和基本属性
        params.put("type", "base".equalsIgnoreCase(type) ? "1" : "0");
        Object page1 = params.get("page");
        Object limit = params.get("limit");
        if (page1 == null || limit == null) {
            throw new RuntimeException("请输入分页参数");// TODO: 2020/9/6 后续应改为自己业务逻辑编码
        }
        int page = Integer.parseInt((String) page1);
        int size = Integer.parseInt((String) limit);
        page = (page - 1) * size;
        params.put("page", page);
        params.put("limit", size);
        List<AttrRspVo> datas = attrDao.getDatas(params);
        Integer total = attrDao.getDatsTotal(params);

        return new PageUtils(datas, total, size, Integer.parseInt((String) page1));
    }

    @Transactional
    @Override
    public R updateAttrById(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);
        if (attr.getAttrType() == ProductConstant.ATTR_TYPE_BASE.getCode()&&attr.getAttrGroupId()!=null) {
            Integer count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id ", attr.getAttrId()));
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attr.getAttrId());
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            if (count > 0) {
                relationDao.update(relationEntity, new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id ", attr.getAttrId()));
            } else {
                relationDao.insert(relationEntity);
            }
        }
        return R.ok();
    }

    @Override
    public R listForSpu(Long spuId) {
        List<ProductAttrValueEntity> attrValueEntities = attrValueService.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        return R.ok().put("data",attrValueEntities);
    }

    @Override
    public List<Long> getSearchAttr(List<Long> attrIds) {
        List<AttrEntity> list = this.list(new QueryWrapper<AttrEntity>().eq("search_type", 1).in("attr_id", attrIds));
        List<Long> attrs = list.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        return attrs;
    }

}