package com.pda.birdex.pda.activity;

import android.text.TextUtils;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/6/17.
 */
public class TakingBindActivity extends BarScanActivity implements View.OnClickListener {

    private static final String TAG = "TakingBindActivity";

    @Bind(R.id.no_et)
    com.pda.birdex.pda.widget.ClearEditText noEt;

    @Bind(R.id.code_et)
    com.pda.birdex.pda.widget.ClearEditText codeEt;

    @Bind(R.id.titleView)
    com.pda.birdex.pda.widget.TitleView titleView;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_bind;
    }

    @Override
    public void barInitializeContentViews() {
        titleView.setTitle(getString(R.string.areabind));

        noEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                noEt.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(noEt);
                }
            }
        });
        codeEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                codeEt.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(codeEt);
                }
            }
        });
    }

    @Override
    public ClearEditText getClearEditText() {
        return noEt;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }


    @OnClick({R.id.submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                // 点击提交按钮
                // 先判断是否为空
                if (TextUtils.isEmpty(noEt.getText())) {
                    T.showShort(this, getString(R.string.taking_no_empty));
                    return;
                }
                if (TextUtils.isEmpty(codeEt.getText())) {
                    T.showShort(this, getString(R.string.taking_area_empty));
                    return;
                }

                JSONObject jsonObject = new JSONObject();
                try {


                    // 调用绑定接口
//                    RequestParams params = new RequestParams();
                    jsonObject.put("containerNo", noEt.getText() + "");
                    jsonObject.put("areaCode", codeEt.getText() + "");
                    BirdApi.takingBind(this, jsonObject, new RequestCallBackInterface() {

                        @Override
                        public void successCallBack(JSONObject object) {
                            T.showShort(TakingBindActivity.this, getString(R.string.taking_bind_suc));
                        }

                        @Override
                        public void errorCallBack(JSONObject object) {
                            T.showShort(TakingBindActivity.this, getString(R.string.taking_bind_fal));
                            if (object != null) {
                                try {
                                    String errMsg = object.getString("errMsg");
                                    if (!TextUtils.isEmpty(errMsg))
                                        T.showShort(TakingBindActivity.this, errMsg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, TAG, true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}
