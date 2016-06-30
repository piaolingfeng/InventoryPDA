package com.pda.birdex.pda.entity;

import java.io.Serializable;

/**
 * Created by chuming.zhuang on 2016/6/27.
 */
public class OperationLog implements Serializable{
    String opterateTime = "";//": "2016-06-27T03:59:46.161Z",操作时间
    String operation = "";//": "string",动作
    String operator = "";//": "string",操作员
    String comment = "";//": "string"备注

    public String getOpterateTime() {
        return opterateTime;
    }

    public String getOperation() {
        return operation;
    }

    public String getOperator() {
        return operator;
    }

    public String getComment() {
        return comment;
    }
}