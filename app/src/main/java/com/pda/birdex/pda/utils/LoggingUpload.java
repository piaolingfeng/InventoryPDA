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
            L.e("Logging_Report_success",object.toString());
        }

        @Override
        public void errorCallBack(JSONObject object) {
            L.e("Logging_Report_error",object.toString());
        }
    };
    //揽收部分上传
    //扫描物流号
    public  void scanUpload(Context mContext,String tag,String orderId,String tid ,String trkNo,boolean match){
        JSONObject object = new JSONObject();
        try {
            object.put("source",infos.toString());
            object.put("time",getLocalTime());
            object.put("userId",PreferenceUtils.getPrefString(MyApplication.getInstans(), "userId", ""));
            object.put("userName",PreferenceUtils.getPrefString(MyApplication.getInstans(), "user_name", ""));

            object.put("taskId",tid);
            object.put("job", "scan");
            object.put("logLevel", "info");
            object.put( "orderId",orderId);//"匹配的揽收单号; 若无匹配，返回空"
            object.put("event", "commit");
            JSONObject params = new JSONObject();
            params.put("trkNo",trkNo);//"物流单号"
            params.put("matched",match);
            object.put("params", params);
            BirdApi.jsonPostRequest(mContext,object,backInterface,BirdApi.Logging_BASE_URL,tag,true);
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
    public  void PrintTag(Context mContext,String tag,String orderId,String tid,List<String> ctNos){
        JSONObject object = new JSONObject();
        try {
            object.put("source",infos.toString());
            object.put("time",getLocalTime());
            object.put("userId",PreferenceUtils.getPrefString(MyApplication.getInstans(), "username", ""));
            object.put("userName",PreferenceUtils.getPrefString(MyApplication.getInstans(), "user_name", ""));

            object.put("taskId",tid);
            object.put("job", "tag_print");
            object.put("logLevel", "trace");
            object.put( "orderId",orderId);//"匹配的揽收单号; 若无匹配，返回空"
            object.put("event", "commit");

            JSONObject params = new JSONObject();
            JSONArray array = new JSONArray(ctNos);
            params.put("ctNos",array);//"物流单号"
            object.put("params", params);
            BirdApi.jsonPostRequest(mContext,object,backInterface,BirdApi.Logging_BASE_URL,tag,true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //揽收清点
    public  void takingClear(Context mContext,String tag,String orderId,String tid,String ctNo,int count){
        JSONObject object = new JSONObject();
        try {
            object.put("source",infos.toString());
            object.put("time",getLocalTime());
            object.put("userId",PreferenceUtils.getPrefString(MyApplication.getInstans(), "username", ""));
            object.put("userName",PreferenceUtils.getPrefString(MyApplication.getInstans(), "user_name", ""));

            object.put("taskId",tid);
            object.put("job", "submit");
            object.put("logLevel", "info");
            object.put( "orderId",orderId);//"匹配的揽收单号; 若无匹配，返回空"
            object.put("event", "commit");

            JSONObject params = new JSONObject();
            params.put("ctNo",ctNo);//"物流单号"
            params.put("count",count);
            object.put("params", params);
            BirdApi.jsonPostRequest(mContext,object,backInterface,BirdApi.Logging_BASE_URL,tag,true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void bindOrder(Context mContext,String tag,String orderId,String tid,List<String> ctNos){
        JSONObject object = new JSONObject();
        try {
            object.put("source",infos.toString());
            object.put("time",getLocalTime());
            object.put("userId",PreferenceUtils.getPrefString(MyApplication.getInstans(), "username", ""));
            object.put("userName",PreferenceUtils.getPrefString(MyApplication.getInstans(), "user_name", ""));

            object.put("taskId",tid);
            object.put("job", "bind_ct");
            object.put("logLevel", "info");
            object.put( "orderId",orderId);//"匹配的揽收单号; 若无匹配，返回空"
            object.put("event", "commit");

            JSONObject params = new JSONObject();
            JSONArray array = new JSONArray(ctNos);
            params.put("ctNos",array);//""容器号1", "容器号2", ..."
            object.put("params", params);
            BirdApi.jsonPostRequest(mContext,object,backInterface,BirdApi.Logging_BASE_URL,tag,true);
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
    public  void takePhoto(Context mContext,String tag,String orderId,String tid,int photoNum,boolean tagError){
        JSONObject object = new JSONObject();
        try {
            object.put("source",infos.toString());
            object.put("time",getLocalTime());
            object.put("userId",PreferenceUtils.getPrefString(MyApplication.getInstans(), "username", ""));
            object.put("userName",PreferenceUtils.getPrefString(MyApplication.getInstans(), "user_name", ""));

            object.put("taskId",tid);
            object.put("job", "photo");
            object.put("logLevel", "info");
            object.put( "orderId",orderId);//"匹配的揽收单号; 若无匹配，返回空"
            object.put("event", "commit");

            JSONObject params = new JSONObject();
            params.put("photoNum",photoNum);//"物流单号""photoNum": number, /* 提交照片张数 */
            params.put("tagError",tagError);//"tagError": true/false /* 是否标记异常 */
            object.put("params", params);
            BirdApi.jsonPostRequest(mContext,object,backInterface,BirdApi.Logging_BASE_URL,tag,true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void selectMerchant(Context mContext,String tag,String orderId,String tid,String owner){
        JSONObject object = new JSONObject();
        try {
            object.put("source",infos.toString());
            object.put("time",getLocalTime());
            object.put("userId",PreferenceUtils.getPrefString(MyApplication.getInstans(), "username", ""));
            object.put("userName",PreferenceUtils.getPrefString(MyApplication.getInstans(), "user_name", ""));

            object.put("taskId",tid);
            object.put("job", "man_create");
            object.put("logLevel", "info");
            object.put( "orderId",orderId);//"匹配的揽收单号; 若无匹配，返回空"
            object.put("event", "commit");

            JSONObject params = new JSONObject();
            params.put("owner",owner);//"物流单号""photoNum": number, /* 提交照片张数 */
            object.put("params", params);
            BirdApi.jsonPostRequest(mContext,object,backInterface,BirdApi.Logging_BASE_URL,tag,true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}
