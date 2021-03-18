package com.bj.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bj.product.entity.CategoryBrandRelationEntity;
import com.bj.product.service.CategoryBrandRelationService;
import com.bj.product.vo.Catelog2Vo;
import com.bj.utils.PageUtils;
import com.bj.utils.Query;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bj.product.dao.CategoryDao;
import com.bj.product.entity.CategoryEntity;
import com.bj.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
/**
 * 商品三级分类
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 * @Description 此处值得注意的是三级分类数据更新或者改动之后数据不一致的问题
 * 此处提供两个解决方案和三个思路：方案：1.缓存db双写模式 2.缓存失效模式
 * 上述两个方案都涉及到多个操作，而且是非原子性的，以此必然存在脏数据问题
 * 所以提供三个思路：1.非强一致性业务设置过期时间即可
 *                2.强一致性业务采用分布式读写锁
 *                3.采用阿里的canas监听数据库数据变化同步到redis（推荐）
 * 此处由于不要求强一致性，读多写少直接采用springCache
 */

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> getTreeCategorys() {
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper();
        List<CategoryEntity> categoryEntities = baseMapper.selectList(wrapper);
        List<CategoryEntity> categoryEntityList = categoryEntities.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == 0;
        }).map(categoryEntity ->
                {
                    categoryEntity.setChildren(getChildren(categoryEntity, categoryEntities));
                    return categoryEntity;
                }
        ).sorted((cat1, cat2) -> {
            return (cat1.getSort() == null ? 0 : cat1.getSort()) - (cat2.getSort() == null ? 0 : cat2.getSort());
        }).collect(Collectors.toList());


        return categoryEntityList;
    }
    //保证缓存一致性，使该数据改动所有该缓存分区下的缓存失效
    @CacheEvict(value = "category",allEntries = true)
    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO: 2020/8/16 新加业务时需要注意是否引用菜单，被引用的菜单不能删除
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public List<Long> getCatelogPath(Long catId) {
        CategoryEntity categoryEntity = baseMapper.selectById(catId);
        List<Long> catIdPath = new ArrayList<>();
        Long parentCid = categoryEntity.getParentCid();
        catIdPath.add(categoryEntity.getCatId());
        while (parentCid != 0l) {
            catIdPath.add(parentCid);
            CategoryEntity categoryPar = baseMapper.selectById(parentCid);
            Long parentCid1 = categoryPar.getParentCid();
            parentCid = parentCid1;
        }
        if (!CollectionUtils.isEmpty(catIdPath)) {
            Collections.reverse(catIdPath);
        }
        return catIdPath;
    }

    //保证缓存一致性，使该数据改动所有该缓存分区下的缓存失效
    @CacheEvict(value = "category",allEntries = true)
    @Override
    @Transactional
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setCatelogName(category.getName());
        categoryBrandRelationService.update(relationEntity,
                new QueryWrapper<CategoryBrandRelationEntity>()
                        .eq("catelog_id", category.getCatId()));
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


    //对缓存数据加锁，防止缓存击穿，并且加锁的同时要判断Redis里是否有数据办证单机环境下只查一次数据库
    // TODO: 2020/10/8 负载均衡机器多情况下建议分布式锁
    private synchronized Map<String, List<Catelog2Vo>> getCatLogToRedis() {
        String catlogJson = redisTemplate.opsForValue().get("catlogJson");
        if(!StringUtils.isEmpty(catlogJson)){
            Map<String, List<Catelog2Vo>> stringListMap = JSONObject.parseObject(catlogJson, Map.class);
            return stringListMap;
        }
        Map<String, List<Catelog2Vo>> catLogFromDb = getCatLogFromDb();
        //空数据也放入缓存，防止缓存穿透
        String s = JSONObject.toJSONString(catLogFromDb);
        //不设置过期时间，防止缓存雪崩，对于需要设置过期时间的数据可以加上随机时间防止雪崩
        redisTemplate.opsForValue().set("catlogJson",s);
        return catLogFromDb;
    }

    //强一致性要求高的话，参考下边分布式锁
    private  Map<String, List<Catelog2Vo>> getCatLogToRedison() {
        RLock lock = redissonClient.getLock("catlogJson-lock");
        lock.lock();
        Map<String, List<Catelog2Vo>> catLogFromDb;
        try {
            String catlogJson = redisTemplate.opsForValue().get("catlogJson");
            if(!StringUtils.isEmpty(catlogJson)){
                Map<String, List<Catelog2Vo>> stringListMap = JSONObject.parseObject(catlogJson, Map.class);
                return stringListMap;
            }
            catLogFromDb = getCatLogFromDb();
            //空数据也放入缓存，防止缓存穿透
            String s = JSONObject.toJSONString(catLogFromDb);
            //不设置过期时间，防止缓存雪崩，对于需要设置过期时间的数据可以加上随机时间防止雪崩
            redisTemplate.opsForValue().set("catlogJson",s);
        } finally {
            lock.unlock();
        }
        return catLogFromDb;
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
}