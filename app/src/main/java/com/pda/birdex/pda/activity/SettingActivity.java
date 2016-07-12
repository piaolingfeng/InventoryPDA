package com.pda.birdex.pda.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.IndexAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.response.CommonItemEntity;
import com.pda.birdex.pda.utils.PreferenceUtils;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/7/1.
 */
public class SettingActivity extends BasePrintBarScanActivity {
    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.rcy)
    RecyclerView rcy;
    IndexAdapter adapter;
    String[] lists ;
    List<CommonItemEntity> indexList = new ArrayList<>();
    @Override
    public int getPrintContentLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void printInitializeContentViews() {
        title.setTitle(getString(R.string.setting));
        lists = getResources().getStringArray(R.array.setting_list);
        for (int i = 0; i < lists.length; i++) {
            CommonItemEntity entity = new CommonItemEntity();
            entity.setName(lists[i]);
            entity.setCount("");
            indexList.add(entity);
        }
        indexList.get(2).setName(indexList.get(2).getName()+getAppVersionName(this));
        adapter = new IndexAdapter(this,indexList);
        adapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position){
                    case 0:
                        Intent serverIntent = new Intent(SettingActivity.this, DeviceListActivity.class);
                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                        break;
                    case 1:
                        for (Activity activity:MyApplication.activityList){
                            activity.finish();
                        }
                        Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                        startActivity(intent);
                        BirdApi.asyncHttpClient = null;
                        BirdApi.ahc = null;
                        PreferenceUtils.setPrefString(SettingActivity.this, "token", "");
                        SettingActivity.this.finish();
                        break;
                }
            }
        });
        rcy.setLayoutManager(new GridLayoutManager(this, 2));
        rcy.setAdapter(adapter);
    }

    @Override
    public TitleView printTitleView() {
        return title;
    }

    @Override
    public ClearEditText getClearEditText() {
        return null;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
//            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}
