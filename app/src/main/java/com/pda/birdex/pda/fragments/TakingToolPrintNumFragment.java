package com.pda.birdex.pda.fragments;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.entity.TakingOrder;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.PrintEntity;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.StringUtils;
import com.pda.birdex.pda.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/25.
 */
public class TakingToolPrintNumFragment extends BarScanBaseFragment implements View.OnClickListener, OnEditorActionListener {
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

        if (getActivity().getIntent().getExtras().getString("location_position").equals("1")) {//打印数量
            takingOrder = (TakingOrder) getActivity().getIntent().getExtras().get("takingOrder");
            if (takingOrder != null) {
                tv_taking_num.setText(takingOrder.getBaseInfo().getTakingOrderNo());
                tv_bussiness.setText(takingOrder.getPerson().getName());
            }
            edt_print_num.setOnEditorActionListener(this);
        } else {
            edt_print_num.setVisibility(View.GONE);
            tv_taking_container.setVisibility(View.VISIBLE);
            tv_print_num.setText(getString(R.string.taking_num));
            orderNoInfoEntity = (TakingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("orderNoInfoEntity");
            containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
            if (containerInfo != null)
                tv_taking_container.setText(containerInfo.getContainerId());
            if (orderNoInfoEntity != null) {
                tv_taking_num.setText(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
                tv_bussiness.setText(orderNoInfoEntity.getDetail().getBaseInfo().getPerson().getName());
            }
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
        print();
    }

    private void print() {
//        RequestParams params = new RequestParams();
        JSONObject jsonObject = new JSONObject();
        try {
            final String tid;
            final String orderId;
            if (getActivity().getIntent().getExtras().getString("location_position").equals("1")) {//打印数量
                int count = 1;
                if (!StringUtils.isEmpty(edt_print_num.getText().toString())) {
                    count = Integer.parseInt(edt_print_num.getText().toString());
                }
                jsonObject.put("count", count);
                jsonObject.put("owner", takingOrder.getPerson().getCo());
                jsonObject.put("tkNo", takingOrder.getBaseInfo().getTakingOrderNo());
                orderId = takingOrder.getBaseInfo().getTakingOrderNo();
                tid = takingOrder.getBaseInfo().getTid();
            } else {
                jsonObject.put("count", 1);
                jsonObject.put("owner", orderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo());
                jsonObject.put("tkNo", orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
                orderId = orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo();
                tid = orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid();
            }
            BirdApi.postCodePrint(getActivity(), jsonObject, new RequestCallBackInterface() {
                @Override
                public void successCallBack(JSONObject object) {
                    PrintEntity entity = GsonHelper.getPerson(object.toString(), PrintEntity.class);
                    MyApplication.loggingUpload.PrintTag(getActivity(), tag, orderId, tid, entity.getContainerNos());
                    bus.post(entity.getData());
                }

                @Override
                public void errorCallBack(JSONObject object) {

                }
            }, tag, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_UNSPECIFIED == actionId) {
            print();
        }
        return false;
    }

//    @Override
//    public void onKeyDown(int keyCode, KeyEvent event) {
////        T.showShort(getActivity(), "keyCode" + keyCode);
//        if(keyCode == KeyEvent.KEYCODE_ENTER){
//            print();
//        }
//        super.onKeyDown(keyCode, event);
//    }
}
