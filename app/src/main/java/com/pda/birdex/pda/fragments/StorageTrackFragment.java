package com.pda.birdex.pda.fragments;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.StockInContainerInfoEntity;
import com.pda.birdex.pda.utils.StringUtils;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/7/8.
 * 追踪箱号
 */
public class StorageTrackFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "StorageTrackFragment";
    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;
    @Bind(R.id.edt_vessel_num)
    ClearEditText edt_vessel_num;
    @Bind(R.id.btn_edit)
    Button btn_edit;
    @Bind(R.id.edt_storage_old_no)
    ClearEditText edt_storage_old_no;
    @Bind(R.id.tv_storage_old_no)
    TextView tv_storage_old_no;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    String location_position;//判断入口
    StockInContainerInfoEntity entity;
    String stockNum;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_storage_track_layout;
    }

    @Override
    public void barInitializeContentViews() {

        location_position = bundle.getString("location_position");
        if ("SecondIndex".equals(location_position)) {//从入库的首页进来
            edt_vessel_num.setVisibility(View.VISIBLE);
            tv_vessel_num.setVisibility(View.INVISIBLE);
        } else {//从查看容器进来
            entity = (StockInContainerInfoEntity) bundle.getSerializable("StockInContainerInfoEntity");
            stockNum = bundle.getString("stockNum");
            tv_vessel_num.setText(stockNum);
            if (StringUtils.isEmpty(entity.getOldNo())) {//未提交过
                editMode();
            } else {
                disEnableEditMode();
                tv_storage_old_no.setText(entity.getOrderNo());
            }
        }
        edt_storage_old_no.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    String string = v.getText().toString();
                    ClearEditTextCallBack(string);
                }
                return false;
            }
        });
    }

    @Override
    public ClearEditText getClearEditText() {
        if ("SecondIndex".equals(location_position)) {
            return edt_vessel_num;
        }
        return edt_storage_old_no;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if (this.isVisible) {
            if (getEdt_input() == edt_vessel_num && "SecondIndex".equals(location_position)) {//入库首页入口进入
                edt_storage_old_no.requestFocus();
                return;
            }
            if (edt_storage_old_no.getVisibility() == View.VISIBLE) {//工具包进入

            }
        }
    }

    private void editMode() {
        btn_edit.setVisibility(View.INVISIBLE);
        edt_storage_old_no.setVisibility(View.VISIBLE);
        tv_storage_old_no.setVisibility(View.INVISIBLE);
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
    }

    private void disEnableEditMode() {
        btn_edit.setVisibility(View.VISIBLE);
        edt_storage_old_no.setVisibility(View.INVISIBLE);
        tv_storage_old_no.setVisibility(View.VISIBLE);
        btn_commit.setClickable(false);
        btn_commit.setBackgroundResource(R.drawable.rect_fullgray);
        btn_commit.setTextColor(getResources().getColor(R.color.white));
    }

    private void commit() {
        JSONObject object = new JSONObject();
        try {
            object.put("oldContainerNo", edt_storage_old_no.getText().toString());
            if ("SecondIndex".equals(location_position)) {//从入库的首页进来
                object.put("newContainerNo", edt_vessel_num.getText().toString());
            } else {
                object.put("newContainerNo", tv_vessel_num.getText().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BirdApi.postStockInTrack(getActivity(), object, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                T.showShort(getActivity(), getString(R.string.taking_submit_suc));
            }

            @Override
            public void errorCallBack(JSONObject object) {
                T.showShort(getActivity(), getString(R.string.taking_submit_fal));
            }
        }, tag, true);
    }

    @OnClick({R.id.btn_commit, R.id.btn_edit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                commit();
//                disEnableEditMode();
                break;
            case R.id.btn_edit:
                editMode();
                break;
        }
    }
}
