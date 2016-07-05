package com.pda.birdex.pda.entity;

import java.io.Serializable;

/**
 * Created by chuming.zhuang on 2016/7/5.
 */
public class CountingBaseInfo implements Serializable{
    String orderNo = "";// (string, optional): 清点单号 ,
    String tid = "";// (string, optional): 任务号 ,
    int status = 0;//(string, optional): 状态 ,
    String comment = "";//(string, optional): 备注 ,
    String createTime = "";//(string, optional): 创建时间
    String deadline = "";//截止事件
    String diffTime = "";

    public String getOrderNo() {
        return orderNo;
    }

    public String getTid() {
        return tid;
    }

    public int getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getDiffTime() {
        return diffTime;
    }

    public String getDeadline() {
        return deadline;
    }
}
