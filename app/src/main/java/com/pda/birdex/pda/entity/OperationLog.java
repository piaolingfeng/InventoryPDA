package com.pda.birdex.pda.entity;

import java.io.Serializable;

/**
 * Created by chuming.zhuang on 2016/6/27.
 */
public class OperationLog implements Serializable{
//    String opterateTime = "";//": "2016-06-27T03:59:46.161Z",操作时间
//    String operation = "";//": "string",动作
//    String operator = "";//": "string",操作员
//    String comment = "";//": "string"备注
//    String count ="";//(integer, optional): 数量

    String taskId="";// (string, optional): 任务ID ,
    String time ="";//(string, optional): 操作时间 ,
    String source ="";//(string, optional): 来源 ,
    String logLevel ="";//(string, optional): 级别 ,
    String event ="";//(string, optional): 事件
    String task ="";//(string, optional): 任务 ,
    String userId ="";//(string, optional): 用户id ,
    String job ="";//(string, optional): 动作 ,
    String userName ="";//(string, optional): 用户名 ,
    Object params ="";//(object, optional): 参数 ,
    String orderId ="";//(string, optional): orderId ,
    String _ctime ="";//(string, optional): 日志创建时间

    public String getTaskId() {
        return taskId;
    }

    public String getTime() {
        return time;
    }

    public String getSource() {
        return source;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getEvent() {
        return event;
    }

    public String getTask() {
        return task;
    }

    public String getUserId() {
        return userId;
    }

    public String getJob() {
        return job;
    }

    public String getUserName() {
        return userName;
    }

    public Object getParams() {
        return params;
    }

    public String getOrderId() {
        return orderId;
    }

    public String get_ctime() {
        return _ctime;
    }
}