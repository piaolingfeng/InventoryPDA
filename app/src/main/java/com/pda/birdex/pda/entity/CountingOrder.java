package com.pda.birdex.pda.entity;

import java.io.Serializable;

/**
 * Created by chuming.zhuang on 2016/7/5.
 */
public class CountingOrder implements Serializable{
    CountingBaseInfo baseInfo = new CountingBaseInfo();//(CountingBaseInfo, optional),
    Person person= new Person();// (Person, optional)

    public CountingBaseInfo getBaseInfo() {
        return baseInfo;
    }

    public Person getPerson() {
        return person;
    }
}
