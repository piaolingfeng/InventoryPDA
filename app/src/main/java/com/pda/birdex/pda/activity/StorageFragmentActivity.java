package com.pda.birdex.pda.activity;

import android.net.Uri;
import android.os.Bundle;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.StorageBindOrderFragment;
import com.pda.birdex.pda.fragments.StorageBindPositionFragment;
import com.pda.birdex.pda.fragments.StoragePrintOrderFragment;
import com.pda.birdex.pda.fragments.StorageTrackFragment;
import com.pda.birdex.pda.fragments.StorageUnBindFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.widget.TitleView;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/7/8.
 */
public class StorageFragmentActivity extends BaseActivity implements  BaseFragment.OnFragmentInteractionListener, BackHandledInterface {
    @Bind(R.id.title)
    TitleView title;
    BaseFragment baseFragment;
    StoragePrintOrderFragment printOrderFragment;//打印入库单
    StorageBindOrderFragment bindOrderFragment;//绑定入库单
    StorageTrackFragment trackFragment;//追踪箱号
    StorageUnBindFragment unBindFragment;//解除绑单
    StorageBindPositionFragment bindPositionFragment;//绑定库位

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        baseFragment = selectedFragment;
    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_fragment_layout;
    }

    @Override
    public void initializeContentViews() {
        String []head = getResources().getStringArray(R.array.storage_list);
        int position = getIntent().getIntExtra("position",0);
        title.setTitle(head[position]);
        switch (position){
            case 0:
                if(printOrderFragment==null)
                    printOrderFragment = new StoragePrintOrderFragment();
                baseFragment = printOrderFragment;
                break;
            case 2:
                if(bindOrderFragment==null)
                    bindOrderFragment = new StorageBindOrderFragment();
                baseFragment = bindOrderFragment;
                break;
            case 3:
                if(bindPositionFragment == null)
                    bindPositionFragment = new StorageBindPositionFragment();
                baseFragment = bindPositionFragment;
                break;
            case 4:
                if(trackFragment ==null)
                    trackFragment = new StorageTrackFragment();
                baseFragment = trackFragment;
                break;
            case 5:
                if(unBindFragment ==null)
                    unBindFragment = new StorageUnBindFragment();
                baseFragment = unBindFragment;
                break;
        }
        Bundle b = getIntent().getExtras();
        b.putString("location_position", "SecondIndex");
        baseFragment.setUIArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,baseFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
