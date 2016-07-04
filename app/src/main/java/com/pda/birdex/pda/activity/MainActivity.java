package com.pda.birdex.pda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.Toast;

import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.IndexAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.CommonItemEntity;
import com.pda.birdex.pda.response.MerchantEntity;
import com.pda.birdex.pda.services.BluetoothService;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.TitleView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/15.
 */
public class MainActivity extends BaseActivity implements OnRecycleViewItemClickListener {
    String tag = "MainActivity";
    @Bind(R.id.rcy)
    RecyclerView rcy;
    @Bind(R.id.title)
    TitleView title;
    IndexAdapter adapter;
    String[] lists;
    String[] takinglists;//收货
    String[] countToLists;//清点
    private long exitTime = 0;//退出事件累计
    List<CommonItemEntity> indexList = new ArrayList<>();

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initializeContentViews() {
        lists = getResources().getStringArray(R.array.index_list);
        takinglists = getResources().getStringArray(R.array.taking_list);
        countToLists = getResources().getStringArray(R.array.count_list);
        title.setBackIvVisble(false);
        title.setTitle(getString(R.string.index));
        for (int i = 0; i < lists.length; i++) {
            CommonItemEntity entity = new CommonItemEntity();
            entity.setName(lists[i]);
            entity.setCount("");
            indexList.add(entity);
        }
        adapter = new IndexAdapter(this, indexList);
        adapter.setOnRecycleViewItemClickListener(this);
        rcy.setLayoutManager(new GridLayoutManager(this, 2));
        rcy.setAdapter(adapter);
        BirdApi.getAllMerchant(this, new RequestCallBackInterface() {//获取商家列表供后续使用
            @Override
            public void successCallBack(JSONObject object) {
                MyApplication.merchantList = GsonHelper.getPerson(object.toString(), MerchantEntity.class);
            }

            @Override
            public void errorCallBack(JSONObject object) {

            }
        }, tag, true);
        if (BasePrintBarScanActivity.mService != null) {
            if (BasePrintBarScanActivity.mService.getState() != BluetoothService.STATE_CONNECTED) {
                T.showLong(this, getString(R.string.print_hint));
            }
        }else{
            T.showLong(this, getString(R.string.print_hint));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        T.showShort(this, "keyCode" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            onItemClick(keyCode - 8);
        }
        return super.onKeyDown(keyCode, event);
    }

    //两次点击退出
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), getString(R.string.keyCode_back),
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        BirdApi.cancelRequestWithTag(tag);
        super.onDestroy();
    }

    @Override
    public void onItemClick(int position) {
        if (position >= 0 && position < adapter.getItemCount()) {
            Intent intent = new Intent(MainActivity.this, SecondIndexActivity.class);
            Bundle b = new Bundle();
            b.putString("name", indexList.get(position).getName());
            switch (position) {
                case 0:
                    b.putStringArray("list", takinglists);
                    break;
                case 1:// 清点
                    b.putStringArray("list", countToLists);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    intent.setClass(MainActivity.this, SettingBarScanActivity.class);
                    break;
            }
            intent.putExtras(b);
            startActivity(intent);
        }
    }
}
