package com.pda.birdex.pda.activity;

import android.net.Uri;
import android.os.Bundle;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.TakingBindOrderFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.widget.TitleView;

import butterknife.Bind;

/**
 * Created by hyj on 2016/7/4.
 */
public class TakingFragmentActivity extends BaseActivity implements  BaseFragment.OnFragmentInteractionListener, BackHandledInterface {
    @Bind(R.id.title)
    TitleView title;
    private TakingBindOrderFragment bindNumFragment;
    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_fragment_layout;
    }

    @Override
    public void initializeContentViews() {
        title.setTitle(getString(R.string.bind_order));
        if (bindNumFragment == null){
            bindNumFragment = new TakingBindOrderFragment();
        }
        Bundle b = new Bundle();
        b .putString("location_position","SecondIndex");
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,bindNumFragment).commit();
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
