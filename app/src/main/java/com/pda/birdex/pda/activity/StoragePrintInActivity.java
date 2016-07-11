package com.pda.birdex.pda.activity;

import android.view.View;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/7/11.
 * 打印入库单。
 */
public class StoragePrintInActivity extends BarScanActivity implements View.OnClickListener {
    @Bind(R.id.code_et)
    ClearEditText code_et;
    @Bind(R.id.titleView)
    TitleView titleView;
    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_storage_printin_layout;
    }

    @Override
    public void barInitializeContentViews() {
        titleView.setTitle(getString(R.string.print_storage_order));
    }

    @Override
    public ClearEditText getClearEditText() {
        return code_et;
    }



    @Override
    public void ClearEditTextCallBack(String code) {

    }

    @OnClick(R.id.btn_commit)
    @Override
    public void onClick(View v) {

    }
}
