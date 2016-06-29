package com.pda.birdex.pda.response;

import com.pda.birdex.pda.entity.TakingOrderDetail;

import java.io.Serializable;

/**
 * Created by chuming.zhuang on 2016/6/27.
 */
public class TakingOrderNoInfoEntity extends BaseEntity implements Serializable{
    TakingOrderDetail detail = new TakingOrderDetail();//":

    public TakingOrderDetail getDetail() {
        return detail;
    }


}
