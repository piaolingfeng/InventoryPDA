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
import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.CountMissionClearNumAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.BaseInfo;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.entity.CountingBaseInfo;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.CountingOrderNoInfoEntity;
import com.pda.birdex.pda.response.PrintEntity;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.StringUtils;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/23.
 */
public class MissionClearNumActivity extends BasePrintBarScanActivity implements OnTabSelectedListener, LoadingListener, OnRecycleViewItemClickListener, View.OnClickListener {
    String tag = "MissionClearNumActivity";
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

    @Bind(R.id.pll_item)
    PercentLinearLayout pll_item;
    //揽收清点的空间
    @Bind(R.id.tv_name_count_num)
    TextView tv_name_count_num;
    @Bind(R.id.pll_taking_scan_no)
    PercentLinearLayout pll_taking_scan_no;
    @Bind(R.id.edt_taking_scan_no)
    ClearEditText edt_taking_scan_no;


    CountMissionClearNumAdapter adapter;
    TakingOrderNoInfoEntity takingOrderNoInfoEntity;//揽收任务详情
    CountingOrderNoInfoEntity countingOrderNoInfoEntity;//清点任务详情
    List<ContainerInfo> list;
//    List<ContainerInfo> assignedList;//进行中列表
//    List<ContainerInfo> unassignedList;//未开始列表
//    List<ContainerInfo> doneList;//已完成列表

    String HeadName = "";
    BaseInfo baseInfo;
    CountingBaseInfo countingBaseInfo;
    String takingOrderNum = "";

    //    public String []tabList = {getString(R.string.not_start),getString(R.string.has_classified),
//            getString(R.string.has_counted),getString(R.string.has_transfer)};

    @Override
    public int getPrintContentLayoutResId() {
        return R.layout.activity_countmission_clearnum_layout;
    }

    @Override
    public void printInitializeContentViews() {
        HeadName = getIntent().getStringExtra("HeadName");

        pll_item.setBackgroundColor(getResources().getColor(R.color.gray));
        if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收
            tv_name_count_num.setText(getString(R.string.tv_taking_num));
            btn_count_print_no.setText(getString(R.string.taking_print_no));
//            pll_taking_scan_no.setVisibility(View.VISIBLE);
            baseInfo = (BaseInfo) getIntent().getExtras().get("baseInfo");
            if (baseInfo != null) {
                takingOrderNum = baseInfo.getTakingOrderNo();
                tv_count_num.setText(takingOrderNum);//揽收单号
            }
            title.setTitle(getString(R.string.taking_task));
        } else {
            title.setTitle(getString(R.string.count_task));
            countingBaseInfo = (CountingBaseInfo) getIntent().getExtras().get("baseInfo");
            if (countingBaseInfo != null) {
                takingOrderNum = countingBaseInfo.getOrderNo();//清点单号
                tv_count_num.setText(takingOrderNum);
            }
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
        xrcy.setPullRefreshEnabled(false);
        xrcy.setLoadingListener(this);//加载监听器
        xrcy.setLayoutManager(new LinearLayoutManager(this));
        xrcy.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);//上拉加载类型
        adapter = new CountMissionClearNumAdapter(this, list);
        adapter.setOnRecycleViewItemClickListener(this);//item点击

        xrcy.setAdapter(adapter);

