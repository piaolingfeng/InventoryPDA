package com.pda.birdex.pda.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.CountMissionClearNumAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.BaseInfo;
import com.pda.birdex.pda.entity.BindOrder;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.PrintEntity;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.StringUtils;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/23.
 */
public class CountMissionClearNumActivity extends BasePrintBarScanActivity implements OnTabSelectedListener, LoadingListener, OnRecycleViewItemClickListener, View.OnClickListener {
    String tag = "CountMissionClearNumActivity";
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.xrcy)
    XRecyclerView xrcy;
    @Bind(R.id.title)
    TitleView title;

    @Bind(R.id.tv_count_num)
    TextView tv_count_num;//单号
    @Bind(R.id.tv_operate_vessl)
    TextView tv_operate_vessl;//容器
    @Bind(R.id.tv_title_last_time)
    TextView tv_title_last_time;
    //tab
    @Bind(R.id.tv_status)
    TextView tv_status;
    @Bind(R.id.tv_clear_num)
    TextView tv_clear_num;
    @Bind(R.id.tv_last_time)
    TextView tv_last_time;

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
    TakingOrderNoInfoEntity orderNoInfoEntity;
    List<ContainerInfo> list;
    List<ContainerInfo> assignedList;//进行中列表
    List<ContainerInfo> unassignedList;//未开始列表
    List<ContainerInfo> doneList;//已完成列表

    String HeadName = "";
    BaseInfo baseInfo;
    String takingOrderNum = "";

    //    public String []tabList = {getString(R.string.not_start),getString(R.string.has_classified),
