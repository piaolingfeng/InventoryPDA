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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/7/8.
 * 绑定入库单
 */
public class StorageBindOrderFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "StorageBindOrderFragment";
    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;
    @Bind(R.id.edt_storage_order)
    ClearEditText edt_storage_order;
    @Bind(R.id.tv_storage_order)
    TextView tv_storage_order;
    @Bind(R.id.btn_edit)
    Button btn_edit;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    StockInContainerInfoEntity entity;
    String stockNum;//容器号

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_storage_bindorder_layout;
    }

    @Override
    public void barInitializeContentViews() {
        bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            entity = (StockInContainerInfoEntity) bundle.getSerializable("StockInContainerInfoEntity");
            stockNum = bundle.getString("stockNum");
        }
        tv_vessel_num.setText(stockNum + "");
        if (entity != null && !StringUtils.isEmpty(entity.getOrderNo())) {//异常，不能解绑
            disableEditMode();
            tv_storage_order.setText(entity.getOrderNo());
        } else {
            editMode();
        }

        edt_storage_order.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        return edt_storage_order;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if (this.isVisible) {

        }
    }

    private void editMode() {
        btn_edit.setVisibility(View.INVISIBLE);
        edt_storage_order.setVisibility(View.VISIBLE);
        tv_storage_order.setVisibility(View.INVISIBLE);
        edt_storage_order.setText(tv_storage_order.getText());
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
    }

    private void disableEditMode() {
        btn_edit.setVisibility(View.VISIBLE);
        edt_storage_order.setVisibility(View.INVISIBLE);
        tv_storage_order.setVisibility(View.VISIBLE);
        tv_storage_order.setText(edt_storage_order.getText().toString());
        btn_commit.setClickable(false);
        btn_commit.setBackgroundResource(R.drawable.rect_fullgray);
        btn_commit.setTextColor(getResources().getColor(R.color.white));
    }

    private void commit() {
        JSONObject object = new JSONObject();
        String ss = edt_storage_order.getText().toString();
        List<String> list = new ArrayList<>();
        list.add(stockNum);
        try {
            object.put("orderNO",ss);
            JSONArray array = new JSONArray(list);
            object.put("containers",array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BirdApi.postStockInbBndOrderBat(getActivity(), object, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                T.showShort(getActivity(), getString(R.string.taking_bind_suc));
                disableEditMode();
            }

            @Override
            public void errorCallBack(JSONObject object) {
                T.showShort(getActivity(), getString(R.string.taking_bind_fal));
            }
        }, tag, true);
    }

    @OnClick({R.id.btn_commit, R.id.btn_edit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                commit();
                break;
            case R.id.btn_edit:
                editMode();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
}
