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
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.CountingOrderNoInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.HideSoftKeyboardUtil;
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
    CountingOrderNoInfoEntity countingOrderNoInfoEntity;//清点任务详情
    ContainerInfo containerInfo;//位置2进来时传进来的item
    // 容器 list
    private List<String> containerList = new ArrayList<>();

    private String owner;
    private String orderId="";

    // 标记位， 1：从首页进来的  2：从清点任务进来的
    private int index;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_count_tool_bindorder_layout;
    }

    @Override
    public void barInitializeContentViews() {

        if(bundle!=null && bundle.getString("location_position")!=null){//清点首页进来的绑定清点单
            index = 1;
            edt_count_num.setVisibility(View.VISIBLE);
            tv_count_num.setVisibility(View.GONE);
            edt_count_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    edt_count_num.overrideOnFocusChange(hasFocus);
                    if (hasFocus) {
                        setEdt_input(edt_count_num);
                    }
                }
            });
        }else{//清点任务进来的绑单
            index = 2;
            countingOrderNoInfoEntity = (CountingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("countingOrderNoInfoEntity");
            containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
            if (countingOrderNoInfoEntity != null) {
                orderId = countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getOrderNo();
                tv_count_num.setText(orderId);
                owner = countingOrderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo();
            }
        }

        edt_count_container.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_count_container.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_count_container);
                }
//                if (!hasFocus) {
//                    inputEntry(edt_count_container.getText() + "");
//                }
            }
        });

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
//        edt_count_container.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    inputEntry(edt_count_container.getText() + "");
//                }
//            }
//        });
    }

    private void inputEntry(String input) {
        HideSoftKeyboardUtil.hideSoftKeyboard((BaseActivity) getActivity());//隐藏软键盘
        if(!TextUtils.isEmpty(input)) {
            if (!containerList.contains(input.trim())) {
                containerList.add(input);
                adapter.notifyDataSetChanged();
            }
        }
    }

//    @Override
//    public void onKeyDown(int keyCode, KeyEvent event) {
////        super.onKeyDown(keyCode, event);
//        if(keyCode == KeyEvent.KEYCODE_ENTER){
//            inputEntry(edt_count_container.getText() + "");
//        }
//    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_count_container;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if (this.isVisible()) {
            HideSoftKeyboardUtil.hideSoftKeyboard((BaseActivity) getActivity());
            inputEntry(code);
        }
    }


    @OnClick({R.id.btn_commit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击提交
            case R.id.btn_commit:
//                if (TextUtils.isEmpty(edt_count_container.getText())) {
//                    T.showShort(getContext(), R.string.count_num_toast);
//                    return;
//                }
                commit();
                break;
        }
    }

    private void commit(){
        if (containerList.size() > 0) {

            JSONObject jsonObject = new JSONObject();
            try {
                String str = GsonHelper.createJsonString(containerList);
//                        jsonObject.put("containerConfig",str);

                JSONArray object = new JSONArray(str);
                if(index == 1){
                    //从首页进来的
                    jsonObject.put("orderNO", edt_count_num.getText() + "");
                } else {
                    // 从清点任务进来的
                    jsonObject.put("orderNO", tv_count_num.getText() + "");
                }
                jsonObject.put("containers", object);

                BirdApi.jsonCountBindorderSubmit(getContext(), jsonObject, new RequestCallBackInterface() {

                    @Override
                    public void successCallBack(JSONObject object) {
                        //绑定清点单日志上报
                        String tid ="";
                        try {
                            tid = object.getString("tid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MyApplication.loggingUpload.countBindOrder(getActivity(),tag,orderId,tid,containerList);
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
    }
}
