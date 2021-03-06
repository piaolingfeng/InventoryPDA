package com.pda.birdex.pda.activity;

import android.net.Uri;
import android.os.Bundle;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.CountBindOrderFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.widget.TitleView;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/7/6.
 */
public class CountFragmentActivity extends BaseActivity implements  BaseFragment.OnFragmentInteractionListener, BackHandledInterface {
    @Bind(R.id.title)
    TitleView title;
    private CountBindOrderFragment bindOrderFragment;
    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_fragment_layout;
    }

    @Override
    public void initializeContentViews() {
        title.setTitle(getString(R.string.count_bind));
        if (bindOrderFragment == null){
            bindOrderFragment = new CountBindOrderFragment();
        }
        Bundle b = getIntent().getExtras();
        b .putString("location_position","SecondIndex");
        bindOrderFragment.setUIArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,bindOrderFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
