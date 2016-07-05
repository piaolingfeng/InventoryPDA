package com.pda.birdex.pda.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/7/5.
 */
public class UploadReport {
    //日志上报
//    source (string, optional),上报设备
//    time (integer, optional),操作事件，
//    userId (string, optional),操作人ID
//   "userName": "操作人名称",
//    "taskId": "任务Id",
//    orderId (string, optional),单号
//    logLevel (string, optional),error/warn/info/trace/debug
//    task (string, optional),任务，详见具体上报场景
//    job (string, optional),工序，详见具体上报场景
//    event (string, optional),事件，主要是commit,其他可能值如：begin,pause,resume,commit，cancel，error
//    params (object, optional)事件参数，根据具体情况上报

    String source="";
    int time =0;
    String userId="";
    String userName="";
    String taskId="";
    String orderId="";
    String task="";
    String job="";
    String event="";
    String logLevel="";
    List<?> params = new ArrayList<>();
}
