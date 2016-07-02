package com.pda.birdex.pda.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
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

import org.apache.http.Header;
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
public class TakingToolBindNumFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "TakingToolBindNumFragment";

    private BindNumAdapter adapter;

    @Bind(R.id.xrcy)
    com.jcodecraeer.xrecyclerview.XRecyclerView xrcy;

    @Bind(R.id.edt_taking_num)
    com.pda.birdex.pda.widget.ClearEditText edt_taking_num;

    @Bind(R.id.tv_taking_num)
    TextView tv_taking_num;
    TakingOrder takingOrder;//位置1进来传来的实体
    TakingOrderNoInfoEntity orderNoInfoEntity;//位置2进来传来的实体
    ContainerInfo containerInfo;//位置2进来时传进来的item
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

        if (getActivity().getIntent().getExtras().getString("location_position").equals("1")) {
        from = getActivity().getIntent().getExtras().getString("location");
        if ("1".equals(from)) {
            takingOrder = (TakingOrder) getActivity().getIntent().getExtras().get("takingOrder");
            tv_taking_num.setText(takingOrder.getBaseInfo().getTakingOrderNo());
            owner = takingOrder.getPerson().getCo();
        } else {//打印数量
            orderNoInfoEntity = (TakingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("orderNoInfoEntity");
            containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
            tv_taking_num.setText(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
            owner = orderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo();
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
                if (!hasFocus) {
                    inputEntry(edt_taking_num.getText() + "");
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
        return edt_taking_num;
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
                    for(String code: containerList){
                        BindOrder bo = new BindOrder();
                        bo.setCode(code);
                        bo.setOwner(owner);
                        containerConfig.add(bo);
                    }
//                    BindOrder bo = new BindOrder();
////                        bo.setCode(code);
////                        bo.setOwner(owner);
//                    bo.setOwner("MET");
//                    bo.setCode("CT-160630000010");
//                    containerConfig.add(bo);

//                    String t = GsonHelper.createJsonString(containerConfig);
//                    params.put("containerConfig", containerConfig);
//                    if ("1".equals(from)) {
//                        params.put("tid", takingOrder.getBaseInfo().getTid());
//                    } else {
//                        params.put("tid", orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid());
//                    }
                    params.put("tid", "MET:TK-160630000003");

                    JSONObject jsonObject = new JSONObject();
                    try {
                        String str = GsonHelper.createJsonString(containerConfig);
//                        jsonObject.put("containerConfig",str);

                        JSONArray object = new JSONArray(str);
                        jsonObject.put("containerConfig",object);
                        if ("1".equals(from)) {
                            jsonObject.put("tid", takingOrder.getBaseInfo().getTid());
                        } else {
                            jsonObject.put("tid", orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid());
                        }
//                        jsonObject.put("tid", "MET:TK-160630000003");
                        StringEntity stringEntity = new StringEntity(jsonObject.toString());
//                        stringEntity.setContentType("application/json");

                        BirdApi.jsonTakingBindorderSubmit(getContext(), jsonObject,new RequestCallBackInterface(){

                            @Override
                            public void successCallBack(JSONObject object) {
                                T.showShort(getContext(), getString(R.string.taking_submit_suc));
                            }

                            @Override
                            public void errorCallBack(JSONObject object) {
                                T.showShort(getContext(), getString(R.string.taking_submit_fal));
                            }
                        },tag, true);

//                        BirdApi.jsonPost(getContext(),"code/bindOrder",stringEntity, new JsonHttpResponseHandler(){
//
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                                super.onSuccess(statusCode, headers, response);
//                            }
//
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                                super.onSuccess(statusCode, headers, response);
//                            }
//
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                                super.onSuccess(statusCode, headers, responseString);
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                                super.onFailure(statusCode, headers, responseString, throwable);
//                            }
//
//                            @Override
//                            public void onFinish() {
//                                super.onFinish();
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                                super.onFailure(statusCode, headers, throwable, errorResponse);
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                                super.onFailure(statusCode, headers, throwable, errorResponse);
//                            }
//                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }





//                    BirdApi.takingBindorderSubmit(getContext(), params, new RequestCallBackInterface() {
//
//                        @Override
//                        public void successCallBack(JSONObject object) {
//                            try {
//                                if ("success".equals(object.getString("result"))) {
//                                    T.showShort(getContext(), getString(R.string.taking_submit_suc));
//                                } else {
//                                    T.showShort(getContext(), getString(R.string.taking_submit_fal));
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void errorCallBack(JSONObject object) {
//                            T.showShort(getContext(), getString(R.string.taking_submit_fal));
//                        }
//                    }, tag, true);
                } else {
                    T.showShort(getContext(), getString(R.string.taking_bind_no));
                }
                break;
        }
    }
}
