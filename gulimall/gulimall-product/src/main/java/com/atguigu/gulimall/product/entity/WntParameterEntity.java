package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * spu信息
 * 
 * @author martin_yang
 * @date 2021-3-31 21:08:49
 */
@Data
@TableName("wnt_parameter")
public class WntParameterEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 一级目录id
	 */
	private Integer firstNodeId;
	/**
	 * 一级目录名称
	 */
	private String firstNode;
	/**
	 * 商品描述
	 */
	private Integer secondNodeId;
	/**
	 * 所属分类id
	 */
	private String secondNode;
	/**
	 * 品牌id
	 */
	private Integer thirdNodeId;
	/**
	 * 
	 */
	private String thirdNode;
	/**
	 *
	 */
	private String parameterName;

}
