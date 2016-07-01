package com.pda.birdex.pda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import com.loopj.android.http.RequestParams;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/30.
 */
public class TakingSelectBussinessActivity extends BaseActivity implements OnClickListener {
    String tag = "TakingSelectBussinessActivity";
    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.spin_bussiness)
    AppCompatSpinner spin_bussiness;
    @Bind(R.id.edt_co)
    ClearEditText edt_co;
    @Bind(R.id.edt_recivier)
    ClearEditText edt_recivier;

    SpinnerDropAdapter adapter;
    List<Merchant> list;
    String merchantId = "";//商家编号

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
                merchantId = list.get(position).getMerchantId();
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
        if(StringUtils.isEmpty(edt_co.getText().toString())||StringUtils.isEmpty(edt_recivier.getText().toString())){
            T.showShort(this,getString(R.string.co_recivier_not));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("expressNo", getIntent().getStringExtra("expressNo"));//快递号
        params.put("merchant", merchantId);
        params.put("name", edt_recivier.getText().toString());//收件人姓名
        params.put("co", edt_co.getText().toString());//用户编号
        BirdApi.postTakingCreat(this, params, new RequestCallBackInterface() {
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

            }
        }, tag, true);
    }

    private void getMerchant(String tid) {
        BirdApi.takingOrderNoInfo(this, tid, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                TakingOrderNoInfoEntity entity;
                entity = GsonHelper.getPerson(object.toString(), TakingOrderNoInfoEntity.class);
                if (entity != null) {
                    Intent intent = new Intent(TakingSelectBussinessActivity.this, TakingToolActivity.class);
                    Bundle b = new Bundle();
                    b.putString("location_position", "1");
                    b.putSerializable("takingOrder", entity.getDetail().getBaseInfo());
                    intent.putExtras(b);
                    startActivity(intent);
                    TakingSelectBussinessActivity.this.finish();//结束这个activity
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
