package com.pda.birdex.pda.activity;

import android.net.Uri;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.CountMissionBussinessOtherFragment;
import com.pda.birdex.pda.fragments.CountMissionBussniessFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.interfaces.TitleBarBackInterface;
import com.pda.birdex.pda.utils.HideSoftKeyboardUtil;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/22.
 */
public class CountMissionActivity extends BarScanActivity implements BaseFragment.OnFragmentInteractionListener, BackHandledInterface {

    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.edt_input_business)
    ClearEditText edt_input_business;

    CountMissionBussinessOtherFragment otherFragment;
    CountMissionBussniessFragment bussniessFragment;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_countmission_layout;
    }

    @Override
    public void barInitializeContentViews() {
        EventBus.getDefault().register(this);
        title.setTitle(getString(R.string.count_task));
        title.setBackInterface(new TitleBarBackInterface() {//
            @Override
            public void onBackClick() {
                if(getSupportFragmentManager().getBackStackEntryCount()>0)
                    getSupportFragmentManager().popBackStack();
                else{
                    CountMissionActivity.this.finish();
                }
            }
        });
        edt_input_business.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String string = v.getText().toString();
//                    search(string);
                    ClearEditTextCallBack(string);
//                    T.showShort(MyApplication.getInstans(), "dddd" + actionId);
                }
                return false;
            }
        });
        if (otherFragment == null)
            otherFragment = new CountMissionBussinessOtherFragment();
        if (bussniessFragment == null) {
            bussniessFragment = new CountMissionBussniessFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, bussniessFragment).commit();
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_input_business;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        HideSoftKeyboardUtil.hideSoftKeyboard(this);
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEvent(String count_mission_other){
        if (count_mission_other.equals("count_mission_other")) {
            if (!otherFragment.isAdded())
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.in_from_right,R.anim.out_from_left,R.anim.in_from_left,R.anim.out_from_right).replace(R.id.framelayout, otherFragment).addToBackStack("").commit();
        }
    }
}
