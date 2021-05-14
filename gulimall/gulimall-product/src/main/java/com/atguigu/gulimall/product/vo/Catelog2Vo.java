package com.atguigu.gulimall.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName Catelog2Vo
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/6 14:54
 * @Version 1.0
 **/
@Data
public class Catelog2Vo {
    private Long catalog1Id;
    private List<Catelog3Vo> catalog3List;
    private Long id;
    private String name;
@Data
    public static class Catelog3Vo {
        private Long catalog2Id;
        private Long id;
        private String name;
    }
}
