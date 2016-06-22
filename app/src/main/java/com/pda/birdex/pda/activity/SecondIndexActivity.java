package com.pda.birdex.pda.activity;


import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.IndexAdapter;
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
    List<String> indexList = new ArrayList<>();

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
        for(int i =0;i<lists.length;i++){
            indexList.add(lists[i]);
        }
        adapter = new IndexAdapter(this,indexList);
        adapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("name", indexList.get(position));
                switch (position) {
                    case 0:
                        if(getString(R.string.taking).equals(titleStr)) {
                            intent.setClass(SecondIndexActivity.this, TakingActivity.class);
                            startActivity(intent);
                        } else if(getString(R.string.count).equals(titleStr)) {

                        }
                        break;
                    case 1:
                        if(getString(R.string.taking).equals(titleStr)) {
                            // 拍照
                            intent.setClass(SecondIndexActivity.this, PhotoActivity.class);
                            startActivity(intent);
                        } else if(getString(R.string.count).equals(titleStr)) {
                            // 绑定区域
                            intent.setClass(SecondIndexActivity.this, CountBindActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        if(getString(R.string.taking).equals(titleStr)) {
                            //打印揽收单
                            intent.setClass(SecondIndexActivity.this, TakingPrintActivity.class);
                            startActivity(intent);
                        } else if(getString(R.string.count).equals(titleStr)) {
                            // 打印清点单
                            intent.setClass(SecondIndexActivity.this, CountPrintActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        if(getString(R.string.taking).equals(titleStr)) {
                            // 绑定区域
                            intent.setClass(SecondIndexActivity.this, TakingBindActivity.class);
                            startActivity(intent);
                        } else if(getString(R.string.count).equals(titleStr)) {
                            // 追踪箱号
                            intent.setClass(SecondIndexActivity.this, CountTrackActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
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
                adapter.setBvValue("88");
            }
        } else {
            adapter.setBvValue("");
        }
        rcy.setLayoutManager(myGLManager);
        rcy.setAdapter(adapter);
    }
}
