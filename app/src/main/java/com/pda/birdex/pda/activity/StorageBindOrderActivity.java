package com.pda.birdex.pda.activity;

import android.nfc.Tag;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.BindNumAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.SoftKeyboardUtil;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/7/11.
 */
public class StorageBindOrderActivity extends BarScanActivity implements View.OnClickListener {

    private static final String TAG = "StorageBindOrderActivity";

    // 入库单号
    @Bind(R.id.edt_storage_num)
    ClearEditText edt_storage_num;

    // 容器号
    @Bind(R.id.edt_storage_container)
    ClearEditText edt_storage_container;

    @Bind(R.id.titleView)
    TitleView titleView;

    @Bind(R.id.xrcy)
    XRecyclerView xrcy;

    // 容器 list
    private List<String> containerList = new ArrayList<>();

    private BindNumAdapter adapter;

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

        xrcy.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BindNumAdapter(this, containerList);
        xrcy.setAdapter(adapter);

        adapter.setOnRecyclerViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (containerList.size() > position) {
                    containerList.remove(containerList.get(position));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void inputEntry(String input) {
        if (!containerList.contains(input.trim())) {
            containerList.add(input);
            adapter.notifyDataSetChanged();
        }
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
        if (edt_storage_container.hasFocus() && (!TextUtils.isEmpty(code.trim()))) {
            inputEntry(code);
        }
    }

    private void upload(){
        JSONObject jsonObject = new JSONObject();
        try {
            String containerStr = GsonHelper.createJsonString(containerList);
            JSONArray array = new JSONArray(containerStr);
            jsonObject.put("containers", array);
            jsonObject.put("orderNO", edt_storage_num.getText() + "");
            BirdApi.postStockBindOrder(this, jsonObject, new RequestCallBackInterface() {

                @Override
                public void successCallBack(JSONObject object) {
                    try {
                        if ("success".equals(object.getString("result"))) {
                            T.showShort(getApplication(), getString(R.string.taking_submit_suc));
                        } else {
                            T.showShort(getApplication(), getString(R.string.taking_submit_fal));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void errorCallBack(JSONObject object) {
                    T.showShort(getApplication(), getString(R.string.taking_submit_fal));
                }
            }, TAG, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_commit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                if (TextUtils.isEmpty(edt_storage_num.getText())) {
                    T.showShort(this, getString(R.string.storage_order_empty));
                    return;
                }
                if (containerList.size() == 0) {
                    T.showShort(this, getString(R.string.count_vessel_empty));
                    return;
                }
                // 调用接口进行提交操作
                upload();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(TAG);
    }
}
