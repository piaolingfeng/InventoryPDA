package com.pda.birdex.pda.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.CountingMissonClearAdapter;
import com.pda.birdex.pda.adapter.TakingMissionClearAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.CountingOrder;
import com.pda.birdex.pda.entity.TakingOrder;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.CountingListResultEntity;
import com.pda.birdex.pda.response.MerchantDetailEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.TitleView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/22.
 */
public class CountMissionMerchantActivity extends BaseActivity {
    String tag = "CountMissionMerchantActivity";

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
    TakingMissionClearAdapter takingAdapter;
    CountingMissonClearAdapter countingAdapter;
    MerchantDetailEntity entity;//商家详情
    List<CountingOrder> countingOrderList = new ArrayList<>();
    List<TakingOrder> takingList = new ArrayList<>();
    String merchantId = "";
    String merchantName = "";
    String HeadName = "";

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_countbussiness_layout;
    }

    @Override
    public void initializeContentViews() {

        if (getIntent().getExtras() != null) {
            merchantId = getIntent().getStringExtra("MerchantId");
            HeadName = getIntent().getStringExtra("HeadName");
            merchantName = getIntent().getStringExtra("merchantName");
        }
        tv_business.setText(merchantName);
        xrcy.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        xrcy.setLoadingMoreEnabled(true);
        xrcy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
//xrcy.loadMoreComplete();
                if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收清点
                    getTakingMerchantMission("null");
                } else {
                    getCountingMerchantMission("null");
                }
            }
        });
        xrcy.setPullRefreshEnabled(false);
        xrcy.setLayoutManager(new LinearLayoutManager(this));

        if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收清点
            title.setTitle(getString(R.string.taking_task));
            tv_name_count_mission.setText(getResources().getString(R.string.tv_taking_mission));
            tv_clear_num.setText(getResources().getString(R.string.tv_taking_num_1));
            takingAdapter = new TakingMissionClearAdapter(this, takingList);
            takingAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(CountMissionMerchantActivity.this, CountMissionClearNumActivity.class);
                    intent.putExtra("baseInfo", takingList.get(position).getBaseInfo());//传递揽收单号
                    intent.putExtra("HeadName", HeadName);
                    startActivity(intent);
                }
            });
            xrcy.setAdapter(takingAdapter);
        } else {
            title.setTitle(getString(R.string.count_task));
            countingAdapter = new CountingMissonClearAdapter(this,countingOrderList);
            countingAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(CountMissionMerchantActivity.this, CountMissionClearNumActivity.class);
                    intent.putExtra("baseInfo", countingOrderList.get(position).getBaseInfo());//传递揽收单号
                    intent.putExtra("HeadName", HeadName);
                    startActivity(intent);
                }
            });
            xrcy.setAdapter(countingAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收清点
            getTakingMerchantMission("null");
        } else {
            getCountingMerchantMission("null");
        }
    }

    //通过网络请求获取商家待清点任务列表
    private void getTakingMerchantMission(String listType) {
        int offset = 0;
        if (takingList != null) {
            offset = takingList.size();
        }
        final int count = 10;//默认5条数据
        BirdApi.getMerchant(this, merchantId + "/" + listType + "/" + offset + "/" + count, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                if (xrcy != null)
                    xrcy.loadMoreComplete();
                entity = GsonHelper.getPerson(object.toString(), MerchantDetailEntity.class);
                if (entity.getTakingList().size() < count) {
                    T.showShort(CountMissionMerchantActivity.this, getString(R.string.last_page));
                }
                takingList.addAll(entity.getTakingList());
                tv_count_mission.setText(entity.getCount() + "");
//                takingAdapter.setTakingOrders(takingList);
                takingAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorCallBack(JSONObject object) {
                if (xrcy != null)
                    xrcy.loadMoreComplete();
            }
        }, tag, true);
    }

    //通过网络请求获取商家待清点任务列表
    private void getCountingMerchantMission(String listType) {
        int offset = 0;
        if (countingOrderList != null) {
            offset = countingOrderList.size();
        }
        final int count = 10;//默认5条数据
        BirdApi.getCountingMerchant(this, merchantId + "/" + listType + "/" + offset + "/" + count, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                if (xrcy != null)
                    xrcy.loadMoreComplete();
                CountingListResultEntity countingListResultEntity = GsonHelper.getPerson(object.toString(), CountingListResultEntity.class);
                if (countingListResultEntity.getList().size() < count) {
                    T.showShort(CountMissionMerchantActivity.this, getString(R.string.last_page));
                }
                countingOrderList.addAll(countingListResultEntity.getList());
                tv_count_mission.setText(countingListResultEntity.getCount() + "");
                countingAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorCallBack(JSONObject object) {
                if (xrcy != null)
                    xrcy.loadMoreComplete();
            }
        }, tag, true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
}
