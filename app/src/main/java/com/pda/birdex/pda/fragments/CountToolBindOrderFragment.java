package com.pda.birdex.pda.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.BindNumAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.BindOrder;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.entity.TakingOrder;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/7/6.
 */
public class CountToolBindOrderFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "CountToolBindOrderFragment";

    private BindNumAdapter adapter;

    @Bind(R.id.xrcy)
    com.jcodecraeer.xrecyclerview.XRecyclerView xrcy;

    @Bind(R.id.edt_count_container)
    com.pda.birdex.pda.widget.ClearEditText edt_count_container;

    @Bind(R.id.edt_count_num)
    com.pda.birdex.pda.widget.ClearEditText edt_count_num;

    @Bind(R.id.tv_count_num)
    TextView tv_count_num;
    TakingOrderNoInfoEntity orderNoInfoEntity;//位置2进来传来的实体
    ContainerInfo containerInfo;//位置2进来时传进来的item
    // 容器 list
    private List<String> containerList = new ArrayList<>();

    private String from;
    private String owner;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_count_tool_bindorder_layout;
    }

    @Override
    public void barInitializeContentViews() {

        from = getActivity().getIntent().getExtras().getString("location_position");
//        if ("1".equals(from)) {
//            takingOrder = (TakingOrder) getActivity().getIntent().getExtras().get("takingOrder");
//            tv_count_num.setText(takingOrder.getBaseInfo().getTakingOrderNo());
//            owner = takingOrder.getPerson().getCo();
//        } else {//打印数量
        orderNoInfoEntity = (TakingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("orderNoInfoEntity");
        containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
        tv_count_num.setText(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
        owner = orderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo();
//        }

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
    }

    private void inputEntry(String input) {
        if (!containerList.contains(input.trim())) {
            containerList.add(input);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_count_container;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if (this.isVisible()) {
            inputEntry(code);
        }
    }


    @OnClick({R.id.btn_commit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击提交
            case R.id.btn_commit:
                if (TextUtils.isEmpty(edt_count_container.getText())) {
                    T.showShort(getContext(), R.string.count_num_toast);
                    return;
                }
                if (containerList.size() > 0) {
                    RequestParams params = new RequestParams();
//                    params.put("tkNo", tv_taking_num.getText() + "");
//                    params.put("code", containerList);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        String str = GsonHelper.createJsonString(containerList);
//                        jsonObject.put("containerConfig",str);

                        JSONArray object = new JSONArray(str);
                        jsonObject.put("orderNO", tv_count_num.getText() + "");
                        jsonObject.put("containers", object);

                        BirdApi.jsonCountBindorderSubmit(getContext(), jsonObject, new RequestCallBackInterface() {

                            @Override
                            public void successCallBack(JSONObject object) {
                                T.showShort(getContext(), getString(R.string.taking_submit_suc));
                            }

                            @Override
                            public void errorCallBack(JSONObject object) {
                                T.showShort(getContext(), getString(R.string.taking_submit_fal));
                            }
                        }, tag, true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    T.showShort(getContext(), getString(R.string.taking_bind_no));
                }
                break;
        }
    }
}
