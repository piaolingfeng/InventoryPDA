package com.pda.birdex.pda.activity;

import android.net.Uri;
import android.os.Bundle;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.CountToolTrackFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.widget.TitleView;

import butterknife.Bind;

/**
 * Created by hyj on 2016/6/22.
 */
public class CountTrackActivity extends BaseActivity implements  BaseFragment.OnFragmentInteractionListener, BackHandledInterface {
    @Bind(R.id.title)
    TitleView title;
    private CountToolTrackFragment trackFragment;
    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_taking_bindorder_layout;
    }

    @Override
    public void initializeContentViews() {
        title.setTitle(getString(R.string.count_track_no));
        if (trackFragment == null){
            trackFragment = new CountToolTrackFragment();
        }
        Bundle b = getIntent().getExtras();
        b .putString("location_position","SecondIndex");
        trackFragment.setUIArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,trackFragment).commit();
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
