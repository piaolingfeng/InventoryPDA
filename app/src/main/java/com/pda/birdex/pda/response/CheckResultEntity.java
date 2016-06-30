package com.pda.birdex.pda.response;

import com.pda.birdex.pda.entity.TakingOrder;

/**
 * Created by chuming.zhuang on 2016/6/29.
 */
public class CheckResultEntity extends BaseEntity {
    boolean isExist = false;
    TakingOrder orderInfo = new TakingOrder();

    public TakingOrder getOrderInfo() {
        return orderInfo;
    }

    public boolean isExist() {
        return isExist;
    }
}
