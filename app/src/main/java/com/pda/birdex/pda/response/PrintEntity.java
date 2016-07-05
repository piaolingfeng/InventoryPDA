package com.pda.birdex.pda.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/7/1.
 */
public class PrintEntity extends BaseEntity {
    List<String> data = new ArrayList<>();
    String tid = "";//只在打印相同箱号时返回
//    String
    public List<String> getData() {
        return data;
    }
}
