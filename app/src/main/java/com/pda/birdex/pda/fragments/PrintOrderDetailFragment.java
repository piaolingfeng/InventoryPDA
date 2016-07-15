package com.pda.birdex.pda.fragments;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.PrintEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/7/15.
 */
public class PrintOrderDetailFragment extends BaseFragment implements View.OnClickListener, RequestCallBackInterface {
    String tag = "PrintOrderDetailFragment";
    @Bind(R.id.no_tv)
    TextView no_tv;
    @Bind(R.id.tv_order)
    TextView tv_order;

    @Bind(R.id.tv_printsame)
    TextView tv_printsame;
    @Bind(R.id.tv_printnew)
    TextView tv_printnew;
    @Bind(R.id.edt_print_count)
    ClearEditText edt_print_count;
    @Bind(R.id.edt_print_copies)
    ClearEditText edt_print_copies;
    @Bind(R.id.pll_print_copies)
    PercentLinearLayout pll_print_copies;
    @Bind(R.id.tv_print_copies)
    TextView tv_print_copies;

    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_lanshouprint;
    }

    @Override
    public void initializeContentViews() {
        String name = getActivity().getIntent().getExtras().getString("inputname");
        if (!TextUtils.isEmpty(name)) {
            no_tv.setText(name);
        }
        String order = "";
        if (bundle != null)
            order = bundle.getString("code");
        tv_order.setText(order + "");
    }

    @Override
    protected void lazyLoad() {

    }

    @OnClick({R.id.tv_printsame, R.id.tv_printnew, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_printsame:
            case R.id.tv_printnew:
                setPrintStae(v);
                break;
            case R.id.btn_commit:
                if ("same".equals(printState)) {
                    printSame();
                } else {
                    printNew();
                }
                break;
        }
    }

    String printState = "new";

    //打印类型
    private void setPrintStae(View v) {
        switch (v.getId()) {
            case R.id.tv_printnew:
                pll_print_copies.setVisibility(View.VISIBLE);
                tv_printsame.setBackgroundResource(R.drawable.item_select_y);
                tv_printsame.setTextColor(getResources().getColor(R.color.white));
                tv_printnew.setBackgroundResource(R.drawable.item_select_n);
                tv_printnew.setTextColor(getResources().getColor(R.color.blue_head_1));
                printState = "new";
                tv_print_copies.setText(getString(R.string.print_count));
                break;
            case R.id.tv_printsame:
                pll_print_copies.setVisibility(View.INVISIBLE);
                tv_printsame.setBackgroundResource(R.drawable.item_select_n);
                tv_printsame.setTextColor(getResources().getColor(R.color.blue_head_1));
                tv_printnew.setBackgroundResource(R.drawable.item_select_y);
                tv_printnew.setTextColor(getResources().getColor(R.color.white));
                printState = "same";
                tv_print_copies.setText(getString(R.string.print_copies));
                break;
        }
    }

    //打印相同箱单
    private void printSame() {
        if (no_tv.getText().equals(getString(R.string.count_box_no)))// 清点
            BirdApi.postCountingCodeSamePrint(getActivity(), tv_order.getText().toString(), this, tag, true);
        else if (no_tv.getText().equals(getString(R.string.taking_num)))// 揽收
            BirdApi.postTakingCodeSamePrint(getActivity(), tv_order.getText().toString(), this, tag, true);
        else if (no_tv.getText().equals(getString(R.string.count_vessel_no)))// 入库
            BirdApi.postStockInCodeSamePrint(getActivity(), tv_order.getText().toString(), this, tag, true);
    }

    //打印新的箱单
    private void printNew() {
        String order = tv_order.getText().toString();
        String count = edt_print_count.getText().toString();
        if (no_tv.getText().equals(getString(R.string.count_box_no)))// 清点
            BirdApi.postCountingCodeNewPrint(getActivity(), order+"/"+count, this, tag, true);
        else if (no_tv.getText().equals(getString(R.string.taking_num)))// 揽收
            BirdApi.postTakingCodeNewPrint(getActivity(), order+"/"+count, this, tag, true);
        else if (no_tv.getText().equals(getString(R.string.count_vessel_no)))// 入库
            BirdApi.postStockInCodeNewPrint(getActivity(), order+"/"+count, this, tag, true);
    }

    @Override
    public void successCallBack(JSONObject object) {
        PrintEntity entity = GsonHelper.getPerson(object.toString(), PrintEntity.class);
        //日志上报
        String orderId = entity.getOrderNo();
        String tid = entity.getTid();
        if (no_tv.getText().equals(getString(R.string.count_box_no)))// 清点日志上报
            MyApplication.loggingUpload.countPrint(getActivity(), tag, orderId, tid, entity.getContainerNos());
        else if (no_tv.getText().equals(getString(R.string.taking_num)))//揽收日志上报
            MyApplication.loggingUpload.takePrint(getActivity(), tag, orderId, tid, entity.getContainerNos());
        else if (no_tv.getText().equals(getString(R.string.count_vessel_no)))// 入库
        {

        }
        if (entity != null) {
            List<String> list = entity.getData();
            if ("same".equals(printState)) {//打印相同
                List<String> printlist = new ArrayList<>();
                int count = Integer.parseInt(edt_print_count.getText().toString());
                for (int i = 0; i < count; i++) {
                    if (list.size() > 0)
                        printlist.add(list.get(0));
                }
                list = printlist;
            } else {//打印新的
                List<String> printlist = new ArrayList<>();
                int count = Integer.parseInt(edt_print_count.getText().toString());
                int copy = Integer.parseInt(edt_print_copies.getText().toString());
                for (int i = 0; i < count; i++) {
                    for (int ci = 0; ci < copy; ci++) {
                        if (list.size() == count) {
                            printlist.add(list.get(i));//每份打印数量
                        }
                    }
                }
                list = printlist;//新的打印列表
            }
            bus.post(list);
        } else {
            T.showLong(getActivity(), getString(R.string.parse_error));
        }
    }


    @Override
    public void errorCallBack(JSONObject object) {
//        T.showShort(this,getString(R.string.default_font_gray_scale));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
}
