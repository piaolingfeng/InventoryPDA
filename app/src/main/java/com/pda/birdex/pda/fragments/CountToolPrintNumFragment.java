package com.pda.birdex.pda.fragments;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.CountingOrderNoInfoEntity;
import com.pda.birdex.pda.response.PrintEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/24.
 */
public class CountToolPrintNumFragment extends BarScanBaseFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    String tag = "CountToolPrintNumFragment";
    @Bind(R.id.tv_count_num)
    TextView tv_count_num;
    @Bind(R.id.edt_taking_num)
    ClearEditText edt_taking_num;
    CountingOrderNoInfoEntity countingOrderNoInfoEntity;//清点任务详情
    ContainerInfo containerInfo;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_count_tool_print_layout;
    }

    @Override
    public void barInitializeContentViews() {
        countingOrderNoInfoEntity = (CountingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("countingOrderNoInfoEntity");
        if (countingOrderNoInfoEntity != null) {
            tv_count_num.setText(countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getOrderNo());
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

    @OnClick(R.id.edt_taking_num)
    @Override
    public void onClick(View v) {
        print();
    }

    private void print() {
        if(countingOrderNoInfoEntity == null)
            return;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("count", 1);
            jsonObject.put("owner", countingOrderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo());
            jsonObject.put("tkNo", countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getOrderNo());
            BirdApi.postCountingCodePrint(getActivity(), jsonObject, new RequestCallBackInterface() {
                @Override
                public void successCallBack(JSONObject object) {
                    PrintEntity entity = GsonHelper.getPerson(object.toString(), PrintEntity.class);
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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_UNSPECIFIED == actionId) {
            print();
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
}
