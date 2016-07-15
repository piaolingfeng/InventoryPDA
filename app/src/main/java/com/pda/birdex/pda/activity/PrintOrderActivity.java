package com.pda.birdex.pda.activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.PrintOrderDetailFragment;
import com.pda.birdex.pda.fragments.PrintOrderScanFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hyj on 2016/6/17.
 * 揽收打印揽收单
 * 清点打印清点单
 * 入库打印容器单
 */
public class PrintOrderActivity extends BasePrintBarScanActivity implements BaseFragment.OnFragmentInteractionListener, BackHandledInterface {

    String tag = "PrintOrderActivity";

    @Bind(R.id.title)
    TitleView title;

    PrintOrderDetailFragment printOrderDetailFragment;
    PrintOrderScanFragment printOrderScanFragment;

    @Override
    public int getPrintContentLayoutResId() {
        return R.layout.activity_tool_layout;
    }

    @Override
    public void printInitializeContentViews() {
        bus.register(this);
        String titleText = getIntent().getExtras().getString("title");

        if (!TextUtils.isEmpty(titleText)) {
            title.setTitle(titleText);
        }

        addFragment(0, false,"");
    }

    @Override
    public ClearEditText getClearEditText() {
        return null;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
//        SoftKeyboardUtil.hideSoftKeyboard(this);
        if (!TextUtils.isEmpty(code)) {
//            visible();
        }
    }

    // Visible 动画
//    private void visible() {
//        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                2.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//        mShowAction.setDuration(500);
//        button_ll.startAnimation(mShowAction);
//        button_ll.setVisibility(View.VISIBLE);
//    }
//
//    // gone 动画
//    private void gone() {
//        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
//                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                2.0f);
//        mHiddenAction.setDuration(500);
//        button_ll.startAnimation(mHiddenAction);
//        button_ll.setVisibility(View.GONE);
//    }
    android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Subscribe
    public void onEvent(String print_detail) {
        addFragment(1, true,print_detail);
    }

    /**
     * 隐藏添加fragment
     */
    private void addFragment(int position, boolean flag,String text) {
        printOrderDetailFragment = null;
        if (printOrderDetailFragment == null)
            printOrderDetailFragment = new PrintOrderDetailFragment();
        if (printOrderScanFragment == null)
            printOrderScanFragment = new PrintOrderScanFragment();
        BaseFragment baseFragment = null;
        if (flag)
            fragmentTransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.in_from_right, R.anim.out_from_left, R.anim.in_from_left, R.anim.out_from_right).addToBackStack(null);
        else
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment();
        switch (position) {
            case 0:
                baseFragment = printOrderScanFragment;
                break;
            case 1:
                baseFragment = printOrderDetailFragment;
                break;
        }

        if (!baseFragment.isAdded()) {
            fragmentTransaction.add(R.id.framelayout, baseFragment);
        }
        Bundle b = new Bundle();
        b.putString("code", text);
        baseFragment.setUIArguments(b);
        fragmentTransaction.show(baseFragment);
        fragmentTransaction.commit();
    }

    private void hideFragment() {
        if (printOrderDetailFragment != null)
            fragmentTransaction.hide(printOrderDetailFragment);
        if (printOrderScanFragment != null)
            fragmentTransaction.hide(printOrderScanFragment);
    }

    @Subscribe
    public void onEvent(List<String> list) {
        for (String s : list) {
            sendMessage(s);
        }
    }

    @Override
    public TitleView printTitleView() {
        return title;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    BaseFragment baseFragment;

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        baseFragment = selectedFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag = false;
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            flag = true;
        }else
            flag = super.onKeyDown(keyCode, event);
        return flag;
    }
}
