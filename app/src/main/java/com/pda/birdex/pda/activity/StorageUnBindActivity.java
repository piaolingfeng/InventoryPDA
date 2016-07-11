package com.pda.birdex.pda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.StockInContainerInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/7/11.
 */
public class StorageUnBindActivity extends BarScanActivity {
    String tag= "StorageUnBindActivity";
    @Bind(R.id.titleView)
    TitleView titleView;
    @Bind(R.id.no_tv)
    TextView no_tv;
    @Bind(R.id.code_et)
    ClearEditText code_et;



    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_lanshouprint;
    }

    @Override
    public void barInitializeContentViews() {
        titleView.setTitle(getString(R.string.release_unbind));
        no_tv.setText(getString(R.string.count_vessel_no));
    }

    @Override
    public ClearEditText getClearEditText() {
        return code_et;
    }

    //入库容器号搜索
    private void checkForStockIn(final String code){
        BirdApi.stockInfo(this, code, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                StockInContainerInfoEntity entity = GsonHelper.getPerson(object.toString(), StockInContainerInfoEntity.class);
                Intent intent = new Intent(StorageUnBindActivity.this, StorageFragmentActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("StockInContainerInfoEntity", entity);
                b.putInt("position", getIntent().getIntExtra("position", 0));//从第五个位置进入
                b.putString("stockNum",code);
                intent.putExtras(b);
                startActivity(intent);
            }

            @Override
            public void errorCallBack(JSONObject object) {
                try {
                    T.showShort(StorageUnBindActivity.this, object.getString("errMsg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, tag, true);
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        checkForStockIn(code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
}
