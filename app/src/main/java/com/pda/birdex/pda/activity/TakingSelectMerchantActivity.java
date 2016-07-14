package com.pda.birdex.pda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.SpinnerDropAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.Merchant;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.StringUtils;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/30.
 */
public class TakingSelectMerchantActivity extends BaseActivity implements OnClickListener {
    String tag = "TakingSelectMerchantActivity";
    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.spin_bussiness)
    AppCompatSpinner spin_bussiness;
    @Bind(R.id.edt_co)
    ClearEditText edt_co;
    @Bind(R.id.edt_recivier)
    ClearEditText edt_recivier;
    @Bind(R.id.pll_code)
    PercentLinearLayout pll_code;
    @Bind(R.id.pll_reciver)
    PercentLinearLayout pll_reciver;
    SpinnerDropAdapter adapter;
    List<Merchant> list;
    String merchantId = "";//商家编号
    int isSpinnerFirst = 0;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_select_layout;
    }

    @Override
    public void initializeContentViews() {
        title.setTitle(getString(R.string.select_bussiness));
        if (MyApplication.merchantList != null) {
            list = MyApplication.merchantList.getList();
        }
        adapter = new SpinnerDropAdapter(list, this);
        spin_bussiness.setAdapter(adapter);
        spin_bussiness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    Field field = null;
//                    try {
//                        field = AdapterView.class.getDeclaredField("mOldSelectedPosition");
//                    } catch (NoSuchFieldException e) {
//                        e.printStackTrace();
//                    }
//                    field.setAccessible(true);  //设置mOldSelectedPosition可访问
//                    field.setInt(spin_bussiness, AdapterView.INVALID_POSITION); //设置mOldSelectedPosition的值
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                if (isSpinnerFirst<2) {
////第一次初始化spinner时，不显示默认被选择的第一项即可
////                    view.setVisibility(View.INVISIBLE);
//                    TextView textView = (TextView) view.findViewById(R.id.tv_context);
//                    textView.setText(getString(R.string.select_merchant));
//                    isSpinnerFirst ++;
//                } else {
//                    TextView textView = (TextView) view.findViewById(R.id.tv_context);
//                    textView.setText(list.get(position).getMerchantName());
                merchantId = list.get(position).getMerchantId();
                edt_co.setText(merchantId);
                edt_recivier.setText(list.get(position).getMerchantName());
                if (list.get(position).getShowCo() == 0) {
                    pll_code.setVisibility(View.GONE);
                    pll_reciver.setVisibility(View.GONE);
                } else {
                    pll_code.setVisibility(View.VISIBLE);
                    pll_reciver.setVisibility(View.VISIBLE);
                }
//                    isSpinnerFirst = false;
//                }

//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @OnClick(R.id.btn_commit)
    @Override
    public void onClick(View v) {
        createTaking();
    }

    //创建无预报揽收
    private void createTaking() {
        if (pll_reciver.getVisibility() == View.VISIBLE &&
                (StringUtils.isEmpty(edt_co.getText().toString()) ||
                        StringUtils.isEmpty(edt_recivier.getText().toString()))) {
            T.showShort(this, getString(R.string.co_recivier_not));
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("expressNo", getIntent().getStringExtra("expressNo"));
            if (StringUtils.isEmpty(merchantId)) {
                T.showShort(this, getString(R.string.select_merchant));
            } else
                jsonObject.put("merchant", merchantId);
            jsonObject.put("name", edt_recivier.getText().toString());
            jsonObject.put("co", edt_co.getText().toString());

            BirdApi.postTakingCreat(this, jsonObject, new RequestCallBackInterface() {
                @Override
                public void successCallBack(JSONObject object) {
                    try {
                        String tid = object.getString("tid");
                        getMerchant(tid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void errorCallBack(JSONObject object) {
                    try {
                        T.showShort(TakingSelectMerchantActivity.this, object.getString("errMsg"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, tag, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getMerchant(String tid) {
        BirdApi.takingOrderNoInfo(this, tid, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                TakingOrderNoInfoEntity entity;
                entity = GsonHelper.getPerson(object.toString(), TakingOrderNoInfoEntity.class);
                String orderId = entity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo();
                String tid = entity.getDetail().getBaseInfo().getBaseInfo().getTid();
                String owner = entity.getDetail().getBaseInfo().getPerson().getCo();
                MyApplication.loggingUpload.takeSelectMerchant(TakingSelectMerchantActivity.this, tag, orderId, tid, owner);//日志上报
                if (entity != null) {
                    Intent intent = new Intent(TakingSelectMerchantActivity.this, TakingToolActivity.class);
                    Bundle b = new Bundle();
                    b.putString("location_position", "1");
                    b.putSerializable("takingOrder", entity.getDetail().getBaseInfo());
                    intent.putExtras(b);
                    startActivity(intent);
                    TakingSelectMerchantActivity.this.finish();//结束这个activity
                }
            }

            @Override
            public void errorCallBack(JSONObject object) {

            }
        }, tag, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
}
