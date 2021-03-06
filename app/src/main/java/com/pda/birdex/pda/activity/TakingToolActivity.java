package com.pda.birdex.pda.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.entity.TakingOrder;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.TakingBindOrderFragment;
import com.pda.birdex.pda.fragments.TakingClearFragment;
import com.pda.birdex.pda.fragments.TakingPhotoFragment;
import com.pda.birdex.pda.fragments.TakingPrintNumFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/25.
 */
public class TakingToolActivity extends BasePrintBarScanActivity implements OnRecycleViewItemClickListener, BaseFragment.OnFragmentInteractionListener, BackHandledInterface {
    String tag = "TakingToolActivity";
    @Bind(R.id.title)
    TitleView title;

    int currentPosition = 0;//当前savetext list的位置
    List<String> currentMenuList = new ArrayList<>();
    String[] toolMenu;

    private TakingPrintNumFragment printNumFragment;
    private TakingClearFragment clearFragment;
//    private TakingBindAreaFragment bindAreaFragment;
    private TakingBindOrderFragment bindNumFragment = null;
    private TakingPhotoFragment photoFragment = null;
    private FragmentTransaction transaction;
    private BaseFragment baseFragment=null;

    public TakingOrder orderInfo;
    String location = "1";

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.baseFragment = selectedFragment;
    }

    @Override
    public int getPrintContentLayoutResId() {
        return R.layout.activity_tool_layout;
    }

    @Override
    public void printInitializeContentViews() {
//        获取传递过来的数据案例
        bus.register(this);
        if(getIntent().getExtras()!=null){
//            orderInfo = (TakingOrder) getIntent().getExtras().get("takingOrder");
            location = getIntent().getStringExtra("location_position");
        }
        if (printNumFragment == null)
            printNumFragment = new TakingPrintNumFragment();
        if (photoFragment == null)
            photoFragment = new TakingPhotoFragment();
//        if (bindAreaFragment == null)
//            bindAreaFragment = new TakingBindAreaFragment();
        if (bindNumFragment == null)
            bindNumFragment = new TakingBindOrderFragment();
        if (clearFragment == null)
            clearFragment = new TakingClearFragment();

        toolMenu = getResources().getStringArray(R.array.taking_tool_menu);
        for (String title : toolMenu) {
            currentMenuList.add(title);
        }
        title.setMenuVisble(true);
        title.setOnSaveItemClickListener(this);//saveMenu clicklistener
        title.setSaveList(currentMenuList);
        if("2".equals(location)){//位置2进来的话会传首先显示的fragment的位置
            currentPosition = getIntent().getExtras().getInt("position");
        }
        dealToolMenuList();//处理数据后获取默认首页的位置
        addFragment(currentPosition, false);//初始默认第一个fragment
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

//    //通过揽收单详情
//    private void getTakingOrderNoInfo(){
//        BirdApi.takingOrderNoInfo(this, takingOrderNo, new RequestCallBackInterface() {
//            @Override
//            public void successCallBack(JSONObject object) {
//                orderNoInfo = GsonHelper.getPerson(object.toString(), TakingOrderNoInfoEntity.class);
//                addFragment(currentPosition,true);//初始默认第一个fragment
//            }
//
//            @Override
//            public void errorCallBack(JSONObject object) {
//
//            }
//        },tag,true);
//    }

    //处理save list
    private void dealToolMenuList() {
//        switch (tabPosition) {
//            case 0://未开始
//                currentPosition = 0;
//                title.setTitle(currentMenuList.get(0));
//                break;
//            case 1://已分类
//                currentPosition = 1;
//                title.setTitle(currentMenuList.get(2));
//                currentMenuList.remove(toolMenu[0]);
//                currentMenuList.remove(toolMenu[1]);
//                break;
//            case 2://已清点/已交接
//            case 3:
//                currentPosition = 0;
//                currentMenuList.remove(toolMenu[0]);
//                currentMenuList.remove(toolMenu[1]);
//                currentMenuList.remove(toolMenu[3]);
//                break;
//        }
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
                baseFragment = printNumFragment;
                break;
            case 1:
                baseFragment = bindNumFragment;
                break;
//            case 2:
//                baseFragment = bindAreaFragment;
//                break;
            case 2:
                baseFragment = clearFragment;
                break;
            case 3:
                baseFragment = photoFragment;
                break;
            default:
                baseFragment = printNumFragment;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        baseFragment.onKeyDown(keyCode,event);
        return super.onKeyDown(keyCode, event);
    }

    //隐藏所有fragment
    private void hideFragment() {
        if (printNumFragment != null)
            transaction.hide(printNumFragment);
        if (bindNumFragment != null)
            transaction.hide(bindNumFragment);
//        if (bindAreaFragment != null)
//            transaction.hide(bindAreaFragment);
        if (photoFragment != null)
            transaction.hide(photoFragment);
        if (clearFragment != null)
            transaction.hide(clearFragment);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onItemClick(int position) {
        if (position != currentPosition) {//点击同个位置给予忽略
            currentPosition = position;
            title.setTitle(currentMenuList.get(currentPosition));
            addFragment(position, true);
        }
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
