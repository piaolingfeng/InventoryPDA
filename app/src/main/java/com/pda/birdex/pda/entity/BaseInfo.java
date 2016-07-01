package com.pda.birdex.pda.entity;

import java.io.Serializable;

/**
 * Created by chuming.zhuang on 2016/6/27.
 */
public class BaseInfo implements Serializable{
    String takingOrderNo = "";//": "string",揽收单号
    String tid="";// (string, optional): 任务号 ,
    String expressNo="";// (string, optional): 物流号 ,
    String carrier ="";//(string, optional): 物流公司 ,
    String takingType = "";//": "string",揽收类型
    boolean isExist = false;//": true,是否已预报
    int takingStatus = 0;//"揽收状态 assigned/unassigned/done ,: "string",description:[0:OPEN, 1:ASSIGN未开始, 2:EXECUTE进行中, 3:EXCPTION, 4::CLOSE已完成]
    String staff = "";//": "string",揽收员 ,
    String combine = "";//": true,是否绑定区域 ,
    String comment = "";//": "string",备注
    String createTime = "";//": "2016-06-27T03:59:46.160Z",揽收单创建时间 ,
    String takingTime = "";//": "2016-06-27T03:59:46.160Z",揽收时间 ,
    String deadline ="";//截止时间
    String diffTime = "";//": "string"时间间隔

    public String getDeadLine() {
        return deadline ;
    }

    public String getTakingOrderNo() {
        return takingOrderNo;
    }

    public String getTakingType() {
        return takingType;
    }

    public boolean isExist() {
        return isExist;
    }

    public int getTakingStatus() {
        return takingStatus;
    }

    public String getStaff() {
        return staff;
    }

    public String getCombine() {
        return combine;
    }

    public String getComment() {
        return comment;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getTakingTime() {
        return takingTime;
    }

    public String getDiffTime() {
        return diffTime;
    }

    public String getTid() {
        return tid;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public String getCarrier() {
        return carrier;
    }
}