        String[] tabTitle = getResources().getStringArray(R.array.tab_title);
        tv_clear_num.setText(tabTitle[0]);
        tv_last_time.setText(tabTitle[2]);
        tv_status.setText(tabTitle[1]);
        tv_status.setVisibility(View.GONE);//不需要数量这块
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
        tablayout.setVisibility(View.GONE);
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收
            getTakingOrderDetail();//通过揽收单号获取揽收单详情
        } else {
            getCountingOrderDetail();//获取清点单详情
        }
    }

    //处理揽收单
    private void dealTakingDetail() {
//       String time = TimeUtil.long2Date(Long.parseLong(takingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getDeadLine()));
        tv_title_last_time.setText(takingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getDeadLine());
        tv_operate_vessl.setText(takingOrderNoInfoEntity.getDetail().getContainerList().size() + "");
        list = takingOrderNoInfoEntity.getDetail().getContainerList();
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    //处理清点单
    private void dealCountDetail() {
        list = countingOrderNoInfoEntity.getDetail().getContainerList();
//        list.addAll(countingOrderNoInfoEntity.getDetail().getTakingContainList());
        List<ContainerInfo> takingContainList =   countingOrderNoInfoEntity.getDetail().getTakingContainList();
        for(int i=0;i<list.size();i++){
            for(int ti=0;ti<takingContainList.size();ti++) {
                if (list.get(i).getContainerId().equals(takingContainList.get(ti).getContainerId())) {
                    list.remove(i);
                    i--;
                    break;
                }
            }
        }
        list.addAll(takingContainList);
        tv_title_last_time.setText(countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getDeadline());
        tv_operate_vessl.setText(countingOrderNoInfoEntity.getDetail().getContainerList().size() + "");
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }



    //通过揽收单号获取揽收单详情
    private void getTakingOrderDetail() {
        BirdApi.takingOrderNoInfo(this, baseInfo.getTid(), new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                xrcy.refreshComplete();
                takingOrderNoInfoEntity = GsonHelper.getPerson(object.toString(), TakingOrderNoInfoEntity.class);
                if (takingOrderNoInfoEntity != null)
                    dealTakingDetail();
                else {
                    T.showShort(MissionClearNumActivity.this, getString(R.string.parse_error));
                }
            }

            @Override
            public void errorCallBack(JSONObject object) {
                xrcy.refreshComplete();
            }
        }, tag, true);
    }

    //通过清点单号获取清点单详情
    private void getCountingOrderDetail() {
        BirdApi.getCountingOrderNoInfo(this, countingBaseInfo.getTid(), new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                xrcy.refreshComplete();
                countingOrderNoInfoEntity = GsonHelper.getPerson(object.toString(), CountingOrderNoInfoEntity.class);
                if (countingOrderNoInfoEntity != null)
                    dealCountDetail();
                else {
                    T.showShort(MissionClearNumActivity.this, getString(R.string.parse_error));
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
        if (StringUtils.isEmpty(code))
            return;
        for (int i = 0; i < list.size(); i++) {//如果已经绑定就跳转到详情，否则先绑定，后进入详情
            if (list.get(i).getContainerId().equals(code)) {
                enterToCheckActivity(i);
                return;
            }
        }
        ScanBindContainer(code);
    }

    /**
     * 扫描绑定容器号并上报日志
     * */
    private void ScanBindContainer(final String code){
        final List<String> containerConfig = new ArrayList<>();
        containerConfig.add(code);
        JSONObject jsonObject = new JSONObject();
        JSONArray object = new JSONArray(containerConfig);
        if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收
            if (takingOrderNoInfoEntity != null) {
                try{
                    jsonObject.put("containers", object);
                    jsonObject.put("orderNO", takingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
                    BirdApi.jsonTakingBindorderBatSubmit(this, jsonObject, new RequestCallBackInterface() {

                        @Override
                        public void successCallBack(JSONObject object) {
                            //揽收绑定日志上报
                            String orderId = takingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo();
                            String tid = takingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid();
                            MyApplication.loggingUpload.takeBindOrder(MissionClearNumActivity.this, tag, orderId, tid, containerConfig);
                            //成功后页面切换
                            T.showShort(MissionClearNumActivity.this, getString(R.string.taking_bind_suc));
                            ContainerInfo info = new ContainerInfo();
                            info.setContainerId(code);
                            takingOrderNoInfoEntity.getDetail().getContainerList().add(info);
                            dealTakingDetail();//重新分配list给三种不同状态
                            enterToCheckActivity(list.size() - 1);//从list的最后一个进入activity
                        }

                        @Override
                        public void errorCallBack(JSONObject object) {
                            T.showShort(MissionClearNumActivity.this, getString(R.string.taking_bind_fal));
                        }
                    }, tag, true);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else{
            if (countingOrderNoInfoEntity != null) {
                try{
                    jsonObject.put("containers", object);
                    jsonObject.put("orderNO", countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getOrderNo());
                    BirdApi.jsonCountBindorderSubmit(this, jsonObject, new RequestCallBackInterface() {

                        @Override
                        public void successCallBack(JSONObject object) {
                            //绑定清点单日志上报
                            String orderId = countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getOrderNo();
                            String tid = countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid();
                            MyApplication.loggingUpload.countBindOrder(MissionClearNumActivity.this, tag, orderId, tid, containerConfig);

                            //成功后页面切换
                            T.showShort(MissionClearNumActivity.this, getString(R.string.taking_bind_suc));
                            ContainerInfo info = new ContainerInfo();
                            info.setContainerId(code);
                            countingOrderNoInfoEntity.getDetail().getContainerList().add(info);
                            dealCountDetail();//重新分配list给三种不同状态
                            enterToCheckActivity(list.size() - 1);//从list的最后一个进入activity
                        }

                        @Override
                        public void errorCallBack(JSONObject object) {
                            T.showShort(MissionClearNumActivity.this, getString(R.string.taking_submit_fal));
                        }
                    }, tag, true);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
//        T.showShort(MissionClearNumActivity.this, "");
//        switch (tab.getPosition()) {
//            case 0:
//                adapter.setList(unassignedList);
//                break;
//            case 1:
//                adapter.setList(assignedList);
//                break;
//            case 2:
//                adapter.setList(doneList);
//                break;
//        }
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onRefresh() {
        if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收
            getTakingOrderDetail();//通过揽收单号获取揽收单详情
        } else {
            getCountingOrderDetail();//获取清点单详情
        }
    }

    @Override
    public void onLoadMore() {
//        xrcy.loadMoreComplete();
    }

    /**
     * 搜索后进入揽收查看器,
     */
    private void enterToCheckActivity(int position) {
        Intent intent = new Intent();
        Bundle b = new Bundle();
        if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收
            b.putSerializable("orderNoInfoEntity", takingOrderNoInfoEntity);
            b.putString("checkType", getString(R.string.taking));//揽收进入
            b.putString("location_position", "2");//揽收任务进入
        }else{
            b.putSerializable("countingOrderNoInfoEntity", countingOrderNoInfoEntity);
            b.putString("checkType", getString(R.string.count));//揽收进入
        }
        b.putSerializable("containerInfo", list.get(position));
        intent.setClass(this, CheckActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent();
        Bundle b = new Bundle();
//        switch (tablayout.getSelectedTabPosition()) {
//            case 0:
//                b.putSerializable("containerInfo", unassignedList.get(position));
//                intent.putExtras(b);
//                break;
//            case 1:
//                b.putSerializable("containerInfo", assignedList.get(position));
//                intent.putExtras(b);
//                break;
//            case 2:
//                b.putSerializable("containerInfo", doneList.get(position));
//                intent.putExtras(b);
//                break;
//        }
        if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收
            b.putSerializable("orderNoInfoEntity", takingOrderNoInfoEntity);
            b.putString("location_position", "2");//揽收任务进入
            b.putString("checkType", getString(R.string.taking));//揽收进入
        } else {//清点任务进入
            b.putSerializable("countingOrderNoInfoEntity", countingOrderNoInfoEntity);
            b.putString("checkType", getString(R.string.count));//清点进入
        }
        intent.setClass(this, CheckActivity.class);
        b.putSerializable("containerInfo", list.get(position));
        intent.putExtras(b);
        startActivity(intent);
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

    /**
     * 此功能已取消
     */
    private void commit() {
        RequestParams params = new RequestParams();
        List containerId = new ArrayList();
        int count = 0;
//        if (doneList != null) {
//            for (ContainerInfo info : doneList) {
//                containerId.add(info.getContainerId());
//                count += info.getCount();
//            }
//        }
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
                    T.showShort(MissionClearNumActivity.this, getString(R.string.commit_success));
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
//                T.showShort(MissionClearNumActivity.this, getString(R.string.commit_success));
//            }
//
//            @Override
//            public void errorCallBack(JSONObject object) {
//
//            }
//        }, tag, true);
    }

    private void print() {
        if (takingOrderNoInfoEntity != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("count", 1);
                jsonObject.put("owner", takingOrderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo());
                jsonObject.put("orderNo", takingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
                BirdApi.postTakingCodePrint(this, jsonObject, callBackInterface, tag, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (countingOrderNoInfoEntity != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("count", 1);
                jsonObject.put("owner", countingOrderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo());
                jsonObject.put("orderNo", countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getOrderNo());
                BirdApi.postCountingCodePrint(this, jsonObject, callBackInterface, tag, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    RequestCallBackInterface callBackInterface = new RequestCallBackInterface() {
        @Override
        public void successCallBack(JSONObject object) {
            PrintEntity entity = GsonHelper.getPerson(object.toString(), PrintEntity.class);
            if (entity != null && entity.getData() != null) {//发送给打印机
                for (String i : entity.getData()) {
                    sendMessage(i);
                }
            }
            //打印日志上报
            if (getResources().getString(R.string.taking).equals(HeadName)) {//揽收
                String orderId = takingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo();
                String tid = takingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid();
                MyApplication.loggingUpload.takePrint(MissionClearNumActivity.this, tag, orderId, tid, entity.getContainerNos());
            }else{
                String orderId = countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getOrderNo();
                String tid = countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid();
                MyApplication.loggingUpload.countPrint(MissionClearNumActivity.this, tag, orderId, tid, entity.getContainerNos());
            }
        }

        @Override
        public void errorCallBack(JSONObject object) {
            try {
                T.showShort(MissionClearNumActivity.this, object.getString("errMsg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @OnClick({R.id.btn_commit, R.id.btn_count_print_no})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
//                commit();
                break;
            case R.id.btn_count_print_no:
                print();
                break;
        }
    }
}
