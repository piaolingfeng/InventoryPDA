package com.pda.birdex.pda.activity;

import android.view.View;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.widget.ClearEditText;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/6/17.
 */
public class TakingPrintActivity extends BarScanActivity implements View.OnClickListener {

    @Bind(R.id.titleView)
    com.pda.birdex.pda.widget.TitleView titleView;

    @Bind(R.id.code_et)
    com.pda.birdex.pda.widget.ClearEditText codeEt;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_lanshouprint;
    }

    @Override
    public void barInitializeContentViews() {
        titleView.setTitle(getString(R.string.printlanshou));
    }

    @Override
    public ClearEditText getClearEditText() {
        return codeEt;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }

    @OnClick({R.id.printsame, R.id.printnew})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.printsame:

                break;
            case R.id.printnew:

                break;
        }
    }
}