//            getString(R.string.has_counted),getString(R.string.has_transfer)};

    @Override
    public int getPrintContentLayoutResId() {
        return R.layout.activity_countmission_clearnum_layout;
    }

    @Override
    public void printInitializeContentViews() {
        title.setTitle(getString(R.string.count_task));
        HeadName = getIntent().getStringExtra("HeadName");
        baseInfo = (BaseInfo) getIntent().getExtras().get("baseInfo");
        if (baseInfo != null) {
            takingOrderNum = baseInfo.getTakingOrderNo();
            tv_count_num.setText(takingOrderNum);//揽收单号/清点单号
        }
        if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收
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
//        tv_status.setVisibility(View.GONE);//隐藏状态栏

        xrcy.setLoadingMoreEnabled(false);
        xrcy.setPullRefreshEnabled(true);
        xrcy.setLoadingListener(this);//加载监听器
        xrcy.setLayoutManager(new LinearLayoutManager(this));
        xrcy.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);//上拉加载类型
        adapter = new CountMissionClearNumAdapter(this, list);
        adapter.setOnRecycleViewItemClickListener(this);//item点击

        xrcy.setAdapter(adapter);

        String[] tabTitle = getResources().getStringArray(R.array.tab_title);
        tv_clear_num.setText(tabTitle[0]);
        tv_last_time.setText(tabTitle[1]);
        tv_status.setText(tabTitle[2]);
        String[] tabList = getResources().getStringArray(R.array.tab_list);
        //添加3种分类
        for (int i = 0; i < tabList.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_tab_layout, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_tab_title);
            textView.setText(tabList[i]);
            tablayout.addTab(tablayout.newTab().setCustomView(view));
        }
        //分类切换监听器
        tablayout.setOnTabSelectedListener(this);
        getTakingOrderDetail();//通过揽收单号获取揽收单详情
    }

    //处理
    private void dealDetail() {
//        String time = TimeUtil.long2Date(Long.parseLong(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getDeadLine()));
        tv_title_last_time.setText(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getDeadLine());
        tv_operate_vessl.setText(orderNoInfoEntity.getDetail().getContainerList().size() + "");
        list = orderNoInfoEntity.getDetail().getContainerList();
        unassignedList = new ArrayList<>();
        doneList = new ArrayList<>();
        assignedList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!StringUtils.isEmpty(list.get(i).getArea()) && list.get(i).getCount() > 0) {//已完成
                doneList.add(list.get(i));
            } else {
                if (!StringUtils.isEmpty(list.get(i).getArea())) {//进行中
                    assignedList.add(list.get(i));
                } else {//未开始
                    unassignedList.add(list.get(i));
                }
            }
        }
        switch (tablayout.getSelectedTabPosition()) {
            case 0:
                adapter.setList(unassignedList);
                break;
            case 1:
                adapter.setList(assignedList);
                break;
            case 2:
                adapter.setList(doneList);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    //通过揽收单号获取揽收单详情
    private void getTakingOrderDetail() {
        BirdApi.takingOrderNoInfo(this, baseInfo.getTid(), new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                xrcy.refreshComplete();
                orderNoInfoEntity = GsonHelper.getPerson(object.toString(), TakingOrderNoInfoEntity.class);
                if(orderNoInfoEntity!=null)
                    dealDetail();
                else{
                    T.showShort(CountMissionClearNumActivity.this,getString(R.string.parse_error));
                }
            }

            @Override
            public void errorCallBack(JSONObject object) {
                xrcy.refreshComplete();
            }
        }, tag, true);
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_taking_scan_no;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        //扫描回调
        if (code != null && orderNoInfoEntity != null) {
            List<BindOrder> containerConfig = new ArrayList<>();
            BindOrder bo = new BindOrder();
            bo.setCode(code);
            bo.setOwner(orderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo());
            containerConfig.add(bo);
            JSONObject jsonObject = new JSONObject();
            try {
                String str = GsonHelper.createJsonString(containerConfig);
//                        jsonObject.put("containerConfig",str);

                JSONArray object = new JSONArray(str);
                jsonObject.put("containerConfig", object);
                jsonObject.put("tid", orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid());
//                        jsonObject.put("tid", "MET:TK-160630000003");
                StringEntity stringEntity = new StringEntity(jsonObject.toString());
//                        stringEntity.setContentType("application/json");

                BirdApi.jsonTakingBindorderSubmit(this, jsonObject, new RequestCallBackInterface() {

                    @Override
                    public void successCallBack(JSONObject object) {
                        T.showShort(CountMissionClearNumActivity.this, getString(R.string.taking_submit_suc));
                    }

                    @Override
                    public void errorCallBack(JSONObject object) {
                        T.showShort(CountMissionClearNumActivity.this, getString(R.string.taking_submit_fal));
                    }
                }, tag, true);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
//        T.showShort(CountMissionClearNumActivity.this, "");
        switch (tab.getPosition()) {
            case 0:
                adapter.setList(unassignedList);
                break;
            case 1:
                adapter.setList(assignedList);
                break;
            case 2:
                adapter.setList(doneList);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onRefresh() {
        getTakingOrderDetail();
    }

    @Override
    public void onLoadMore() {
//        xrcy.loadMoreComplete();
    }

    @Override
    public void onItemClick(int position) {
        if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收
            Intent intent = new Intent(this, TakingCheckActivity.class);
            intent.putExtra("location", "2");//揽收任务
            Bundle b = new Bundle();
            b.putSerializable("orderNoInfoEntity", orderNoInfoEntity);
            switch (tablayout.getSelectedTabPosition()) {
                case 0:
                    b.putSerializable("containerInfo", unassignedList.get(position));
                    intent.putExtras(b);
                    break;
                case 1:
                    b.putSerializable("containerInfo", assignedList.get(position));
                    intent.putExtras(b);
                    break;
                case 2:
                    b.putSerializable("containerInfo", doneList.get(position));
                    intent.putExtras(b);
                    break;
            }
            startActivity(intent);
        } else {
            if (tablayout.getSelectedTabPosition() != 2) {
                Intent intent = new Intent(this, CountToolActivity.class);
                intent.putExtra("statusPosition", tablayout.getSelectedTabPosition());
                startActivity(intent);
            }
        }
    }

    @Override
    public void onDestroy() {
        BirdApi.cancelRequestWithTag(tag);
        super.onDestroy();
    }


    @Override
    public TitleView printTitleView() {
        return title;
    }

    private void commit() {
        RequestParams params = new RequestParams();
        List containerId = new ArrayList();
        int count = 0;
        if (doneList != null) {
            for (ContainerInfo info : doneList) {
                containerId.add(info.getContainerId());
                count += info.getCount();
            }
        }
        params.put("takingOrderNo", takingOrderNum);
        params.put("containerNo", containerId);
        params.put("count", count);

        JSONObject jsonObject = new JSONObject();
        String str = GsonHelper.createJsonString(containerId);
        try {
            JSONArray jsonArray = new JSONArray(str);
            jsonObject.put("takingOrderNo", takingOrderNum);
            jsonObject.put("containerNo", jsonArray);
            jsonObject.put("count", count);
            BirdApi.postTakingOrderNum(this, jsonObject, new RequestCallBackInterface() {
                @Override
                public void successCallBack(JSONObject object) {
                    T.showShort(CountMissionClearNumActivity.this, getString(R.string.commit_success));
                }

                @Override
                public void errorCallBack(JSONObject object) {

                }
            }, tag, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        BirdApi.postTakingOrderNum(this, params, new RequestCallBackInterface() {
//            @Override
//            public void successCallBack(JSONObject object) {
//                T.showShort(CountMissionClearNumActivity.this, getString(R.string.commit_success));
//            }
//
//            @Override
//            public void errorCallBack(JSONObject object) {
//
//            }
//        }, tag, true);
    }

    private void print() {
        if (orderNoInfoEntity != null) {
            RequestParams params = new RequestParams();
            params.put("count", 1);
            params.put("owner", orderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo());
            params.put("tkNo", orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
            BirdApi.postCodePrint(this, params, new RequestCallBackInterface() {
                @Override
                public void successCallBack(JSONObject object) {
                    PrintEntity entity = GsonHelper.getPerson(object.toString(), PrintEntity.class);
                    if(entity!=null && entity.getData()!=null){//发送给打印机
                        for (String i:entity.getData()){
                            sendMessage(i);
                        }
                    }
                }

                @Override
                public void errorCallBack(JSONObject object) {

                }
            }, tag, true);
        }
    }

    @OnClick({R.id.btn_commit, R.id.btn_count_print_no})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                commit();
                break;
            case R.id.btn_count_print_no:
                print();
                break;
        }
    }
}
