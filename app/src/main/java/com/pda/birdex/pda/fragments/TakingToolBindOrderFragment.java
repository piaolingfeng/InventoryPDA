package com.pda.birdex.pda.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.MyApplication;
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
import com.pda.birdex.pda.utils.StringUtils;
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
 * Created by chuming.zhuang on 2016/6/25.
 */
public class TakingToolBindOrderFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "TakingToolBindOrderFragment";

    private BindNumAdapter adapter;

    @Bind(R.id.xrcy)
    com.jcodecraeer.xrecyclerview.XRecyclerView xrcy;

    @Bind(R.id.edt_taking_container)
    com.pda.birdex.pda.widget.ClearEditText edt_taking_container;

    @Bind(R.id.edt_taking_num)
    com.pda.birdex.pda.widget.ClearEditText edt_taking_num;

    @Bind(R.id.tv_taking_num)
    TextView tv_taking_num;
    TakingOrder takingOrder;//位置1进来传来的实体
    TakingOrderNoInfoEntity orderNoInfoEntity;//位置2进来传来的实体
    ContainerInfo containerInfo;//位置2进来时传进来的item
    String tid ="";
    // 容器 list
    private List<String> containerList = new ArrayList<>();

    private String from;
    private String owner;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_taking_tool_bindnum_layout;
    }

    @Override
    public void barInitializeContentViews() {

        from = getActivity().getIntent().getExtras().getString("location_position");
        if ("1".equals(from)) {
            takingOrder = (TakingOrder) getActivity().getIntent().getExtras().get("takingOrder");
            tv_taking_num.setText(takingOrder.getBaseInfo().getTakingOrderNo());
            owner = takingOrder.getPerson().getCo();
        } else {//打印数量
            if ("2".equals(from)) {
                orderNoInfoEntity = (TakingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("orderNoInfoEntity");
                containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
                tv_taking_num.setText(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
                owner = orderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo();
            } else {
                edt_taking_num.setVisibility(View.VISIBLE);
                tv_taking_num.setVisibility(View.GONE);
            }
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

        edt_taking_container.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!TextUtils.isEmpty(edt_taking_container.getText())) {
                        String input = edt_taking_container.getText() + "";
                        inputEntry(input);
                    }
                }
                return false;
            }
        });

        edt_taking_container.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    inputEntry(edt_taking_container.getText() + "");
                } else {
                    edt_taking_container.overrideOnFocusChange(hasFocus);
                    setEdt_input(edt_taking_container);
                }
            }
        });

        edt_taking_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_taking_num.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_taking_num);
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
        return edt_taking_container;
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
                if (containerList.size() > 0) {
                    RequestParams params = new RequestParams();
//                    params.put("tkNo", tv_taking_num.getText() + "");
//                    params.put("code", containerList);
                    List<BindOrder> containerConfig = new ArrayList<>();
                    for (String code : containerList) {
                        BindOrder bo = new BindOrder();
                        bo.setCode(code);
                        bo.setOwner(owner);
                        containerConfig.add(bo);
                    }

                    JSONObject jsonObject = new JSONObject();
                    try {
                        String str = GsonHelper.createJsonString(containerConfig);
//                        jsonObject.put("containerConfig",str);

                        JSONArray object = new JSONArray(str);
                        jsonObject.put("containerConfig", object);
                        if ("1".equals(from)) {
                            tid = takingOrder.getBaseInfo().getTid();
                            jsonObject.put("tid", tid);
                        } else if ("2".equals(from)) {
                            tid  = orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid();
                            jsonObject.put("tid", tid);
                        } else {
                            /**
                             * 首页有个绑定揽收单，这里是该页面tid入口
                             * */
                            if (StringUtils.isEmpty(edt_taking_num.getText().toString())) {
                                T.showShort(getActivity(), getString(R.string.tv_taking_num_1) + getString(R.string.not_null));
                                return;
                            }
                            jsonObject.put("tid", edt_taking_num.getText().toString());
                        }
//                        jsonObject.put("tid", "MET:TK-160630000003");
                        StringEntity stringEntity = new StringEntity(jsonObject.toString());
//                        stringEntity.setContentType("application/json");

                        if ("1".equals(from) || "2".equals(from))
                            BirdApi.jsonTakingBindorderSubmit(getContext(), jsonObject, callBackInterface, tag, true);
                        else {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    T.showShort(getContext(), getString(R.string.taking_bind_no));
                }
                break;
        }
    }

    RequestCallBackInterface callBackInterface = new RequestCallBackInterface() {

        @Override
        public void successCallBack(JSONObject object) {
            T.showShort(getContext(), getString(R.string.taking_submit_suc));
            //上报日志,
            String orderId = tv_taking_num.getText().toString();
            try {
                tid = object.getString("tid");//收货的绑定揽收单，获取到接口时会返回tid
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MyApplication.loggingUpload.bindOrder(getActivity(), tag,orderId,tid, containerList);
        }

        @Override
        public void errorCallBack(JSONObject object) {
            T.showShort(getContext(), getString(R.string.taking_submit_fal));
        }
    };
}
