package com.bj.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum WareConstant {
    //状态[0新建，1已分配，2正在采购，3已完成，4采购失败]
    CREATE(0,"新建"),
    ASSIGNED(1,"已分配"),
    ORDERING(2,"正在采购/已领取"),//采购需求单/采购单
    FINISHED(3,"已完成"),
    ERROR(4,"采购失败/有异常");//采购需求单/采购单
    private int code;
    private String msg;

}
