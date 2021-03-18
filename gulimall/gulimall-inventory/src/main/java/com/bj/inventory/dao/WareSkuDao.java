package com.bj.inventory.dao;

import com.bj.inventory.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品库存
 * 
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:40:11
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void updateBatch(List<WareSkuEntity> updateList);

    Long getSkuStock(Long skuId);

    List<Long> listWareIdHasSkuStock(Long skuId);

    Long lockSkuStock(Long skuId, Long wareId, Integer num);
}
