package com.pda.birdex.pda.entity;

/**
 * Created by hyj on 2016/7/1.
 */
public class BindOrder {

    // 容器编号
    private String Code;
    // 容器属主
    private String Owner;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }
}
