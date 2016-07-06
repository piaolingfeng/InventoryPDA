package com.pda.birdex.pda.fragments;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.entity.TakingOrder;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/25.
 */
public class TakingToolBindAreaFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "TakingToolBindAreaFragment";

    @Bind(R.id.edt_taking_num)
    com.pda.birdex.pda.widget.ClearEditText edt_taking_num;
    @Bind(R.id.edt_area)
    com.pda.birdex.pda.widget.ClearEditText edt_area;
    @Bind(R.id.tv_taking_num)
    TextView tv_taking_num;
    TakingOrder takingOrder;//位置1进来传来的实体
    TakingOrderNoInfoEntity orderNoInfoEntity;//位置2进来传来的实体
    ContainerInfo containerInfo;//位置2进来时传进来的item

    private String from;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_taking_tool_bindarea_layout;
    }

    @Override
    public void barInitializeContentViews() {

        from = getActivity().getIntent().getExtras().getString("location_position");
        if ("1".equals(from)) {
            takingOrder = (TakingOrder) getActivity().getIntent().getExtras().get("takingOrder");
            if(takingOrder!=null) {
                tv_taking_num.setText(takingOrder.getBaseInfo().getTakingOrderNo());
            }
        } else {//打印数量
            orderNoInfoEntity = (TakingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("orderNoInfoEntity");
            if(orderNoInfoEntity!=null) {
                containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
                tv_taking_num.setText(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
            }
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
        if (this.isVisible()) {

        }
    }

    @OnClick(R.id.btn_commit)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

                JSONObject jsonObject = new JSONObject();

                try {


                    // 调用绑定接口
//                    RequestParams params = new RequestParams();
                    jsonObject.put("containerNo", edt_taking_num.getText() + "");
                    jsonObject.put("areaCode", edt_area.getText() + "");
                    if ("1".equals(from)) {
                        jsonObject.put("owner", takingOrder.getPerson().getCo());
                    } else {
                        jsonObject.put("owner", orderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo());
                    }

                    BirdApi.takingBind(getContext(), jsonObject, new RequestCallBackInterface() {

                        @Override
                        public void successCallBack(JSONObject object) {
                            T.showShort(getActivity(), getString(R.string.taking_bind_suc));
                        }

                        @Override
                        public void errorCallBack(JSONObject object) {
                            T.showShort(getActivity(), getString(R.string.taking_bind_fal));
                        }
                    }, tag, true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
