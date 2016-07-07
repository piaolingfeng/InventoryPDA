package com.pda.birdex.pda.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.response.CountingOrderNoInfoEntity;
import com.pda.birdex.pda.widget.ClearEditText;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/24.
 */
public class CountToolTrackFragment extends BarScanBaseFragment {
    @Bind(R.id.tv_count_num)
    TextView tv_count_num;
    @Bind(R.id.edt_count_now_no)
    ClearEditText edt_count_now_no;
    @Bind(R.id.edt_count_old_no)
    ClearEditText edt_count_old_no;
    CountingOrderNoInfoEntity countingOrderNoInfoEntity;//清点任务详情
    ContainerInfo containerInfo;//位置2进来时传进来的item
    Bundle b;
    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_count_tool_track_layout;
    }

    @Override
    public void barInitializeContentViews() {
        b = getActivity().getIntent().getExtras();
        if(b!=null){
            countingOrderNoInfoEntity = (CountingOrderNoInfoEntity) b.get("countingOrderNoInfoEntity");
            containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
            if (countingOrderNoInfoEntity != null) {
                tv_count_num.setText(countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getOrderNo());
            }
        }
        edt_count_now_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_count_now_no.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_count_now_no);
                }
            }
        });
        edt_count_old_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_count_old_no.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_count_old_no);
                }
            }
        });
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_count_now_no;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }
}
