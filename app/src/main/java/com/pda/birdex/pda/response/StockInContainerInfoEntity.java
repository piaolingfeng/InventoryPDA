package com.pda.birdex.pda.response;

import com.pda.birdex.pda.entity.OperationLog;
import com.pda.birdex.pda.entity.UpcData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/7/11.
 */
public class StockInContainerInfoEntity extends BaseEntity {
    String oldNo = "";//原容器号，自己增加的字段，为了完成逻辑
    String areaNo ="";//(string, optional): 区域码 ,
    String orderNo ="";//(string, optional): 单号 ,
    List<UpcData> upcData= new ArrayList<>();//Array[UpcData], optional),
    List<OperationLog> operationLog = new ArrayList<> ();//Array[OperationLog], optional)

    public String getAreaNo() {
        return areaNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public List<UpcData> getUpcData() {
        return upcData;
    }

    public List<OperationLog> getOperationLog() {
        return operationLog;
    }

    public String getOldNo() {
        return oldNo;
    }
}
