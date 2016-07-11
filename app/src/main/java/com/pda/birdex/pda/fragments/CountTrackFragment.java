package com.pda.birdex.pda.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.BaseActivity;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.CountingOrderNoInfoEntity;
import com.pda.birdex.pda.utils.SoftKeyboardUtil;
import com.pda.birdex.pda.utils.StringUtils;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/24.
 */
public class CountTrackFragment extends BarScanBaseFragment implements View.OnClickListener,RequestCallBackInterface{
    String tag="CountTrackFragment";
    @Bind(R.id.tv_count_num)
    TextView tv_count_num;
    @Bind(R.id.edt_count_now_no)
    ClearEditText edt_count_now_no;
    @Bind(R.id.edt_count_old_no)
    ClearEditText edt_count_old_no;
    @Bind(R.id.pll_count_num)
    PercentLinearLayout pll_count_num;//清点单号项，在清点中进入追踪箱号时，需要隐藏这一项

    CountingOrderNoInfoEntity countingOrderNoInfoEntity;//清点任务详情
    ContainerInfo containerInfo;//位置2进来时传进来的item
    Bundle b;
    String orderId="";
    String tid="";
    String owner ="";
    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_count_tool_track_layout;
    }

    @Override
    public void barInitializeContentViews() {
        b = getActivity().getIntent().getExtras();
        if(b!=null){
            if(bundle!=null && bundle.getString("location_position")==null) {
                countingOrderNoInfoEntity = (CountingOrderNoInfoEntity) b.get("countingOrderNoInfoEntity");
                containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
                if (countingOrderNoInfoEntity != null) {
                    orderId = countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getOrderNo();
                    owner = countingOrderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo();
                    tid = countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid();
                    tv_count_num.setText(countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getOrderNo());
                }
            }else{
                pll_count_num.setVisibility(View.GONE);
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
        edt_count_now_no.requestFocus();
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
        if (this.isVisible()) {
            SoftKeyboardUtil.hideSoftKeyboard((BaseActivity) getActivity());
            if(edt_count_now_no.hasFocus()){
                edt_count_old_no.requestFocus();
                return;
            }
            if(edt_count_old_no.hasFocus()){

            }
        }
    }

    @OnClick(R.id.btn_commit)
    @Override
    public void onClick(View v) {
        commit();
    }

    private void commit(){
        String oldContainerNo = edt_count_old_no.getText().toString();
        String newContainerNo = edt_count_now_no.getText().toString();
        if(StringUtils.isEmpty(oldContainerNo)){
            T.showShort(getActivity(),getString(R.string.count_old_toast));
            return;
        }
        if(StringUtils.isEmpty(newContainerNo)){
            T.showShort(getActivity(),getString(R.string.count_new_toast));
            return;
        }
        JSONObject object  = new JSONObject();
        try {
            object.put("oldContainerNo",oldContainerNo);
            object.put("owner",owner);
            object.put("newContainerNo",newContainerNo);
            BirdApi.jsonCountTrack(getActivity(), object, this, tag, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        BirdApi.cancelRequestWithTag(tag);
        super.onDestroy();
    }

    @Override
    public void successCallBack(JSONObject object) {
        //日志上报
        String oldContainerNo = edt_count_old_no.getText().toString();
        String newContainerNo = edt_count_now_no.getText().toString();
        MyApplication.loggingUpload.countTrack(getActivity(),tag,orderId,tid,oldContainerNo,newContainerNo);
        T.showShort(getActivity(),getString(R.string.commit_success));
    }

    @Override
    public void errorCallBack(JSONObject object) {
        T.showShort(getActivity(),getString(R.string.taking_submit_fal));
    }
}
