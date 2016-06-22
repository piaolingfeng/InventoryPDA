package com.pda.birdex.pda.activity;

import android.view.View;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;

import butterknife.Bind;

/**
 * Created by hyj on 2016/6/17.
 */
public class TakingBindActivity extends BarScanActivity{

    @Bind(R.id.no_et)
    com.pda.birdex.pda.widget.ClearEditText noEt;

    @Bind(R.id.code_et)
    com.pda.birdex.pda.widget.ClearEditText codeEt;

    @Bind(R.id.titleView)
    com.pda.birdex.pda.widget.TitleView titleView;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_bind;
    }

    @Override
    public void barInitializeContentViews() {
        titleView.setTitle(getString(R.string.areabind));

        noEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                noEt.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(noEt);
                }
            }
        });
        codeEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                codeEt.overrideOnFocusChange(hasFocus);
                if(hasFocus){
                    setEdt_input(codeEt);
                }
            }
        });
    }

    @Override
    public ClearEditText getClearEditText() {
        return noEt;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }


}
