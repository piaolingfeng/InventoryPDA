package com.pda.birdex.pda.response;

import com.pda.birdex.pda.entity.OperationLog;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.entity.TakingOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/6/27.
 */
public class TakingOrderNoInfoEntity extends BaseEntity {
    BaseInfo1 detail = new BaseInfo1();//":

    public BaseInfo1 getDetail() {
        return detail;
    }

    public class BaseInfo1 {
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
}
