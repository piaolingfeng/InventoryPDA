package com.pda.birdex.pda.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.TakingToolBindAreaFragment;
import com.pda.birdex.pda.fragments.TakingToolBindNumFragment;
import com.pda.birdex.pda.fragments.TakingToolClearFragment;
import com.pda.birdex.pda.fragments.TakingToolPhotoFragment;
import com.pda.birdex.pda.fragments.TakingToolPrintNumFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.widget.TitleView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/25.
 */
public class TakingToolActivity extends BaseActivity implements OnRecycleViewItemClickListener, BaseFragment.OnFragmentInteractionListener, BackHandledInterface {
    String tag = "TakingToolActivity";
    @Bind(R.id.title)
    TitleView title;

    int tabPosition = 0;//跳转进来的tab的位置
    int currentPosition = 0;//当前savetext list的位置
    List<String> currentMenuList = new ArrayList<>();
    String[] toolMenu;

    private TakingToolPrintNumFragment printNumFragment;
    private TakingToolClearFragment clearFragment;
    private TakingToolBindAreaFragment bindAreaFragment;
    private TakingToolBindNumFragment bindNumFragment = null;
    private TakingToolPhotoFragment photoFragment = null;
    private FragmentTransaction transaction;

    public static String takingOrderNo;//揽收单号

    public static TakingOrderNoInfoEntity orderNoInfo;
    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_tool_layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void initializeContentViews() {
        takingOrderNo = getIntent().getStringExtra("takingOrderNo");//
        if(printNumFragment==null)
            printNumFragment= new TakingToolPrintNumFragment();
        if(photoFragment == null)
            photoFragment = new TakingToolPhotoFragment();
        if(bindAreaFragment==null)
            bindAreaFragment = new TakingToolBindAreaFragment();
        if(bindNumFragment == null)
            bindNumFragment = new TakingToolBindNumFragment();
        if(clearFragment == null)
            clearFragment = new TakingToolClearFragment();

        String[] tabList = {getString(R.string.not_start), getString(R.string.has_classified),
                getString(R.string.has_counted), getString(R.string.has_transfer)};//tablayoutName
        tabPosition = getIntent().getIntExtra("statusPosition", 0);
        toolMenu = getResources().getStringArray(R.array.taking_tool_menu);
        for (String title : toolMenu) {
            currentMenuList.add(title);
        }
        dealToolMenuList();//处理数据后获取默认首页的位置
        title.setMenuVisble(true);
        title.setOnSaveItemClickListener(this);//saveMenu clicklistener
        title.setSaveList(currentMenuList);
//        addFragment(currentPosition,false);//初始默认第一个fragment
        getTakingOrderNoInfo();
    }

    //通过揽收单详情
    private void getTakingOrderNoInfo(){
        BirdApi.takingOrderNoInfo(this, takingOrderNo, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                orderNoInfo = GsonHelper.getPerson(object.toString(), TakingOrderNoInfoEntity.class);
                addFragment(currentPosition,true);//初始默认第一个fragment
            }

            @Override
            public void errorCallBack(JSONObject object) {

            }
        },tag,true);
    }

    //处理save list
    private void dealToolMenuList() {
        switch (tabPosition) {
            case 0://未开始
                currentPosition = 0;
                title.setTitle(currentMenuList.get(0));
                break;
            case 1://已分类
                currentPosition = 1;
                title.setTitle(currentMenuList.get(2));
                currentMenuList.remove(toolMenu[0]);
                currentMenuList.remove(toolMenu[1]);
                break;
            case 2://已清点/已交接
            case 3:
                currentPosition = 0;
                currentMenuList.remove(toolMenu[0]);
                currentMenuList.remove(toolMenu[1]);
                currentMenuList.remove(toolMenu[3]);
                break;
        }
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
        switch (tabPosition) {
            case 0:
                switch (position) {
                    case 0:
                        baseFragment = printNumFragment;
                        break;
                    case 1:
                        baseFragment = bindNumFragment;
                        break;
                    case 2:
                        baseFragment = bindAreaFragment;
                        break;
                    case 3:
                        baseFragment = clearFragment;
                        break;
                    case 4:
                        baseFragment = photoFragment;
                        break;
                    default:
                        baseFragment = printNumFragment;
                }
                break;
            case 1:
                switch (position) {
                    case 0:
                        baseFragment = printNumFragment;
                        break;
                    case 1:
                        baseFragment = bindNumFragment;
                        break;
                    case 2:
                        baseFragment = bindAreaFragment;
                        break;
                    case 3:
                        baseFragment = clearFragment;
                        break;
                    case 4:
                        baseFragment = photoFragment;
                        break;
                    default:
                        baseFragment = printNumFragment;
                }
                break;
            case 2:
                switch (position) {
                    case 0:
                        baseFragment = printNumFragment;
                        break;
                    case 1:
                        baseFragment = bindNumFragment;
                        break;
                    case 2:
                        baseFragment = bindAreaFragment;
                        break;
                    case 3:
                        baseFragment = clearFragment;
                        break;
                    case 4:
                        baseFragment = photoFragment;
                        break;
                    default:
                        baseFragment = printNumFragment;
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
        if (printNumFragment != null)
            transaction.hide(printNumFragment);
        if (bindNumFragment != null)
            transaction.hide(bindNumFragment);
        if (bindAreaFragment != null)
            transaction.hide(bindAreaFragment);
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
}
