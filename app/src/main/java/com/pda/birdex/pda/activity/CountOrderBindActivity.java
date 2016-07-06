package com.pda.birdex.pda.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.BindNumAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
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
 * Created by hyj on 2016/7/6.
 */
public class CountOrderBindActivity extends BarScanActivity implements View.OnClickListener{
    String tag = "CountOrderBindActivity";

    private BindNumAdapter adapter;

    @Bind(R.id.title)
    com.pda.birdex.pda.widget.TitleView titleView;

    @Bind(R.id.xrcy)
    com.jcodecraeer.xrecyclerview.XRecyclerView xrcy;

    @Bind(R.id.edt_count_container)
    com.pda.birdex.pda.widget.ClearEditText edt_count_container;

    @Bind(R.id.edt_count_num)
    com.pda.birdex.pda.widget.ClearEditText edt_count_num;

    // 容器 list
    private List<String> containerList = new ArrayList<>();

    private String owner;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_count_tool_bindorder_layout;
    }

    @Override
    public void barInitializeContentViews() {
        titleView.setTitle(getString(R.string.count_bind));

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

        edt_count_container.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!TextUtils.isEmpty(edt_count_container.getText())) {
                        String input = edt_count_container.getText() + "";
                        inputEntry(input);
                    }
                }
                return false;
            }
        });

        edt_count_container.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    inputEntry(edt_count_container.getText() + "");
                }
            }
        });

        edt_count_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_count_num.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_count_num);
                }
            }
        });
        edt_count_container.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_count_container.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_count_container);
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
        return edt_count_num;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        inputEntry(code);
    }


    @OnClick({R.id.btn_commit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击提交
            case R.id.btn_commit:
                if (TextUtils.isEmpty(edt_count_num.getText())) {
                    T.showShort(this, R.string.count_order_toast);
                    return;
                }
                if (containerList.size() > 0) {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        String str = GsonHelper.createJsonString(containerList);
//                        jsonObject.put("containerConfig",str);

                        JSONArray object = new JSONArray(str);
                        jsonObject.put("orderNO", edt_count_num.getText() + "");
                        jsonObject.put("containers", object);

                        BirdApi.jsonCountBindorderSubmit(this, jsonObject, new RequestCallBackInterface() {

                            @Override
                            public void successCallBack(JSONObject object) {
                                T.showShort(getApplication(), getString(R.string.taking_submit_suc));
                            }

                            @Override
                            public void errorCallBack(JSONObject object) {
                                T.showShort(getApplication(), getString(R.string.taking_submit_fal));
                            }
                        }, tag, true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    T.showShort(this, getString(R.string.taking_bind_no));
                }
                break;
        }
    }
}
