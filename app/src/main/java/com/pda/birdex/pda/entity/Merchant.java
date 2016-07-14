package com.pda.birdex.pda.entity;

import java.io.Serializable;

/**
 * Created by chuming.zhuang on 2016/6/28.
 * 商家
 */
public class Merchant implements Serializable {
    int count = 0;//数量",///taking/list/count/{merchant}/{listType}接口返回参数时使用
    String merchantId = "";// (string, optional): 商家编号 ,
    String merchantName = "";//这个是通过商家列表拼凑过来的，需要手动配置，2.在获取商家列表时能拿到
    int showCo = 1;//参数，为0不显示用户编码，发1显示用户编码,在获取商家列表时能拿到

    String merchantFirtSpell = "";//简拼
    String merchantFullSpell = "";//全拼，提供搜索使用

    public String getMerchantFirtSpell() {
        return merchantFirtSpell;
    }

    public void setMerchantFirtSpell(String merchantFirtSpell) {
        this.merchantFirtSpell = merchantFirtSpell;
    }

    public String getMerchantFullSpell() {
        return merchantFullSpell;
    }

    public void setMerchantFullSpell(String merchantFullSpell) {
        this.merchantFullSpell = merchantFullSpell;
    }

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

    public int getShowCo() {
        return showCo;
    }

    public void setShowCo(int showCo) {
        this.showCo = showCo;
    }
}