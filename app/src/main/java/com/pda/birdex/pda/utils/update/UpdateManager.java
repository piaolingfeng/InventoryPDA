package com.pda.birdex.pda.utils.update;

import android.content.Context;

/**
 * Created by weiyu.wei on 2015/11/27.
 */
public class UpdateManager {

    private static UpdateManager updateManager;
    private String filePath = "/gtapad.apk";//文件保存路径
    private String downLoadPath;//下载的路径


    private UpdateManager() {

    }

    public static UpdateManager getInstance() {
        if (updateManager == null) {
            synchronized (UpdateManager.class) {
                if (updateManager == null) {
                    updateManager = new UpdateManager();
                }
            }
        }
        return updateManager;
    }

    public void set(Context context,String description) {
        CheckUpdate.check(context,description);
    }

    public String getFilePath() {
        String filepath = filePath;
        if (downLoadPath != null) {
            filepath = downLoadPath.substring(downLoadPath.lastIndexOf("/"), downLoadPath.length());
        }
        return  filepath;
    }

    public void setDownLoadPath(String downLoadPath) {
        this.downLoadPath = downLoadPath;
    }

    public String getDownLoadPath() {
        return downLoadPath;
    }


}
