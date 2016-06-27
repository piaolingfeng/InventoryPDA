package com.pda.birdex.pda.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView.LoadingListener;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.CountMissionClearNumAdapter;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;
import com.zhy.android.percent.support.PercentLinearLayout;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/23.
 */
public class CountMissionClearNumActivity extends BarScanActivity implements OnTabSelectedListener, LoadingListener, OnRecycleViewItemClickListener {
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.xrcy)
    XRecyclerView xrcy;
    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.tv_count_num)
    TextView tv_count_num;
    @Bind(R.id.tv_last_time)
    TextView tv_last_time;
    @Bind(R.id.tv_operate_vessl)
    TextView tv_operate_vessl;
    @Bind(R.id.tv_status)
    TextView tv_status;

    @Bind(R.id.btn_count_print_no)
    Button btn_count_print_no;
    //揽收清点的空间
    @Bind(R.id.tv_name_count_num)
    TextView tv_name_count_num;
    @Bind(R.id.pll_taking_scan_no)
    PercentLinearLayout pll_taking_scan_no;
    @Bind(R.id.edt_taking_scan_no)
    ClearEditText edt_taking_scan_no;

    CountMissionClearNumAdapter adapter;

    String HeadName="";
    //    public String []tabList = {getString(R.string.not_start),getString(R.string.has_classified),
//            getString(R.string.has_counted),getString(R.string.has_transfer)};

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_countmission_clearnum_layout;
    }

    @Override
    public void barInitializeContentViews() {
        title.setTitle(getString(R.string.count_task));
        HeadName = getIntent().getStringExtra("HeadName");
        if(getResources().getString(R.string.taking).equals(HeadName)){//揽收
            tv_name_count_num.setText(getString(R.string.tv_taking_num));
            btn_count_print_no.setText(getString(R.string.taking_print_no));
            pll_taking_scan_no.setVisibility(View.VISIBLE);
        }
        edt_taking_scan_no.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String string = v.getText().toString();
                    ClearEditTextCallBack(string);
                }
                return false;
            }
        });
        tv_status.setVisibility(View.GONE);//隐藏状态栏

        String[] tabList = {getString(R.string.not_start), getString(R.string.has_classified),
                getString(R.string.has_counted), getString(R.string.has_transfer)};//tablayoutName
        xrcy.setLoadingMoreEnabled(true);
        xrcy.setPullRefreshEnabled(false);
        xrcy.setLoadingListener(this);//加载监听器
        xrcy.setLayoutManager(new LinearLayoutManager(this));
        xrcy.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);//上拉加载类型
        adapter = new CountMissionClearNumAdapter(this);
        adapter.setOnRecycleViewItemClickListener(this);//item点击

        xrcy.setAdapter(adapter);
        //添加4种分类
        for (int i = 0; i < tabList.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_tab_layout, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_tab_title);
            textView.setText(tabList[i]);
            tablayout.addTab(tablayout.newTab().setCustomView(view));
        }
        //分类切换监听器
        tablayout.setOnTabSelectedListener(this);
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_taking_scan_no;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        //扫描回调
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        T.showShort(CountMissionClearNumActivity.this, "");
        switch (tab.getPosition()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
//        xrcy.loadMoreComplete();
    }

    @Override
    public void onItemClick(int position) {
        if (tablayout.getSelectedTabPosition() != 3) {
            if(getResources().getString(R.string.taking).equals(HeadName)){//揽收
                Intent intent = new Intent(this, TakingToolActivity.class);
                intent.putExtra("statusPosition", tablayout.getSelectedTabPosition());
                startActivity(intent);
            }else {
                Intent intent = new Intent(this, CountToolActivity.class);
                intent.putExtra("statusPosition", tablayout.getSelectedTabPosition());
                startActivity(intent);
            }
        }
    }
}
