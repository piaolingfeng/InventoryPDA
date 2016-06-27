package com.pda.birdex.pda.fragments;

import android.view.View;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.TakingToolActivity;
import com.pda.birdex.pda.entity.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.widget.ClearEditText;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/25.
 */
public class TakingToolPrintNumFragment extends BarScanBaseFragment implements View.OnClickListener{
    String tag="TakingToolPrintNumFragment";

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
    TakingOrderNoInfoEntity entity;
    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_taking_tool_print_layout;
    }

    @Override
    public void barInitializeContentViews() {
        entity = TakingToolActivity.orderNoInfo;
        if(entity!=null){
            tv_bussiness.setText(entity.getDetail().getBaseInfo().getPerson().getName());
            if(entity.getDetail().getContainerList()==null){
                edt_print_num.setVisibility(View.GONE);
                tv_taking_container.setVisibility(View.VISIBLE);
                tv_print_num.setText(getString(R.string.taking_num));
            }
        }
        tv_print_num.setText(TakingToolActivity.takingOrderNo);
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

    }
}
