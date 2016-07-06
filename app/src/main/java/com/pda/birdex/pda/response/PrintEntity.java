package com.pda.birdex.pda.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/7/1.
 */
public class PrintEntity extends BaseEntity {
    List<String> data = new ArrayList<>();
    List<String> containerNos = new ArrayList<>();
    String tid = "";//只在打印相同箱号时返回
    String orderNo="";//
//    String
    public List<String> getData() {
        return data;
    }

    public List<String> getContainerNos() {
        return containerNos;
    }

    public String getTid() {
        return tid;
    }

    public String getOrderNo() {
        return orderNo;
    }
}
