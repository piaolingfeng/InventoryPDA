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
    List<UpcData> upcData = new ArrayList<>();
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

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPhotoUrl(List<String> photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<UpcData> getUpcData() {
        return upcData;
    }
}
