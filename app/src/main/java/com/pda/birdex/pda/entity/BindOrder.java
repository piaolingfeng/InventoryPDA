package com.pda.birdex.pda.entity;

/**
 * Created by hyj on 2016/7/1.
 */
public class BindOrder {

    // 容器编号
    private String containerNo;
    // 容器属主
    private String owner;

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
