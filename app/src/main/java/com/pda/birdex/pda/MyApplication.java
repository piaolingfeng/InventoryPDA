package com.pda.birdex.pda;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.loopj.android.http.AsyncHttpClient;
import com.pda.birdex.pda.response.MerchantEntity;
import com.pda.birdex.pda.utils.Constant;
import com.pda.birdex.pda.utils.CrashHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/3/18.
 */
public class MyApplication extends Application {
    private static MyApplication instants;
    //获取包名
    private static final String TAG = MyApplication.class.getName();


    public static AsyncHttpClient ahc;
    public static String appName = "库内PDA";
    // 推送设备号
    public static String device_token;
    // app 版本号
    public static String app_version = "";
    // 设备信息
//    public static String device_info;
    // 设备类型 这里写死为 android
    public static String device_type = "android";
    private SharedPreferences sp;
    //sp保存Umeng
    public static String SP_Umeng = "umeng_token";
    public static List<Activity> activityList = new ArrayList<>();

    // 登录的相关信息 user
//    public static User user;
    public static MerchantEntity merchantList;//所有商家

    // 清除 activity 栈
    public void clearActivities() {
        for (Activity activity : activityList) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

    // 获取当前版本号
    private String getVersionLocal() {
        String versionLocal = "";
        try {
            // 取得当前版本号
            PackageManager manager = MyApplication.getInstans().getPackageManager();
            PackageInfo info = null;
            info = manager.getPackageInfo(MyApplication.getInstans().getPackageName(), 0);
            versionLocal = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionLocal;
    }

    // 获取设备信息
//    private String getDevice_info() {
//        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        StringBuilder sb = new StringBuilder();
//        sb.append("\nDeviceModel = " + android.os.Build.MODEL);
//        sb.append("\nDeviceVERSION_RELEASE = " + android.os.Build.VERSION.RELEASE);
//        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
//        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
//        sb.append("\nLine1Number = " + tm.getLine1Number());
//        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
//        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
//        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
//        sb.append("\nNetworkType = " + tm.getNetworkType());
//        sb.append("\nPhoneType = " + tm.getPhoneType());
//        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
//        sb.append("\nSimOperator = " + tm.getSimOperator());
//        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
//        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
//        sb.append("\nSimState = " + tm.getSimState());
//        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
//        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
//        Log.e("info", sb.toString());
//        return sb.toString();
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        instants = this;
        sp = getSharedPreferences("login", Activity.MODE_PRIVATE);
        initFile();
        iniCrash();
        app_version = getVersionLocal();
        initAsyncHttpClient();
    }


    private void initFile() {
        File file = new File(Constant.BASEPATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 异常扑捉器
     */
    private void iniCrash() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

    }

    public static MyApplication getInstans() {
        if (instants == null) {
            instants = new MyApplication();
        }
        return instants;
    }

    //线程同步网络请求
    private synchronized void initAsyncHttpClient() {
        ahc = new AsyncHttpClient();//获取网络连接超时
        ahc.setTimeout(8 * 1000);//设置30秒超时
        ahc.setConnectTimeout(4 * 1000);//设置30秒超时
        ahc.setResponseTimeout(8 * 1000);
        ahc.setMaxConnections(5);
        ahc.addHeader("DEVICE-TOKEN", device_token);
        ahc.addHeader("APP-VERSION", app_version);
//        ahc.addHeader("Content-Type", "application/json");
    }

}
