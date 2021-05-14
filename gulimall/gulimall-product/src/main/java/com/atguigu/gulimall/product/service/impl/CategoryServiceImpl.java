package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2、组装成父子的树形结构

        //2.1）、找到所有的一级分类
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
             categoryEntity.getParentCid() == 0
        ).map((menu)->{
            menu.setChildren(getChildrens(menu,entities));
            return menu;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());




        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO  1、检查当前删除的菜单，是否被别的地方引用

        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    //[2,25,225]
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);


        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联的数据
     * @param category
     */
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    //225,25,2
    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        //1、收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;

    }


    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            //1、找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity,all));
            return categoryEntity;
        }).sorted((menu1,menu2)->{
            //2、菜单的排序
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }

    /*
     * 采用cache直接缓存
     * */
    @Cacheable(value = {"category"},key = "#root.method.name")
    @Override
    public List<CategoryEntity> selectFirstCategory() {
        System.out.println("一级分类加入缓存");
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("cat_level", 1));
        return categoryEntities;
    }

    @Cacheable(value = {"category"},key = "#root.method.name")
    @Override
    public Map<String, List<Catelog2Vo>> getCatlog() {
        System.out.println("二三级级分类加入缓存");
        Map<String, List<Catelog2Vo>> catLogFromDb = getCatLogFromDb();
        return catLogFromDb;
        //以下注释代码推荐强一致性情况下放开，此处直接使用springCache
       /* String catlogJson = redisTemplate.opsForValue().get("catlogJson");
        if (catlogJson==null){
            //缓存中没有从数据库拿
            Map<String, List<Catelog2Vo>> catLogToRedis = getCatLogToRedis();
            return catLogToRedis;
        }
        Map<String, List<Catelog2Vo>> stringListMap = JSONObject.parseObject(catlogJson, Map.class);
        return  stringListMap;*/

    }
    private Map<String, List<Catelog2Vo>> getCatLogFromDb(){
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper();
        wrapper.ne("cat_level", 1);
        List<CategoryEntity> categoryEntities = baseMapper.selectList(wrapper);
        //查询二级三级分类
        List<Catelog2Vo> catelog2VoList = categoryEntities.stream().map(l2 -> {
            Catelog2Vo catelog2Vo = new Catelog2Vo();
            catelog2Vo.setCatalog1Id(l2.getParentCid());
            catelog2Vo.setId(l2.getCatId());
            catelog2Vo.setName(l2.getName());
            List<CategoryEntity> children = getChildren(l2, categoryEntities);
            List<Catelog2Vo.Catelog3Vo> catelog3VoList = children.stream().map(l3 -> {
                Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo();
                catelog3Vo.setCatalog2Id(l2.getCatId());
                catelog3Vo.setId(l3.getCatId());
                catelog3Vo.setName(l3.getName());
                return catelog3Vo;
            }).collect(Collectors.toList());
            catelog2Vo.setCatalog3List(catelog3VoList);
            return catelog2Vo;
        }).collect(Collectors.toList());

        Map<String, List<Catelog2Vo>> catLogToRedis=new HashMap<>();
        //构造首页json数据
        if (!CollectionUtils.isEmpty(catelog2VoList)) {
            Map<String, List<Catelog2Vo>> collect = catelog2VoList.stream().collect(Collectors.toMap(catelog2Vo -> catelog2Vo.getCatalog1Id().toString(), v -> {
                        List<Catelog2Vo> list = new ArrayList<>();
                        list.add(v);
                        return list;
                    },
                    (List<Catelog2Vo> v1, List<Catelog2Vo> v2) -> {
                        v1.addAll(v2);
                        return v1;
                    }));

            catLogToRedis= collect;
        }
        return catLogToRedis;
    }

    private List<CategoryEntity> getChildren(CategoryEntity cur, List<CategoryEntity> categoryEntities) {
        List<CategoryEntity> entityList = categoryEntities.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(cur.getCatId());
        }).map(categoryEntity -> {
            categoryEntity.setChildren(getChildren(categoryEntity, categoryEntities));
            return categoryEntity;
        }).sorted((cat1, cat2) -> {
            return (cat1.getSort() == null ? 0 : cat1.getSort()) - (cat2.getSort() == null ? 0 : cat2.getSort());
        }).collect(Collectors.toList());

        return entityList;
    }
}