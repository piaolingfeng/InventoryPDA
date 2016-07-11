package com.pda.birdex.pda.entity;

import java.io.Serializable;

/**
 * Created by hyj on 2016/7/6.
 */
public class UpcData implements Serializable{
    private String upc;
    private String count;
    private String upcName ="";//(string, optional): upc名称 ,
    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getUpcName() {
        return upcName;
    }
}
