package com.pda.birdex.pda.fragments;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.TakingToolActivity;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/25.
 */
public class TakingToolBindAreaFragment extends BarScanBaseFragment implements View.OnClickListener{
    String tag="TakingToolBindAreaFragment";

    @Bind(R.id.edt_taking_num)
    com.pda.birdex.pda.widget.ClearEditText edt_taking_num;
    @Bind(R.id.edt_area)
    com.pda.birdex.pda.widget.ClearEditText edt_area;
    @Bind(R.id.tv_taking_num)
    TextView tv_taking_num;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_taking_tool_bindarea_layout;
    }

    @Override
    public void barInitializeContentViews() {

        if(TakingToolActivity.takingOrderNo != null) {
            tv_taking_num.setText(TakingToolActivity.takingOrderNo);
        }

        edt_taking_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_taking_num.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_taking_num);
                }
            }
        });
        edt_area.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_area.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_area);
                }
            }
        });
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_taking_num;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }

    @OnClick(R.id.btn_commit)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                // 点击提交
                // 先判断是否为空
                if (TextUtils.isEmpty(edt_taking_num.getText())) {
                    T.showShort(getContext(), getString(R.string.taking_num_toast));
                    return;
                }
                if (TextUtils.isEmpty(edt_area.getText())) {
                    T.showShort(getContext(), getString(R.string.area_toast));
                    return;
                }

                // 调用绑定接口
                RequestParams params = new RequestParams();
                params.add("containerNo", edt_taking_num.getText() + "");
                params.add("areaCode", edt_area.getText() + "");
                BirdApi.takingBind(getContext(), params, new RequestCallBackInterface() {

                    @Override
                    public void successCallBack(JSONObject object) {
                        T.showShort(getActivity(), getString(R.string.taking_bind_suc));
                    }

                    @Override
                    public void errorCallBack(JSONObject object) {
                        T.showShort(getActivity(), getString(R.string.taking_bind_fal));
                    }
                }, tag, true);
                break;
        }
    }
}
