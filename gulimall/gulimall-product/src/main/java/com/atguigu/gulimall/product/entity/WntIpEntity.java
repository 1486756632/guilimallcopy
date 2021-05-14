package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("wnt_ip")
public class WntIpEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id序号
     */
    private Integer id;
    /**
     *风机名
     */
    private String wntName;
    /**
     *风机名ip
     */
    private String wntIp;

}
