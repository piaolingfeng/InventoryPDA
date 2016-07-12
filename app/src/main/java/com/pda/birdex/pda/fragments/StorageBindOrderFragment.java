package com.pda.birdex.pda.fragments;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.utils.GsonHelper;
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

    public static final String TAG = "StorageBindOrderFragment";

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

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_storage_bindorder_layout;
    }

    @Override
    public void barInitializeContentViews() {
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
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
    }

    private void disEnableEditMode() {
        btn_edit.setVisibility(View.VISIBLE);
        edt_storage_order.setVisibility(View.INVISIBLE);
        tv_storage_order.setVisibility(View.VISIBLE);
        btn_commit.setClickable(false);
        btn_commit.setBackgroundResource(R.drawable.rect_fullgray);
        btn_commit.setTextColor(getResources().getColor(R.color.white));
    }

    // 调用上传接口
    private void commit() {
        JSONObject jsonObject = new JSONObject();
        try {
            List<String> containers = new ArrayList<>();
            containers.add(tv_vessel_num.getText() + "");
            String containerStr = GsonHelper.createJsonString(containers);
            JSONArray array = new JSONArray(containerStr);
            jsonObject.put("containers", array);
            jsonObject.put("orderNO", edt_storage_order.getText() + "");
            BirdApi.postStockBindOrder(getContext(), jsonObject, new RequestCallBackInterface(){

                @Override
                public void successCallBack(JSONObject object) {
                    try {
                        if ("success".equals(object.getString("result"))) {
                            T.showShort(getContext(), getString(R.string.taking_submit_suc));
                            disEnableEditMode();
                        } else {
                            T.showShort(getContext(), getString(R.string.taking_submit_fal));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void errorCallBack(JSONObject object) {
                    T.showShort(getContext(), getString(R.string.taking_submit_fal));
                }
            }, TAG, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_commit, R.id.btn_edit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                if (TextUtils.isEmpty(tv_vessel_num.getText())) {
                    T.showShort(getContext(), getString(R.string.count_vessel_empty));
                    return;
                }
                if (TextUtils.isEmpty(edt_storage_order.getText())) {
                    T.showShort(getContext(), getString(R.string.storage_order_empty));
                    return;
                }
                commit();
                break;
            case R.id.btn_edit:
                editMode();
                break;
        }
    }
}
