package com.storetemplate.auth.com.bj.vo;

import lombok.Data;

@Data
public class SocialUser {

    private String accessToken;

    private String remindIn;

    private int expiresIn;

    private String uid;

    private String isrealname;
}