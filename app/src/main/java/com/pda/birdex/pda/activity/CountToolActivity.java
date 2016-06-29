package com.pda.birdex.pda.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.CountToolBindAreaFragment;
import com.pda.birdex.pda.fragments.CountToolClearFragment;
import com.pda.birdex.pda.fragments.CountToolPhotoFragment;
import com.pda.birdex.pda.fragments.CountToolPrintNumFragment;
import com.pda.birdex.pda.fragments.CountToolTrackFragment;
import com.pda.birdex.pda.fragments.CountToolUnbindFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/24.
 */
public class CountToolActivity extends BaseActivity implements OnRecycleViewItemClickListener, BaseFragment.OnFragmentInteractionListener, BackHandledInterface {
    @Bind(R.id.title)
    TitleView title;

    int tabPosition = 0;//跳转进来的tab的位置
    int currentPosition = 0;//当前savetext list的位置
    List<String> currentMenuList = new ArrayList<>();
    String[] toolMenu;

    private CountToolUnbindFragment countToolUnbindFragment;
    private CountToolPrintNumFragment countToolPrintNumFragment;
    private CountToolClearFragment countToolClearFragment;
    private CountToolPhotoFragment countToolPhotoFragment = null;
    private CountToolTrackFragment countToolTrackFragment = null;
    private CountToolBindAreaFragment countToolBindAreaFragment;
    private FragmentTransaction transaction;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_tool_layout;
    }

    @Override
    public void initializeContentViews() {
        if (countToolUnbindFragment == null)
            countToolUnbindFragment = new CountToolUnbindFragment();
        if (countToolPrintNumFragment == null)
            countToolPrintNumFragment = new CountToolPrintNumFragment();
        if (countToolClearFragment == null)
            countToolClearFragment = new CountToolClearFragment();
        if (countToolPhotoFragment == null)
            countToolPhotoFragment = new CountToolPhotoFragment();
        if (countToolTrackFragment == null)
            countToolTrackFragment = new CountToolTrackFragment();
        if (countToolBindAreaFragment == null)
            countToolBindAreaFragment = new CountToolBindAreaFragment();
        tabPosition = getIntent().getIntExtra("statusPosition", 0);
        toolMenu = getResources().getStringArray(R.array.tool_menu);
        for (String title : toolMenu) {
            currentMenuList.add(title);
        }
        dealToolMenuList();//处理数据
        title.setMenuVisble(true);
        title.setOnSaveItemClickListener(this);//saveMenu clicklistener
        title.setSaveList(currentMenuList);
        addFragment(0,false);//初始默认第一个fragment
    }

    //处理save list
    private void dealToolMenuList() {
        switch (tabPosition) {
            case 0://未开始
                currentPosition = 0;
                title.setTitle(currentMenuList.get(0));
                break;
            case 1://已分类
                title.setTitle(currentMenuList.get(2));
                currentMenuList.remove(toolMenu[0]);
                break;
            case 2://已清点
                currentMenuList.remove(toolMenu[0]);
                currentMenuList.remove(toolMenu[2]);
                break;
        }
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
        switch (tabPosition) {
            case 0:
                switch (position) {
                    case 0:
                        baseFragment = countToolUnbindFragment;
                        break;
                    case 1:
                        baseFragment = countToolPrintNumFragment;
                        break;
                    case 2:
                        baseFragment = countToolClearFragment;
                        break;
                    case 3:
                        baseFragment = countToolPhotoFragment;
                        break;
                    case 4:
                        baseFragment = countToolBindAreaFragment;
                        break;
                    case 5:
                        baseFragment = countToolTrackFragment;
                        break;
                    default:
                        baseFragment = countToolUnbindFragment;
                }
                break;
            case 1:
                switch (position) {
                    case 0:
                        baseFragment = countToolPrintNumFragment;
                        break;
                    case 1:
                        baseFragment = countToolClearFragment;
                        break;
                    case 2:
                        baseFragment = countToolPhotoFragment;
                        break;
                    case 3:
                        baseFragment = countToolBindAreaFragment;
                        break;
                    case 4:
                        baseFragment = countToolTrackFragment;
                        break;
                    default:
                        baseFragment = countToolPrintNumFragment;
                }
                break;
            case 2:
                switch (position) {
                    case 0:
                        baseFragment = countToolPrintNumFragment;
                        break;
                    case 1:
                        baseFragment = countToolPhotoFragment;
                        break;
                    case 2:
                        baseFragment = countToolBindAreaFragment;
                        break;
                    case 3:
                        baseFragment = countToolTrackFragment;
                        break;
                    default:
                        baseFragment = countToolPrintNumFragment;
                }
                break;
        }
        Log.e("android", transaction.isEmpty() + "");
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
        if (countToolUnbindFragment != null)
            transaction.hide(countToolUnbindFragment);
        if (countToolPrintNumFragment != null)
            transaction.hide(countToolPrintNumFragment);
        if (countToolClearFragment != null)
            transaction.hide(countToolClearFragment);
        if (countToolPhotoFragment != null)
            transaction.hide(countToolPhotoFragment);
        if (countToolTrackFragment != null)
            transaction.hide(countToolTrackFragment);
        if (countToolBindAreaFragment != null)
            transaction.hide(countToolBindAreaFragment);
    }


    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
