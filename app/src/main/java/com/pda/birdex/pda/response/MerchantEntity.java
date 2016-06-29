package com.pda.birdex.pda.response;

import com.pda.birdex.pda.entity.Merchant;

import java.util.List;

/**
 * Created by chuming.zhuang on 2016/6/27.
 * 所有商家
 */
public class MerchantEntity extends BaseEntity {
    List<Merchant> list;

    public List<Merchant> getList() {
        return list;
    }
}
