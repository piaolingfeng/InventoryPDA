package com.pda.birdex.pda.utils.update;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;

import com.pda.birdex.pda.utils.Constant;


public class UpdateService extends Service {

    /**
     * 安卓系统下载类
     **/
    DownloadManager manager;
    DownloadCompleteReceiver receiver;

    public UpdateService() {
    }

    /**
     * 初始化下载器
     **/
    private void initDownManager() {

        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        /** 接收下载完的广播 **/

        receiver = new DownloadCompleteReceiver();


        //设置下载地址
        try {


            DownloadManager.Request down = new DownloadManager.Request(
                    Uri.parse(UpdateManager.getInstance().getDownLoadPath()));

            // 设置允许使用的网络类型，这里是移动网络和wifi都可以
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                    | DownloadManager.Request.NETWORK_WIFI);

            // 下载时，通知栏显示途中
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

            // 显示下载界面
            down.setVisibleInDownloadsUi(true);

            // 设置下载后文件存放的位置
            down.setDestinationInExternalPublicDir(Constant.NAME, UpdateManager.getInstance().getFilePath());

            // 将下载请求放入队列
            manager.enqueue(down);
            //注册下载广播
            registerReceiver(receiver, new IntentFilter(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 调用下载
        initDownManager();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {

        // 注销下载广播
        if (receiver != null)
            unregisterReceiver(receiver);

        super.onDestroy();
    }

    // 接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //判断是否下载完成的广播
            if (intent.getAction().equals(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                //获取下载的文件id
                long downId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                //自动安装apk
                UpdateUtils.installAPK(UpdateService.this, manager.getUriForDownloadedFile(downId));

                //停止服务并关闭广播
                UpdateService.this.stopSelf();

            }
        }
    }
}
