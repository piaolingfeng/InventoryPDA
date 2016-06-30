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
import com.pda.birdex.pda.widget.TitleView;

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
    SpinnerDropAdapter adapter;
    List<Merchant> list;
    String merchantName="";
    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_select_layout;
    }

    @Override
    public void initializeContentViews() {
        title.setTitle(getString(R.string.select_bussiness));
        if(MyApplication.merchantList!=null){
            list = MyApplication.merchantList.getList();
        }
        adapter = new SpinnerDropAdapter(list,this);
        spin_bussiness.setAdapter(adapter);
        spin_bussiness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                merchantName = list.get(position).getMerchantName();
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
    private void createTaking(){
        RequestParams params = new RequestParams();
        params.put("createOrderData",merchantName);
        BirdApi.postTakingCreat(this, params, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {

            }

            @Override
            public void errorCallBack(JSONObject object) {

            }
        },tag,true);
    }

    private void getMerchant(String takingOrderNo){
        BirdApi.takingOrderNoInfo(this, takingOrderNo, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                TakingOrderNoInfoEntity entity;
                entity = GsonHelper.getPerson(object.toString(), TakingOrderNoInfoEntity.class);
                if(entity!=null){
                    Intent intent = new Intent(TakingSelectBussinessActivity.this,TakingToolActivity.class);
                    Bundle b = new Bundle();
                    b.putString("location_position","1");
                    b.putSerializable("takingOrder", entity.getDetail().getBaseInfo());
                    intent.putExtras(b);
                    startActivity(intent);
                    TakingSelectBussinessActivity.this.finish();//结束这个activity
                }
            }

            @Override
            public void errorCallBack(JSONObject object) {

            }
        },tag,true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
}
