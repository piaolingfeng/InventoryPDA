package com.pda.birdex.pda.entity;

import java.io.Serializable;

/**
 * Created by chuming.zhuang on 2016/6/28.
 * 商家
 */
    public class  Merchant implements Serializable{
        int  count = 0;//数量",
        String merchantId="";// (string, optional): 商家编号 ,
        String merchantName="";//这个是通过商家列表拼凑过来的，需要手动配置
        public int getCount() {
            return count;
        }

    public String getMerchantId() {
        return merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}