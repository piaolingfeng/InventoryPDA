package com.pda.birdex.pda.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.PrintActivity;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.entity.TakingOrder;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.widget.ClearEditText;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/25.
 */
public class TakingToolPrintNumFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "TakingToolPrintNumFragment";

    @Bind(R.id.tv_bussiness)
    TextView tv_bussiness;
    @Bind(R.id.tv_taking_num)
    TextView tv_taking_num;
    @Bind(R.id.tv_print_num)
    TextView tv_print_num;
    @Bind(R.id.edt_print_num)
    TextView edt_print_num;
    @Bind(R.id.tv_taking_container)
    TextView tv_taking_container;
    TakingOrder takingOrder;//位置1进来传来的实体
    TakingOrderNoInfoEntity orderNoInfoEntity;//位置2进来传来的实体
    ContainerInfo containerInfo;
    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_taking_tool_print_layout;
    }

    @Override
    public void barInitializeContentViews() {

        if (getActivity().getIntent().getExtras().getString("location").equals("1")) {//打印数量
            takingOrder = (TakingOrder) getActivity().getIntent().getExtras().get("takingOrder");
            tv_taking_num.setText(takingOrder.getBaseInfo().getTakingOrderNo());
            tv_bussiness.setText(takingOrder.getPerson().getName());
        } else {
            edt_print_num.setVisibility(View.GONE);
            tv_taking_container.setVisibility(View.VISIBLE);
            tv_print_num.setText(getString(R.string.taking_num));
            orderNoInfoEntity = (TakingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("orderNoInfoEntity");
            containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
            tv_taking_num.setText(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
            tv_bussiness.setText(orderNoInfoEntity.getDetail().getBaseInfo().getPerson().getName());
        }
    }

    @Override
    public ClearEditText getClearEditText() {
        return null;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }

    @OnClick(R.id.btn_commit)
    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), PrintActivity.class));
    }
}
