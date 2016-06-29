package com.pda.birdex.pda.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.TakingToolActivity;
import com.pda.birdex.pda.adapter.BindNumAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/25.
 */
public class TakingToolBindNumFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "TakingToolBindNumFragment";

    private BindNumAdapter adapter;

    @Bind(R.id.xrcy)
    com.jcodecraeer.xrecyclerview.XRecyclerView xrcy;

    @Bind(R.id.edt_taking_num)
    com.pda.birdex.pda.widget.ClearEditText edt_taking_num;

    @Bind(R.id.tv_taking_num)
    TextView tv_taking_num;

    // 容器 list
    private List<String> containerList = new ArrayList<>();

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_taking_tool_bindnum_layout;
    }

    @Override
    public void barInitializeContentViews() {

        if(TakingToolActivity.takingOrderNo != null) {
            tv_taking_num.setText(TakingToolActivity.takingOrderNo);
        }

        xrcy.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BindNumAdapter(getContext(), containerList);
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

        edt_taking_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!TextUtils.isEmpty(edt_taking_num.getText())) {
                        String input = edt_taking_num.getText() + "";
                        inputEntry(input);
                    }
                }
                return false;
            }
        });

        edt_taking_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    inputEntry(edt_taking_num.getText() + "");
                }
            }
        });
    }

    private void inputEntry(String input){
        if (!containerList.contains(input.trim())) {
            containerList.add(input);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_taking_num;
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
                if (containerList.size() > 0) {
                    RequestParams params = new RequestParams();
                    params.put("tkNo", tv_taking_num.getText() + "");
                    params.put("code", containerList);

                    BirdApi.takingBindorderSubmit(getContext(), params, new RequestCallBackInterface() {

                        @Override
                        public void successCallBack(JSONObject object) {
                            try {
                                if ("success".equals(object.getString("result"))) {
                                    T.showShort(getContext(), getString(R.string.taking_submit_suc));
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
                    }, tag, true);
                } else {
                    T.showShort(getContext(), getString(R.string.taking_bind_no));
                }
                break;
        }
    }
}
