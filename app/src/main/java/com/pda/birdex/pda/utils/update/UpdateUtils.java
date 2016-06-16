package com.pda.birdex.pda.utils.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;


import com.pda.birdex.pda.utils.Constant;
import com.pda.birdex.pda.utils.T;

import java.io.File;

/**
 * Created by weiyu.wei on 2015/11/27.
 */
public class UpdateUtils {
    public static String getVersionName(Context context) {
        String appVersion = "";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersion = info.versionName;   //版本名
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return appVersion;
    }

    public static double getVersionCode(Context context) {
        int appVersionCode = -1;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersionCode = info.versionCode;   //版本名
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return appVersionCode;
    }

    public static void installAPK(Context context, Uri apk) {
        File f = new File(Constant.BASEPATH,UpdateManager.getInstance().getFilePath());
        if (f.length() > 0) {
            // 通过Intent安装APK文件
            Intent intents = new Intent();
            intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intents.setAction(Intent.ACTION_VIEW);
//        intents.setAction("android.intent.action.VIEW");
//        intents.addCategory("android.intent.category.DEFAULT");
//        intents.setType("application/vnd.android.package-archive");
//        intents.setData(apk);
            intents.setDataAndType(apk, "application/vnd.android.package-archive");
//        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        android.os.Process.killProcess(android.os.Process.myPid());
            // 如果不加上这句的话在apk安装完成之后点击单开会崩溃

            context.startActivity(intents);
        } else {
            T.showShort(context, "更新失败");
        }
    }

    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    //获取当前网络是wife还是3G还是没有网络
    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }
}
