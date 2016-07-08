package com.pda.birdex.pda.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.CountBindOrderFragment;
import com.pda.birdex.pda.fragments.CountClearFragment;
import com.pda.birdex.pda.fragments.CountPhotoFragment;
import com.pda.birdex.pda.fragments.CountPrintOrderFragment;
import com.pda.birdex.pda.fragments.CountTrackFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.utils.L;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/24.
 */
public class CountToolActivity extends BasePrintBarScanActivity implements OnRecycleViewItemClickListener, BaseFragment.OnFragmentInteractionListener, BackHandledInterface {
    @Bind(R.id.title)
    TitleView title;

    int currentPosition = 0;//当前savetext list的位置
    List<String> currentMenuList = new ArrayList<>();
    String[] toolMenu;

    //    private CountUnbindFragment countToolUnbindFragment;
    private CountPrintOrderFragment countToolPrintNumFragment;
    private CountClearFragment countToolClearFragment;
    private CountPhotoFragment countToolPhotoFragment = null;
    private CountTrackFragment countToolTrackFragment = null;
    //    private CountBindAreaFragment countToolBindAreaFragment;
    private CountBindOrderFragment countToolBindOrderFragment;
    private BaseFragment baseFragment;
    private FragmentTransaction transaction;

    @Override
    public int getPrintContentLayoutResId() {
        return R.layout.activity_tool_layout;
    }

    @Override
    public void printInitializeContentViews() {
        bus.register(this);
//        if (countToolUnbindFragment == null)
//            countToolUnbindFragment = new CountUnbindFragment();
        if (countToolPrintNumFragment == null)
            countToolPrintNumFragment = new CountPrintOrderFragment();
        if (countToolClearFragment == null)
            countToolClearFragment = new CountClearFragment();
        if (countToolPhotoFragment == null)
            countToolPhotoFragment = new CountPhotoFragment();
        if (countToolTrackFragment == null)
            countToolTrackFragment = new CountTrackFragment();
//        if (countToolBindAreaFragment == null)
//            countToolBindAreaFragment = new CountBindAreaFragment();
        if (countToolBindOrderFragment == null)
            countToolBindOrderFragment = new CountBindOrderFragment();
//        tabPosition = getIntent().getIntExtra("statusPosition", 0);
        toolMenu = getResources().getStringArray(R.array.counting_tool_menu);
        for (String title : toolMenu) {
            currentMenuList.add(title);
        }
        title.setMenuVisble(true);
        title.setOnSaveItemClickListener(this);//saveMenu clicklistener
        title.setSaveList(currentMenuList);
        dealToolMenuList();//处理数据
        addFragment(currentPosition, false);//初始默认第一个fragment
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(baseFragment!=null) {
//            baseFragment.onKeyDown(keyCode, event);
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    //处理save list
    private void dealToolMenuList() {
//        switch (tabPosition) {
//            case 0://未开始
//                currentPosition = 0;
//                title.setTitle(currentMenuList.get(0));
//                break;
//            case 1://已分类
//                title.setTitle(currentMenuList.get(2));
//                currentMenuList.remove(toolMenu[0]);
//                break;
//            case 2://已清点
//                currentMenuList.remove(toolMenu[0]);
//                currentMenuList.remove(toolMenu[2]);
//                break;
//        }
        currentPosition = getIntent().getIntExtra("position",0);
        title.setTitle(currentMenuList.get(currentPosition));
    }

    @Override
    public void onItemClick(int position) {
        if (position != currentPosition) {//点击同个位置给予忽略
            currentPosition = position;
            title.setTitle(currentMenuList.get(currentPosition));
            addFragment(position, true);
        }
    }

    /**
     * 隐藏添加fragment
     */
    private void addFragment(int position, boolean flag) {
        BaseFragment baseFragment = null;
        if (flag)
            transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.in_from_right, R.anim.out_from_left, R.anim.in_from_left, R.anim.out_from_right);
        else
            transaction = getSupportFragmentManager().beginTransaction();
        hideFragment();
        switch (position) {
//                    case 0:
//                        baseFragment = countToolUnbindFragment;
//                        break;
            case 0:
                baseFragment = countToolPrintNumFragment;
                break;
            case 1:
                baseFragment = countToolBindOrderFragment;
                break;
            case 2:
                baseFragment = countToolClearFragment;
                break;
            case 3:
                baseFragment = countToolPhotoFragment;
                break;
//                    case 4:
//                        baseFragment = countToolBindAreaFragment;
//                        break;
            case 4:
                baseFragment = countToolTrackFragment;
                break;
            default:
                baseFragment = countToolPrintNumFragment;

        }
        L.e("android", transaction.isEmpty() + "");
        if (!baseFragment.isAdded())
            transaction.add(R.id.framelayout, baseFragment);
        transaction.show(baseFragment);
        Bundle b = new Bundle();
//        b.putSerializable("entity", entity);
        baseFragment.setUIArguments(b);
        transaction.commit();
    }


    //隐藏所有fragment
    private void hideFragment() {
//        if (countToolUnbindFragment != null)
//            transaction.hide(countToolUnbindFragment);
        if (countToolPrintNumFragment != null)
            transaction.hide(countToolPrintNumFragment);
        if (countToolBindOrderFragment != null)
            transaction.hide(countToolBindOrderFragment);
        if (countToolClearFragment != null)
            transaction.hide(countToolClearFragment);
        if (countToolPhotoFragment != null)
            transaction.hide(countToolPhotoFragment);
        if (countToolTrackFragment != null)
            transaction.hide(countToolTrackFragment);
//        if (countToolBindAreaFragment != null)
//            transaction.hide(countToolBindAreaFragment);
    }

    @Subscribe
    public void onEvent(List<String> list) {
        if (list != null) {//发送给打印机
            for (String i : list) {
                sendMessage(i);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        baseFragment = selectedFragment;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public TitleView printTitleView() {
        return null;
    }

    @Override
    public ClearEditText getClearEditText() {
        return null;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }
}
