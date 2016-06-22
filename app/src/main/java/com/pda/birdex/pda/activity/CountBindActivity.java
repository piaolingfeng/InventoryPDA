package com.pda.birdex.pda.activity;

import android.view.View;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.widget.ClearEditText;

import butterknife.Bind;

/**
 * Created by hyj on 2016/6/22.
 */
public class CountBindActivity extends BarScanActivity {

    @Bind(R.id.area_et)
    com.pda.birdex.pda.widget.ClearEditText areaEt;

    @Bind(R.id.vessel_et)
    com.pda.birdex.pda.widget.ClearEditText vesselEt;

    @Bind(R.id.titleView)
    com.pda.birdex.pda.widget.TitleView titleView;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_count_bind;
    }

    @Override
    public void barInitializeContentViews() {

        titleView.setTitle(getString(R.string.areabind));

        areaEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                areaEt.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(areaEt);
                }
            }
        });
        vesselEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                vesselEt.overrideOnFocusChange(hasFocus);
                if(hasFocus){
                    setEdt_input(vesselEt);
                }
            }
        });

    }

    @Override
    public ClearEditText getClearEditText() {
        return areaEt;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }
}
