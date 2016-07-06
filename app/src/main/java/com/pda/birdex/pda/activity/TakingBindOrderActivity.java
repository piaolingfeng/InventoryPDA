package com.pda.birdex.pda.activity;

import android.net.Uri;
import android.os.Bundle;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.TakingToolBindOrderFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.widget.TitleView;

import butterknife.Bind;

/**
 * Created by hyj on 2016/7/4.
 */
public class TakingBindOrderActivity extends BaseActivity implements  BaseFragment.OnFragmentInteractionListener, BackHandledInterface {
    @Bind(R.id.title)
    TitleView title;
    private TakingToolBindOrderFragment bindNumFragment;
    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_taking_bindorder_layout;
    }

    @Override
    public void initializeContentViews() {
        title.setTitle(getString(R.string.bind_order));
        if (bindNumFragment == null){
            bindNumFragment = new TakingToolBindOrderFragment();
        }
        Bundle b = new Bundle();
        b .putString("entrance","SecondIndex");
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,bindNumFragment).commit();
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
