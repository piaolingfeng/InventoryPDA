package com.pda.birdex.pda.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/6/29.
 */
public class TakingOrderDetail implements Serializable{
    TakingOrder baseInfo = new TakingOrder();
    List<ContainerInfo> containerList = new ArrayList<>();
    List<OperationLog> operationLog = new ArrayList<>();

    public TakingOrder getBaseInfo() {
        return baseInfo;
    }

    public List<ContainerInfo> getContainerList() {
        return containerList;
    }

    public List<OperationLog> getOperationLog() {
        return operationLog;
    }

    public void setOperationLog(List<OperationLog> operationLog) {
        this.operationLog = operationLog;
    }

}