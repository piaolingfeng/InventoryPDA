package com.pda.birdex.pda.response;

import com.pda.birdex.pda.entity.CountingOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/7/5.
 */
public class CountingListResultEntity extends BaseEntity{
    List<CountingOrder> list =new ArrayList<>();//(Array[Order], optional)
    int count=0;
    public List<CountingOrder> getList() {
        return list;
    }

    public int getCount() {
        return count;
    }
}
