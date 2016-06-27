package com.pda.birdex.pda.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.IndexAdapter;
import com.pda.birdex.pda.entity.CommonItemEntity;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/17.
 */
public class SecondIndexActivity extends BaseActivity {
    String tag = "SecondIndexActivity";
    @Bind(R.id.rcy)
    RecyclerView rcy;
    @Bind(R.id.title)
    TitleView title;
    IndexAdapter adapter;
    String []lists={""};
    List<CommonItemEntity> indexList = new ArrayList<>();

    // 传过来的 title
    private String titleStr;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initializeContentViews() {
        if(getIntent().getExtras()!=null) {
            titleStr = getIntent().getStringExtra("name");
            lists = getIntent().getStringArrayExtra("list");
            if(lists==null){
                lists=new String[]{};
            }
        }
        title.setTitle(titleStr);
        for (int i = 0; i < lists.length; i++) {
            CommonItemEntity entity = new CommonItemEntity();
            entity.setName(lists[i]);
            entity.setCount("1");
            indexList.add(entity);
        }
        adapter = new IndexAdapter(this,indexList);
        adapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("HeadName", titleStr);
                //揽收
                if(getString(R.string.taking).equals(titleStr)){
                    switch (position){
                        case 0:
                            intent.setClass(SecondIndexActivity.this, TakingActivity.class);
                            break;
                        case 1:
                            intent.setClass(SecondIndexActivity.this,CountMissionActivity.class);//揽收任务，跟清点任务页面相同
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
                            intent.setClass(SecondIndexActivity.this, TakingPrintActivity.class);
                            break;
                        case 4:
                            intent.setClass(SecondIndexActivity.this, TakingBindActivity.class);
                            break;
                    }
                }
                //清点
                if(getString(R.string.count).equals(titleStr)){
                    switch (position){
                        case 0:
                            intent.setClass(SecondIndexActivity.this,CountMissionActivity.class);
                            break;
                        case 1:
                            intent.setClass(SecondIndexActivity.this, CountBindActivity.class);
                            break;
                        case 2:
                            Bundle b = new Bundle();
                            b.putString("title",getString(R.string.count_print_no));
                            b.putString("inputname",getString(R.string.count_box_no));
                            intent.putExtras(b);
                            // 打印清点单
                            intent.setClass(SecondIndexActivity.this, TakingPrintActivity.class);
                            startActivity(intent);
                            break;
                        case 3:
                            intent.setClass(SecondIndexActivity.this, TakingBindActivity.class);
                            break;
                        case 4:
                            intent.setClass(SecondIndexActivity.this, CountPhotoActivity.class);
                            break;
                    }
                }
                startActivity(intent);
            }
        });
        GridLayoutManager myGLManager = new GridLayoutManager(this,2);
        // 如果是清点任务，第一行 需要跨列 2行
        if(lists.length > 0) {
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
}
