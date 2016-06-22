package com.pda.birdex.pda.activity;

import android.view.View;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.widget.ClearEditText;

import butterknife.Bind;

/**
 * Created by hyj on 2016/6/22.
 */
public class CountPrintActivity extends BarScanActivity {

    @Bind(R.id.count_no_et)
    com.pda.birdex.pda.widget.ClearEditText count_no_et;

    @Bind(R.id.vessel_et)
    com.pda.birdex.pda.widget.ClearEditText vesselEt;

    @Bind(R.id.titleView)
    com.pda.birdex.pda.widget.TitleView titleView;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_count_print;
    }

    @Override
    public void barInitializeContentViews() {
        titleView.setTitle(getString(R.string.count_print_no));

        count_no_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                count_no_et.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(count_no_et);
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
        return count_no_et;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }
}
