package com.pda.birdex.pda.activity;

import android.os.Bundle;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.fragments.TakingToolBindNumFragment;
import com.pda.birdex.pda.widget.TitleView;

import butterknife.Bind;

/**
 * Created by hyj on 2016/7/4.
 */
public class TakingBindOrderActivity extends BaseActivity{
    @Bind(R.id.title)
    TitleView title;
    private TakingToolBindNumFragment bindNumFragment;
    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initializeContentViews() {
        title.setTitle(getString(R.string.bind_order));
        if (bindNumFragment == null){
            bindNumFragment = new TakingToolBindNumFragment();
        }
        Bundle b = new Bundle();
        b .putString("entrance","SecondIndex");
        getSupportFragmentManager().beginTransaction().replace(R.id.rcy,bindNumFragment).commit();
    }

}
