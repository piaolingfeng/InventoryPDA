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
 * Created by chuming.zhuang on 2016/6/15.
 */
public class MainActivity extends BaseActivity {
    String tag = "MainActivity";
    @Bind(R.id.rcy)
    RecyclerView rcy;
    @Bind(R.id.title)
    TitleView title;
    IndexAdapter adapter;
    String[] lists ;
    String[] takinglists;//收货
    String[] countToLists ;//清点

    List<CommonItemEntity> indexList = new ArrayList<>();

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initializeContentViews() {
        lists=getResources().getStringArray(R.array.index_list);
        takinglists = getResources().getStringArray(R.array.taking_list);
        countToLists = getResources().getStringArray(R.array.count_list);
        title.setBackIvVisble(false);
        title.setTitle("首页");
        for (int i = 0; i < lists.length; i++) {
            CommonItemEntity entity = new CommonItemEntity();
            entity.setName(lists[i]);
            entity.setCount("99");
            indexList.add(entity);
        }
        adapter = new IndexAdapter(this, indexList);
        adapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, SecondIndexActivity.class);
                Bundle b = new Bundle();
                switch (position) {
                    case 0:
                        b.putStringArray("list", takinglists);
                        break;
                    case 1:// 清点
                        b.putStringArray("list", countToLists);
                        break;
                    case 2:
                        break;
                    case 3:


                        break;
                }
                b.putString("name", indexList.get(position).getName());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        rcy.setLayoutManager(new GridLayoutManager(this, 2));
        rcy.setAdapter(adapter);
    }
}
