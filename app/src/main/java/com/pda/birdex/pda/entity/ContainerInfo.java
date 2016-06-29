package com.pda.birdex.pda.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/6/27.
 */
public class ContainerInfo {
    String containerId = "";//": "string"箱号 ,
    String status = "";//": "string",状态 ,
    String area = "";//": "string",
    int count=0;// (integer, optional): 箱号 ,
    List<String> photoUrl = new ArrayList<>();//": [

    public String getContainerId() {
        return containerId;
    }

    public String getStatus() {
        return status;
    }

    public String getArea() {
        return area;
    }

    public List<String> getPhotoUrl() {
        return photoUrl;
    }

    public int getCount() {
        return count;
    }
}
