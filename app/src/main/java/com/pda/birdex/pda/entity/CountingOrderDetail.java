package com.pda.birdex.pda.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/7/6.
 */
public class CountingOrderDetail implements Serializable {
    CountingOrder baseInfo = new CountingOrder();
    List<ContainerInfo> containerList = new ArrayList<>();
    List<OperationLog> operationLog = new ArrayList<>();

    public CountingOrder getBaseInfo() {
        return baseInfo;
    }

    public List<ContainerInfo> getContainerList() {
        return containerList;
    }

    public List<OperationLog> getOperationLog() {
        return operationLog;
    }
}
