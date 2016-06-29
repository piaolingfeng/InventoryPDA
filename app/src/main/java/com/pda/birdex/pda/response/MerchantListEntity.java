package com.pda.birdex.pda.response;

import com.pda.birdex.pda.entity.Merchant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/6/27.
 */
public class MerchantListEntity extends BaseEntity implements Serializable{
    int count= 0;
    List<Merchant> merchantCounts = new ArrayList<>();//": [

    public int getCount() {
        return count;
    }

    public List<Merchant> getMerchantCounts() {
        return merchantCounts;
    }

}
