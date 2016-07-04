package com.pda.birdex.pda.fragments;

import android.view.View;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.widget.ClearEditText;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/24.
 */
public class CountToolUnbindFragment extends BarScanBaseFragment implements View.OnClickListener{
    @Bind(R.id.tv_count_num)
    TextView tv_count_num;
    @Bind(R.id.tv_area)
    TextView tv_area;
    @Bind(R.id.edt_taking_num)
    ClearEditText edt_taking_num;
    TakingOrderNoInfoEntity orderNoInfoEntity;//位置2进来传来的实体
    ContainerInfo containerInfo;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_count_tool_unbind_layout;
    }

    @Override
    public void barInitializeContentViews() {
        orderNoInfoEntity = (TakingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("orderNoInfoEntity");
        if (orderNoInfoEntity != null) {
            tv_count_num.setText(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
        }
        containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_taking_num;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }

    @OnClick({R.id.btn_unbind,R.id.tv_check_map})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                break;
            case R.id.tv_check_map:
                break;
        }
    }
}
