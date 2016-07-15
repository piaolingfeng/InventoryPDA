package com.pda.birdex.pda.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.BaseActivity;
import com.pda.birdex.pda.adapter.BindNumAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.BindOrder;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.entity.TakingOrder;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.SoftKeyboardUtil;
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
 * Created by chuming.zhuang on 2016/6/25.
 */
public class TakingBindOrderFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "TakingBindOrderFragment";

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
    String tid = "";
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
            if (takingOrder != null) {
                tv_taking_num.setText(takingOrder.getBaseInfo().getTakingOrderNo());
                owner = takingOrder.getPerson().getCo();
            }
            edt_taking_container.requestFocus();
        } else {//打印数量
            if ("2".equals(from)) {
                containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
                orderNoInfoEntity = (TakingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("orderNoInfoEntity");
                if (orderNoInfoEntity != null) {
                    tv_taking_num.setText(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
                    owner = orderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo();
                }
                edt_taking_container.requestFocus();
            } else {
                edt_taking_num.setVisibility(View.VISIBLE);
                edt_taking_num.requestFocus();
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
        if("1".equals(from)||"2".equals(from))
            return edt_taking_container;
        else
            return edt_taking_num;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if (this.isVisible()) {
            SoftKeyboardUtil.hideSoftKeyboard((BaseActivity) getActivity());
            if (edt_taking_container.hasFocus()||getEdt_input() == edt_taking_container) {
                inputEntry(code);
                edt_taking_container.requestFocus();
            }
            if(edt_taking_num.hasFocus()){
                edt_taking_container.requestFocus();
            }
        }
    }


    @OnClick({R.id.btn_commit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击提交
            case R.id.btn_commit:
                commitBindOrder();
                break;
        }
    }

    private void commitBindOrder() {
        if (containerList.size() > 0) {
            List<BindOrder> containerConfig = new ArrayList<>();
            for (String code : containerList) {
                BindOrder bo = new BindOrder();
                bo.setContainerNo(code);
                bo.setOwner(owner);
                containerConfig.add(bo);
            }
            JSONObject jsonObject = new JSONObject();

            try {
                String str = GsonHelper.createJsonString(containerConfig);
                JSONArray jsonArray = new JSONArray(str);
                jsonObject.put("containerConfig", jsonArray);
                if ("1".equals(from)) {
                    tid = takingOrder.getBaseInfo().getTid();
                    jsonObject.put("tid", tid);
                } else if ("2".equals(from)) {
                    tid = orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid();
                    jsonObject.put("tid", tid);
                } else {
                    /**
                     * 首页有个绑定揽收单，这里是该页面tid入口
                     * */

                    if (StringUtils.isEmpty(edt_taking_num.getText().toString())) {
                        T.showShort(getActivity(), getString(R.string.tv_taking_num_1) + getString(R.string.not_null));
                        return;
                    }
                    jsonObject = new JSONObject();
                    String orderId = edt_taking_num.getText().toString();//揽收单号
                    jsonObject.put("orderNO", orderId);
                    jsonArray = new JSONArray(containerList);
                    jsonObject.put("containers", jsonArray);
                }
                if ("1".equals(from) || "2".equals(from))
                    BirdApi.jsonTakingBindorderSubmit(getContext(), jsonObject, callBackInterface, tag, true);
                else {
                    BirdApi.jsonTakingBindorderBatSubmit(getContext(), jsonObject, callBackInterface, tag, true);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            T.showShort(getContext(), getString(R.string.taking_bind_no));
        }
    }

    RequestCallBackInterface callBackInterface = new RequestCallBackInterface() {

        @Override
        public void successCallBack(JSONObject object) {
            T.showShort(getContext(), getString(R.string.taking_submit_suc));
            //日志上报,
            String orderId = tv_taking_num.getText().toString();
            try {
                tid = object.getString("tid");//收货的绑定揽收单，获取到接口时会返回tid
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MyApplication.loggingUpload.takeBindOrder(getActivity(), tag, orderId, tid, containerList);
        }

        @Override
        public void errorCallBack(JSONObject object) {
            T.showShort(getContext(), getString(R.string.taking_submit_fal));
        }
    };
}
