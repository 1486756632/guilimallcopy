package com.bj.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.bj.valid.AddGroup;
import com.bj.valid.ListValue;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 品牌
 * 
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 * description:此处的参数校验只限定了新增时的参数校验，实际生产中应根据需要选择校验分组
 */
@Data
@Getter
@Setter
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotEmpty(message = "品牌名不能为空" ,groups = AddGroup.class)
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty(message = "logo地址不合法",groups = AddGroup.class)
	@URL(message = "logo地址不合法",groups = AddGroup.class)
	private String logo;
	/**
	 * 介绍
	 */

	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@ListValue(vals = {0,1},groups = AddGroup.class) //自定义注解限制0或1
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty(message = "检索首字母不能为空",groups = AddGroup.class)
	@Pattern(regexp = "^[a-zA-Z]$",message = "首字母必须为英文字符",groups = AddGroup.class)
	private String firstLetter;
	/**
	 * 排序
	 */
	@Min(value = 0,message = "排序字段必须为整数",groups = AddGroup.class)
	private Integer sort;

}
