package com.pda.birdex.pda.response;

import com.pda.birdex.pda.entity.CountingOrderDetail;

import java.io.Serializable;

/**
 * Created by chuming.zhuang on 2016/7/6.
 */
public class CountingOrderNoInfoEntity extends BaseEntity implements Serializable {
    CountingOrderDetail detail = new CountingOrderDetail();

    public CountingOrderDetail getDetail() {
        return detail;
    }
}
