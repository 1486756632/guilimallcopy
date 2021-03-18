package com.bj.product.vo;

import com.bj.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @ClassName AttrRspVo
 * @Description TODO
 * @Author 13011
 * @Date 2020/9/6 19:11
 * @Version 1.0
 **/
@Data
public class AttrRspVo extends AttrEntity {
    private Long attrGroupId;
    /**
     * 分组属性关联表id
    * */
    private Long relationId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 所属商品分类路径
     */
    //商品分类名
    private String catName;

    private List<Long> catelogPath;
}
