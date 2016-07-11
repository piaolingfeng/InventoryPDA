package com.pda.birdex.pda.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.StorageBindOrderFragment;
import com.pda.birdex.pda.fragments.StorageBindPositionFragment;
import com.pda.birdex.pda.fragments.StorageClearFragment;
import com.pda.birdex.pda.fragments.StoragePhotoFragment;
import com.pda.birdex.pda.fragments.StoragePrintOrderFragment;
import com.pda.birdex.pda.fragments.StorageTrackFragment;
import com.pda.birdex.pda.fragments.StorageUnBindFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.utils.HideSoftKeyboardUtil;
import com.pda.birdex.pda.utils.L;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/7/8.
 */
public class StorageToolActivity extends BasePrintBarScanActivity implements OnRecycleViewItemClickListener, BaseFragment.OnFragmentInteractionListener, BackHandledInterface {
    @Bind(R.id.title)
    TitleView title;

    int currentPosition = 0;//当前savetext list的位置
    List<String> currentMenuList = new ArrayList<>();
    String[] toolMenu;

    private BaseFragment baseFragment;
    private FragmentTransaction transaction;

    private StorageBindOrderFragment bindOrderFragment;
    private StoragePrintOrderFragment printOrderFragment;
    private StorageBindPositionFragment bindPositionFragment;
    private StoragePhotoFragment photoFragment;
    private StorageTrackFragment trackFragment;
    private StorageUnBindFragment unBindFragment;
    private StorageClearFragment clearFragment;
    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        baseFragment = selectedFragment;
    }

    @Override
    public int getPrintContentLayoutResId() {
        return R.layout.activity_tool_layout;
    }

    @Override
    public void printInitializeContentViews() {
        bus.register(this);
        if(bindOrderFragment==null)
            bindOrderFragment = new StorageBindOrderFragment();
        if(printOrderFragment == null)
            printOrderFragment = new StoragePrintOrderFragment();
        if(bindPositionFragment == null)
            bindPositionFragment = new StorageBindPositionFragment();
        if(photoFragment == null)
            photoFragment = new StoragePhotoFragment();
        if(trackFragment ==null)
            trackFragment = new StorageTrackFragment();
        if(unBindFragment ==null)
            unBindFragment = new StorageUnBindFragment();
        if(clearFragment ==null)
            clearFragment = new StorageClearFragment();
        toolMenu = getResources().getStringArray(R.array.storage_tool_list);
        for (String title : toolMenu) {
            currentMenuList.add(title);
        }
        title.setMenuVisble(true);
        title.setOnSaveItemClickListener(this);//saveMenu clicklistener
        title.setSaveList(currentMenuList);
        dealToolMenuList();//处理数据
        addFragment(currentPosition, false);//初始默认第一个fragment
    }
    private void dealToolMenuList() {
        currentPosition = getIntent().getIntExtra("position",0);
        title.setTitle(currentMenuList.get(currentPosition));
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
            case 0:
                baseFragment = bindOrderFragment;
                break;
            case 1:
                baseFragment = printOrderFragment;
                break;
            case 2:
                baseFragment = bindPositionFragment;
                break;
            case 3:
                baseFragment = photoFragment;
                break;
            case 4:
                baseFragment = trackFragment;
                break;
            case 5:
                baseFragment = unBindFragment;
                break;
            case 6:
                baseFragment = clearFragment;
                break;
            default:
                baseFragment = bindOrderFragment;

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
        if (bindOrderFragment != null)
            transaction.hide(bindOrderFragment);
        if (printOrderFragment != null)
            transaction.hide(printOrderFragment);
        if (bindPositionFragment != null)
            transaction.hide(bindPositionFragment);
        if (photoFragment != null)
            transaction.hide(photoFragment);
        if (trackFragment != null)
            transaction.hide(trackFragment);
        if (unBindFragment != null)
            transaction.hide(unBindFragment);
        if (clearFragment != null)
            transaction.hide(clearFragment);
    }


    @Override
    public void onItemClick(int position) {
        if (position != currentPosition) {//点击同个位置给予忽略
            currentPosition = position;
            title.setTitle(currentMenuList.get(currentPosition));
            addFragment(position, true);
        }
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
        HideSoftKeyboardUtil.hideSoftKeyboard(this);//所有扫描动作都统一隐藏软键盘
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Subscribe
    public void onEvent(List<String> list){
        if(list!=null){//发送给打印机
            for (String i:list){
                sendMessage(i);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

}
