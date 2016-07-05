package com.pda.birdex.pda.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.IndexAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.CommonItemEntity;
import com.pda.birdex.pda.widget.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/17.
 */
public class SecondIndexActivity extends BaseActivity implements OnRecycleViewItemClickListener {
    String tag = "SecondIndexActivity";
    @Bind(R.id.rcy)
    RecyclerView rcy;
    @Bind(R.id.title)
    TitleView title;
    IndexAdapter adapter;
    String[] lists = {""};
    List<CommonItemEntity> indexList = new ArrayList<>();

    // 传过来的 title
    private String titleStr;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initializeContentViews() {
        if (getIntent().getExtras() != null) {
            titleStr = getIntent().getStringExtra("name");
            lists = getIntent().getStringArrayExtra("list");
            if (lists == null) {
                lists = new String[]{};
            }
        }
        title.setTitle(titleStr);
        for (int i = 0; i < lists.length; i++) {
            CommonItemEntity entity = new CommonItemEntity();
            entity.setName(lists[i]);
            entity.setCount("");
            indexList.add(entity);
        }
        if (getString(R.string.taking).equals(titleStr)) {//揽收需要统计揽收任务总数
            getAllTakingMission();
        } else if (getString(R.string.count).equals(titleStr)) {
            getAllCountingMission();
        }
        adapter = new IndexAdapter(this, indexList);
        adapter.setOnRecycleViewItemClickListener(this);
        GridLayoutManager myGLManager = new GridLayoutManager(this, 2);
        // 如果是清点任务，第一行 需要跨列 2行
        if (lists.length > 0) {
            if (getString(R.string.count_task).equals(lists[0])) {
                myGLManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                    @Override
                    public int getSpanSize(int position) {
                        int spanSize = 1;
                        if (position == 0) {
                            spanSize = 2;
                        }
                        return spanSize;
                    }
                });
                // 清点任务  右上角显示任务条数
            }
        }
        rcy.setLayoutManager(myGLManager);
        rcy.setAdapter(adapter);
    }

    //获取所有的揽收任务
    public void getAllTakingMission() {
        BirdApi.getTakingListCountMerchant(this, "null/null", new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                try {
                    indexList.get(1).setCount(object.get("count") + "");
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorCallBack(JSONObject object) {

            }
        }, tag, true);
    }

    //获取所有清点任务
    private void getAllCountingMission() {
        BirdApi.getCountingListCountMerchant(this, "all/unCounting", new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                try {
                    indexList.get(0).setCount(object.get("count") + "");
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorCallBack(JSONObject object) {

            }
        }, tag, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        onItemClick(keyCode - 8);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(int position) {
        if (position >= 0 && position < adapter.getItemCount()) {
            Intent intent = new Intent();
            intent.putExtra("HeadName", titleStr);
            //揽收
            if (getString(R.string.taking).equals(titleStr)) {
                switch (position) {
                    case 0:
                        intent.setClass(SecondIndexActivity.this, TakingScanActivity.class);
                        break;
                    case 1:
                        intent.setClass(SecondIndexActivity.this, CountMissionActivity.class);//揽收任务，跟清点任务页面相同
                        break;
                    case 2:
                        intent.setClass(SecondIndexActivity.this, PhotoActivity.class);
                        break;
                    case 3:
                        Bundle b = new Bundle();
                        b.putString("title", getString(R.string.printlanshou));
                        b.putString("inputname", getString(R.string.lanshouno));
                        intent.putExtras(b);
                        //打印揽收单
                        intent.setClass(SecondIndexActivity.this, TakingPrintBarScanActivity.class);
                        break;
                    case 4:
                        intent.setClass(SecondIndexActivity.this, TakingBindOrderActivity.class);
                        break;
                }
            }
            //清点
            if (getString(R.string.count).equals(titleStr)) {
                switch (position) {
                    case 0:
                        intent.setClass(SecondIndexActivity.this, CountMissionActivity.class);
                        break;
                    case 1:
                        intent.setClass(SecondIndexActivity.this, CountBindActivity.class);
                        break;
                    case 2:
                        Bundle b = new Bundle();
                        b.putString("title", getString(R.string.count_print_no));
                        b.putString("inputname", getString(R.string.count_box_no));
                        intent.putExtras(b);
                        // 打印清点单
                        intent.setClass(SecondIndexActivity.this, TakingPrintBarScanActivity.class);
                        break;
                    case 3:
                        intent.setClass(SecondIndexActivity.this, CountTrackActivity.class);
                        break;
                    case 4:
                        intent.setClass(SecondIndexActivity.this, CountPhotoActivity.class);
                        break;
                }
            }
            startActivity(intent);
        }
    }
}
