package com.pda.birdex.pda.fragments;

import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.entity.TakingOrder;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
    ClearEditText edt_print_num;
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
        if (this.isVisible()) {

        }
    }

    @OnClick(R.id.btn_commit)
    @Override
    public void onClick(View v) {
//        startActivity(new Intent(getActivity(), PrintActivity.class));
        RequestParams params = new RequestParams();
        if (getActivity().getIntent().getExtras().getString("location").equals("1")) {//打印数量
            params.put("count", Integer.parseInt(edt_print_num.getText().toString()));
            params.put("owner", takingOrder.getPerson().getCo());
            params.put("tkNo", takingOrder.getBaseInfo().getTakingOrderNo());
        } else {
            params.put("count", 1);
            params.put("owner", orderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo());
            params.put("tkNo", orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
        }
        BirdApi.postCodePrint(getActivity(), params, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                List<String> list = null;
                try {
                    list = (List<String>) object.get("list");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bus.post(list);
            }

            @Override
            public void errorCallBack(JSONObject object) {

            }
        },tag,true);

    }
}
