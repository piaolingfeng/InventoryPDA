package com.pda.birdex.pda.fragments;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.pda.birdex.pda.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/7/8.
 * 打印容器单
 */
public class StoragePrintOrderFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;//容器号
    @Bind(R.id.tv_storage_order)
    TextView tv_storage_order;//入库单号
    @Bind(R.id.tv_business)
    TextView tv_business;//商家
    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_storage_printorder_layout;
    }

    @Override
    public void initializeContentViews() {

    }

    @Override
    protected void lazyLoad() {

    }



    @OnClick(R.id.btn_commit)
    @Override
    public void onClick(View v) {

    }
}
