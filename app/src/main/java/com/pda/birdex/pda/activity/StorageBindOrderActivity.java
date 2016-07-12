package com.pda.birdex.pda.activity;

import android.view.View;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.utils.SoftKeyboardUtil;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/7/11.
 */
public class StorageBindOrderActivity extends BarScanActivity implements View.OnClickListener{
    String tag = "StorageBindOrderActivity";
    // 入库单号
    @Bind(R.id.edt_storage_num)
    ClearEditText edt_storage_num;

    // 容器号
    @Bind(R.id.edt_storage_container)
    ClearEditText edt_storage_container;

    @Bind(R.id.titleView)
    TitleView titleView;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_storage_bindorder;
    }

    @Override
    public void barInitializeContentViews() {
        titleView.setTitle(getString(R.string.storage_bind_order));

        edt_storage_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_storage_num.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_storage_num);
                }
            }
        });
        edt_storage_container.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_storage_container.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_storage_container);
                }
            }
        });
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_storage_num;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        SoftKeyboardUtil.hideSoftKeyboard(this);
        if (edt_storage_num.hasFocus()) {
            setEdt_input(edt_storage_container);
            edt_storage_container.requestFocus();
        } else {
            setEdt_input(edt_storage_num);
            edt_storage_num.requestFocus();
        }
    }

    private void commit(){
        JSONObject object = new JSONObject();
//        object.put("orderNO",stockNum);
//        object.put("containers",)
        BirdApi.postStockInbBndOrderBat(this, object, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                T.showShort(StorageBindOrderActivity.this, getString(R.string.taking_bind_suc));
            }

            @Override
            public void errorCallBack(JSONObject object) {
                T.showShort(StorageBindOrderActivity.this, getString(R.string.taking_bind_fal));
            }
        }, tag, true);
    }

    @OnClick(R.id.btn_commit)
    @Override
    public void onClick(View v) {
        commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
}
