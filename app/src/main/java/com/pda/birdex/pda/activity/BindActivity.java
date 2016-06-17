package com.pda.birdex.pda.activity;

import com.pda.birdex.pda.R;

import butterknife.Bind;

/**
 * Created by hyj on 2016/6/17.
 */
public class BindActivity extends BaseActivity{

    @Bind(R.id.titleView)
    com.pda.birdex.pda.widget.TitleView titleView;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_bind;
    }

    @Override
    public void initializeContentViews() {

        titleView.setTitle(getString(R.string.areabind));

    }
}
