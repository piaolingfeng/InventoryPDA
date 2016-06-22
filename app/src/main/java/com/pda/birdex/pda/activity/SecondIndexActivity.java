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
    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initializeContentViews() {
        String text = "";
        if(getIntent().getExtras()!=null) {
            text = getIntent().getStringExtra("name");
            lists = getIntent().getStringArrayExtra("list");
            if(lists==null){
                lists=new String[]{};
            }
        }
        title.setTitle(text);
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
                        intent.setClass(SecondIndexActivity.this, TakingActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(SecondIndexActivity.this,PrintActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
        rcy.setLayoutManager(new GridLayoutManager(this,2));
        rcy.setAdapter(adapter);
    }
}
