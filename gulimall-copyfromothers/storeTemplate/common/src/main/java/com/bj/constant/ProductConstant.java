package com.bj.constant;

public enum ProductConstant {
    ATTR_TYPE_BASE(1,"base"),
    ATTR_TYPE_SALE(0,"sale"),
    SPU_NEW(0,"新建"),
    SPU_UP(1,"上架"),
    SPU_DOWN(2,"下架");
    private int code;
    private String msg;

    ProductConstant(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
