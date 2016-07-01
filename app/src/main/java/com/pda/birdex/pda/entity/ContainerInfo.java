package com.pda.birdex.pda.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/6/27.
 */
public class ContainerInfo implements Serializable{
    String containerId = "";//": "string"箱号 ,
    String status = "";//": "string",状态 ,
    String owner ="";//(string, optional): 容器属主 ,
    String areaCode ="";//(string, optional): 区域码 ,
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

    public String getOwner() {
        return owner;
    }

    public String getAreaCode() {
        return areaCode;
    }
}
