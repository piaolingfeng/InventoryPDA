package com.pda.birdex.pda.activity;

import android.view.View;

import com.pda.birdex.pda.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/6/17.
 */
public class LanshouPrintActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.titleView)
    com.pda.birdex.pda.widget.TitleView titleView;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_lanshouprint;
    }

    @Override
    public void initializeContentViews() {

        titleView.setTitle(getString(R.string.printlanshou));
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
