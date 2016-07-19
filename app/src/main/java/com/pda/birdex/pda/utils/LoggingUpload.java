package com.pda.birdex.pda.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chuming.zhuang on 2016/7/4.
 * 日志
 */
public class LoggingUpload {
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
    Context mContext;
    private  Map<String, String> infos = new HashMap<String, String>();
    public  final String TAG = "LoggingUpload";
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }
    public void init(Context mContext){
        this.mContext = mContext;
        collectDeviceInfo(mContext);
    }

    private  long getLocalTime(){
        Date date = new Date();
        return date.getTime();
    }

    public RequestCallBackInterface backInterface = new RequestCallBackInterface() {
        @Override
        public void successCallBack(JSONObject object) {
            L.e("Logging_Report_success", object.toString());
        }

        @Override
        public void errorCallBack(JSONObject object) {
            L.e("Logging_Report_error",object.toString());
        }
    };

    //揽收部分上传

    private JSONObject createTakingJson(String orderId, String tid){
        JSONObject object = new JSONObject();
        try {
            object.put("task","take");
            object.put("source","PDA");
            object.put("time",getLocalTime());
            object.put("userId", PreferenceUtils.getPrefString(MyApplication.getInstans(), "username", ""));
            object.put("userName", PreferenceUtils.getPrefString(MyApplication.getInstans(), "user_name", ""));
            object.put("taskId", tid);
            object.put("orderId", orderId);//"匹配的揽收单号; 若无匹配，返回空"
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
    //扫描物流号
//    "trkNo": "物流单号",
//            "matched": true/false   /* 是否匹配到揽收单号 */
    public void takeScan(Context mContext, String tag, String orderId, String tid, String trkNo, boolean match){
        JSONObject object =createTakingJson(orderId,tid);
        try {
            object.put("job", "scan");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            params.put("trkNo",trkNo);//"物流单号"
            params.put("matched",match);
            object.put("params", params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//    打印容器号标签:
//    {
//        ...
//        "job": "tag_print",
//            "logLevel": "trace",
//            "event": "commit",
//            "params": {
//        "ctNos": ["容器号1", "容器号2", ...]
//    }
    public  void takePrint(Context mContext, String tag, String orderId, String tid, List<String> ctNos){
        JSONObject object =createTakingJson(orderId,tid);
        try {
            object.put("job", "tag_print");
            object.put("logLevel", "trace");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            JSONArray array = new JSONArray(ctNos);
            params.put("ctNos",array);//"物流单号"
            object.put("params", params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext,object,backInterface,tag,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //揽收清点
    public  void takeTakeClear(Context mContext, String tag, String orderId, String tid, String ctNo, long count){
        JSONObject object =createTakingJson(orderId,tid);
        try {
            object.put("job", "submit");
            object.put("logLevel", "info");
            object.put("event", "commit");

            JSONObject params = new JSONObject();
            params.put("ctNo",ctNo);//"物流单号"
            params.put("count",count);
            object.put("params", params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext,object,backInterface,tag,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //绑单
    public  void takeBindOrder(Context mContext, String tag, String orderId, String tid, List<String> ctNos){
        JSONObject object =createTakingJson(orderId,tid);
        try {
            object.put("job", "bind_ct");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            JSONArray array = new JSONArray(ctNos);
            params.put("ctNos",array);//""容器号1", "容器号2", ..."
            object.put("params", params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext,object,backInterface,tag,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //绑区
//    ctNo": "容器号",
//            "rgNo": "区域号"
    public void takeBindArea(Context mContext, String tag, String orderId, String tid,String ctNo,String rgNo){
        JSONObject object =createTakingJson(orderId,tid);
        try {
            object.put("job", "bind_rg");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            params.put("ctNo",ctNo);//""容器号1", "容器号2", ..."
            params.put("rgNo",rgNo);
            object.put("params", params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext,object,backInterface,tag,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//    拍照
//    {
//        ...
//        "job": "photo",
//            "logLevel": "info",
//            "event": "commit",
//            "params": {
//        "photoNum": number, /* 提交照片张数 */
//                "tagError": true/false /* 是否标记异常 */
//    }
    public  void takeTakePhoto(Context mContext, String tag, String orderId, String tid, int photoNum, boolean tagError){
        JSONObject object =createTakingJson(orderId,tid);
        try {
            object.put("job", "photo");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            params.put("photoNum",photoNum);//"物流单号""photoNum": number, /* 提交照片张数 */
            params.put("tagError",tagError);//"tagError": true/false /* 是否标记异常 */
            object.put("params", params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext,object,backInterface,tag,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //选择商家
    public  void takeSelectMerchant(Context mContext, String tag, String orderId, String tid, String owner){
        JSONObject object =createTakingJson(orderId,tid);
        try {
            object.put("job", "man_create");
            object.put("logLevel", "info");
            object.put("event", "commit");

            JSONObject params = new JSONObject();
            params.put("owner",owner);//"物流单号""photoNum": number, /* 提交照片张数 */
            object.put("params", params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext,object,backInterface,tag,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     *          清点日志上报
     *
     */
    private JSONObject creatCountJsonObject(String orderId,String tid){
        JSONObject object = new JSONObject();
        try {
            object.put("task","count");
            object.put("source","PDA");
            object.put("time",getLocalTime());
            object.put("userId", PreferenceUtils.getPrefString(MyApplication.getInstans(), "username", ""));
            object.put("userName", PreferenceUtils.getPrefString(MyApplication.getInstans(), "user_name", ""));
            object.put("taskId", tid);
            object.put("orderId", orderId);//"匹配的揽收单号; 若无匹配，返回空"
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    //解绑
    public void countUnBind(Context mContext,String tag,String orderId,String tid,String ctNo){
        JSONObject object = creatCountJsonObject(orderId,tid);
        try {
            object.put("job", "unbind_rg");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            params.put("ctNo",ctNo);
            object.put("params",params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //打印
    public void countPrint(Context mContext,String tag,String orderId,String tid,List<String> ctNos){
        JSONObject object = creatCountJsonObject(orderId,tid);
        try {
            object.put("job", "tag_print");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            JSONArray jsonArray = new JSONArray(ctNos);
            params.put("ctNo",jsonArray);
            object.put("params",params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //绑单
    public void countBindOrder(Context mContext,String tag,String orderId,String tid,List<String> ctNos){
        JSONObject object = creatCountJsonObject(orderId,tid);
        try {
            object.put("job", "bind_ct");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            JSONArray array = new JSONArray(ctNos);
            params.put("ctNos",array);//""容器号1", "容器号2", ..."
            object.put("params", params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //绑区
//    "ctNo": "容器号",
//    "rgNo": "区域号"
    public void countBindArea(Context mContext,String tag,String orderId,String tid,String ctNo,String rgNo){
        JSONObject object = creatCountJsonObject(orderId,tid);
        try {
            object.put("job", "bind_rg");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            params.put("ctNo",ctNo);
            params.put("rgNo",rgNo);
            object.put("params",params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //拍照
//    photoNum": number,   /* 照片数量 */
//            "tagError": true/false,   /* 标记异常 */
//            "upc": "UPC号"
    public void countTakePhoto(Context mContext,String tag,String orderId,String tid,int photoNum,boolean tagError,String upc){
        JSONObject object = creatCountJsonObject(orderId,tid);
        try {
            object.put("job", "photo");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            params.put("photoNum",photoNum);
            params.put("tagError",tagError);
            params.put("upc",upc);
            object.put("params",params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //追踪
//    "sourceCtNo": "原容器号",
//            "destCtNo": "现容器号"
    public void countTrack(Context mContext,String tag,String orderId,String tid,String sourceCtNo,String destCtNo){
        JSONObject object = creatCountJsonObject(orderId,tid);
        try {
            object.put("job", "track");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            params.put("sourceCtNo",sourceCtNo);
            params.put("destCtNo", destCtNo);
            object.put("params",params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //清点提交
//    "upc": "UPC",
//            "ctNo": "容器号",
//            "count": number
    public void countClearCommit(Context mContext,String tag,String orderId,String tid,String upc,String ctNo,long count){
        JSONObject object = creatCountJsonObject(orderId,tid);
        try {
            object.put("job", "submit");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            params.put("upc",upc);
            params.put("ctNo", ctNo);
            params.put("count",count);
            object.put("params",params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     *
     *          入库日志上报
     *
     */
    private JSONObject creatStorageJsonObject(String orderId,String tid){
        JSONObject object = new JSONObject();
        try {
            object.put("task","stockin");
            object.put("source","PDA");
            object.put("time",getLocalTime());
            object.put("userId", PreferenceUtils.getPrefString(MyApplication.getInstans(), "username", ""));
            object.put("userName", PreferenceUtils.getPrefString(MyApplication.getInstans(), "user_name", ""));
            object.put("taskId", tid);
            object.put("orderId", orderId);//"匹配的揽收单号; 若无匹配，返回空"
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

   /* 绑定入库任务
    {
        ...
        "job": "bind_ct",
            "logLevel": "info",
            "event": "commit",
            "params": {
        "ctNo": "容器号"
    }
    }*/
   public void stockInBindOrder(Context mContext,String tag,String orderId,String tid,List<String> ctNos){
       JSONObject object = creatStorageJsonObject(orderId, tid);
       try {
           object.put("job", "bind_ct");
           object.put("logLevel", "info");
           object.put("event", "commit");
           JSONObject params = new JSONObject();
           JSONArray array = new JSONArray(ctNos);
           params.put("ctNos", array);
           object.put("params",params);
           L.e(object.toString());
           BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
       } catch (JSONException e) {
           e.printStackTrace();
       }
   }

   /* 打印容器单
    {
        ...
        "job": "tag_print",
            "logLevel": "info",
            "event": "commit",
            "params": {
        "ctNos": []
    }
    }*/
   public void stockInPrint(Context mContext,String tag,String orderId,String tid,List<String> ctNos){
       JSONObject object = creatStorageJsonObject(orderId, tid);
       try {
           object.put("job", "tag_print");
           object.put("logLevel", "info");
           object.put("event", "commit");
           JSONObject params = new JSONObject();
           JSONArray array = new JSONArray(ctNos);
           params.put("ctNo", array);
           object.put("params",params);
           L.e(object.toString());
           BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
       } catch (JSONException e) {
           e.printStackTrace();
       }
   }

    /*绑定库位
    {
        ...
        "job": "bind_rk",
            "logLevel": "info",
            "event": "commit",
            "params": {
        "ctNo": "",
                "rkNo": "",
                "upc": "",
                "upcCount": ""
    }
    }*/
    public void stockInBindArea(Context mContext,String tag,String orderId,String tid,String ctNo,String upc,String upcCount,String rkNo){
        JSONObject object = creatStorageJsonObject(orderId, tid);
        try {
            object.put("job", "bind_rk");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            params.put("ctNo", ctNo);
            params.put("rkNo", rkNo);
            params.put("upc", upc);
            params.put("upcCount", upcCount);
            object.put("params",params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*拍照
    {
        ...
        "job": "photo",
            "logLevel": "info",
            "event": "commit",
            "params": {
        "ctNo": "",
                "upc": "",
                "photoNum": int
    }
    }*/
    public void stockInPhoto(Context mContext,String tag,String orderId,String tid,String ctNo,String upc,int photoNum){
        JSONObject object = creatStorageJsonObject(orderId, tid);
        try {
            object.put("job", "photo");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            params.put("ctNo", ctNo);
            params.put("upc", upc);
            params.put("photoNum", photoNum);
            object.put("params",params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   /* 追踪
    {
        ...
        "job": "track",
            "logLevel": "info",
            "event": "commit",
            "params": {
        "ctFrom": "",
                "ctTo": "",
                "upc": "",
                "upcCount": ""
    }
    }*/
   public void stockInTrack(Context mContext,String tag,String orderId,String tid,String ctFrom,String ctTo,String upc,long upcCount){
       JSONObject object = creatStorageJsonObject(orderId, tid);
       try {
           object.put("job", "track");
           object.put("logLevel", "info");
           object.put("event", "commit");
           JSONObject params = new JSONObject();
           params.put("ctFrom", ctFrom);
           params.put("ctTo", ctTo);
           params.put("upc", upc);
           object.put("upcCount",upcCount);
           L.e(object.toString());
           BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
       } catch (JSONException e) {
           e.printStackTrace();
       }
   }

   /* 解除绑定
    {
        ...
        "job": "unbind_ct",
            "logLevel": "info",
            "event": "commit",
            "params": {
        "ctNo": ""
    }
    }*/
   public void stockInUnbind(Context mContext,String tag,String orderId,String tid,String ctNo){
       JSONObject object = creatStorageJsonObject(orderId, tid);
       try {
           object.put("job", "unbind_ct");
           object.put("logLevel", "info");
           object.put("event", "commit");
           JSONObject params = new JSONObject();
           params.put("ctNo", ctNo);
           object.put("params",params);
           L.e(object.toString());
           BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
       } catch (JSONException e) {
           e.printStackTrace();
       }
   }

    /*清点
    {
        ...
        "job": "count",
            "logLevel": "info",
            "event": "commit",
            "params": {
        "ctNo": "",
                "upc": "",
                "upcCount": ""
    }
    }*/
    public void stockInClear(Context mContext,String tag,String orderId,String tid,String ctNo,String upc,long upcCount){
        JSONObject object = creatStorageJsonObject(orderId, tid);
        try {
            object.put("job", "count");
            object.put("logLevel", "info");
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            params.put("ctNo", ctNo);
            params.put("upc", upc);
            params.put("upcCount", upcCount);
            object.put("params",params);
            L.e(object.toString());
            BirdApi.jsonPostLoggingRequest(mContext, object, backInterface, tag, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
