package com.bj.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.product.entity.SpuInfoEntity;
import com.bj.product.vo.SpuSaveVo;
import com.bj.utils.PageUtils;
import com.bj.utils.R;

import java.util.Map;

/**
 * spu信息
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 21:08:16
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R saveSpu(SpuSaveVo spuSaveVo);

    PageUtils queryPageByCondition(Map<String, Object> params);

    R up(Long spuId);

    SpuInfoEntity getSpuInfoBySkuId(Long skuId);
}

