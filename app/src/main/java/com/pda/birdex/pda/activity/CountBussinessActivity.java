package com.pda.birdex.pda.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.CountMissionClearAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.widget.TitleView;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/22.
 */
public class CountBussinessActivity extends BaseActivity {
    String tag = "CountBussinessActivity";

    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.xrcy)
    XRecyclerView xrcy;
    @Bind(R.id.tv_business)
    TextView tv_business;
    @Bind(R.id.tv_count_mission)
    TextView tv_count_mission;
    @Bind(R.id.tv_name_count_mission)
    TextView tv_name_count_mission;
    @Bind(R.id.tv_clear_num)
    TextView tv_clear_num;
    CountMissionClearAdapter adapter;

    String bussinessCode = "";
    String HeadName = "";

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_countbussiness_layout;
    }

    @Override
    public void initializeContentViews() {
        title.setTitle(getString(R.string.count_task));
        if (getIntent().getExtras() != null) {
            bussinessCode = getIntent().getStringExtra("bussinessCode");
            HeadName = getIntent().getStringExtra("HeadName");
        }
        if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收清点
            tv_name_count_mission.setText(getResources().getString(R.string.tv_taking_mission));
            tv_clear_num.setText(getResources().getString(R.string.tv_taking_num));
        }
        xrcy.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        xrcy.setLoadingMoreEnabled(true);
        xrcy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
//xrcy.loadMoreComplete();
            }
        });
        xrcy.setPullRefreshEnabled(false);
        xrcy.setLayoutManager(new LinearLayoutManager(this));
//        View view = LayoutInflater.from(this).inflate(R.layout.item_countbussiness_layout,null);
//        xrcy.addHeaderView(view);
        adapter = new CountMissionClearAdapter(this);
        adapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(CountBussinessActivity.this, CountMissionClearNumActivity.class);
                intent.putExtra("clear_num", "");
                intent.putExtra("HeadName",HeadName);
                startActivity(intent);
            }
        });
        xrcy.setAdapter(adapter);
    }

    //通过网络请求获取商家待清点任务列表
    private void getBussinessMission() {
        if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收清点

        } else {//清点

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
}
