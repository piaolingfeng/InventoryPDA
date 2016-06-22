package com.pda.birdex.pda.activity;

import android.view.View;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.widget.ClearEditText;

import butterknife.Bind;

/**
 * Created by hyj on 2016/6/22.
 */
public class CountTrackActivity extends BarScanActivity {

    @Bind(R.id.now_et)
    com.pda.birdex.pda.widget.ClearEditText now_et;

    @Bind(R.id.old_et)
    com.pda.birdex.pda.widget.ClearEditText old_et;

    @Bind(R.id.titleView)
    com.pda.birdex.pda.widget.TitleView titleView;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_count_track;
    }

    @Override
    public void barInitializeContentViews() {
        titleView.setTitle(getString(R.string.count_track_no));

        now_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                now_et.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(now_et);
                }
            }
        });
        old_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                old_et.overrideOnFocusChange(hasFocus);
                if(hasFocus){
                    setEdt_input(old_et);
                }
            }
        });
    }

    @Override
    public ClearEditText getClearEditText() {
        return now_et;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }
}
