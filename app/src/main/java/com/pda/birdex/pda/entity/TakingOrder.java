package com.pda.birdex.pda.entity;

/**
 * Created by chuming.zhuang on 2016/6/27.
 */
public class TakingOrder {
    BaseInfo baseInfo = new BaseInfo();
    Person person = new Person();

    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    public Person getPerson() {
        return person;
    }
}
