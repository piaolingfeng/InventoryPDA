package com.pda.birdex.pda.response;

import com.pda.birdex.pda.entity.OperationLog;
import com.pda.birdex.pda.entity.UpcData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/7/11.
 */
public class StockInContainerInfoEntity extends BaseEntity {

    String areaNo ="";//(string, optional): 区域码 ,
    String area ="";//(string, optional): 区域 ,
    String zone ="";//(string, optional): 区域zone ,
    String owner ="";//(string, optional): 容器所有者 ,
    String capacityLevel ="";//(string, optional): 容器物品数量等级,3高,2中,1低 ,
    String orderNo ="";//(string, optional): 单号 ,
    String tid ="";// (string, optional): tid ,
    String link ="";//(string, optional): 连接的容器号 ,
    List<UpcData> upcData = new ArrayList<>();//(Array[UpcData], optional),
    List<OperationLog> operationLog =new ArrayList<>();//(Array[OperationLog], optional),
    List<String> photoUrl =new ArrayList<>();//Array[string], optional): 照片

    public String getAreaNo() {
        return areaNo;
    }

    public String getArea() {
        return area;
    }

    public String getZone() {
        return zone;
    }

    public String getOwner() {
        return owner;
    }

    public String getCapacityLevel() {
        return capacityLevel;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getTid() {
        return tid;
    }

    public String getLink() {
        return link;
    }

    public List<UpcData> getUpcData() {
        return upcData;
    }

    public List<OperationLog> getOperationLog() {
        return operationLog;
    }

    public List<String> getPhotoUrl() {
        return photoUrl;
    }
}
