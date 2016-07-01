package com.pda.birdex.pda.response;

import com.pda.birdex.pda.entity.TakingOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/6/28.
 */
public class MerchantDetailEntity extends BaseEntity {
    List<TakingOrder> takingList = new ArrayList<>();
    int count=0;

    public int getCount() {
        return count;
    }

    public List<TakingOrder> getTakingList() {
        return takingList;
    }
}
