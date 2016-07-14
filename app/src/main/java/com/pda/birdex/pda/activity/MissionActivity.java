package com.pda.birdex.pda.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.Merchant;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.MissionBussinessOtherFragment;
import com.pda.birdex.pda.fragments.MissionBussniessFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.interfaces.TitleBarBackInterface;
import com.pda.birdex.pda.response.MerchantEntity;
import com.pda.birdex.pda.response.MerchantListEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.PinYinUtil;
import com.pda.birdex.pda.utils.SoftKeyboardUtil;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/22.
 */
public class MissionActivity extends BarScanActivity implements BaseFragment.OnFragmentInteractionListener, BackHandledInterface {
    String tag = "MissionActivity";
    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.edt_input_business)
    ClearEditText edt_input_business;

    MissionBussinessOtherFragment otherFragment;
    MissionBussniessFragment bussniessFragment;

    MerchantListEntity merchantListEntity;
    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_countmission_layout;
    }

    @Override
    public void barInitializeContentViews() {
        EventBus.getDefault().register(this);
        if(getResources().getString(R.string.taking).equals(getIntent().getStringExtra("HeadName"))){//揽收
            title.setTitle(getString(R.string.taking_task));
//            getAllTakingMerchant();//获取揽收商家列表
        }else {
            title.setTitle(getString(R.string.count_task));
//            getAllCountingMerchant();
        }
        title.setBackInterface(new TitleBarBackInterface() {//
            @Override
            public void onBackClick() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStack();
                else {
                    MissionActivity.this.finish();
                }
            }
        });
        edt_input_business.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String string = v.getText().toString();
                    ClearEditTextCallBack(string);
                }
                return false;
            }
        });
        edt_input_business.setOnClearETChangeListener(new ClearEditText.OnClearETChangeListener() {
            @Override
            public void textChange(CharSequence text) {
                EventBus.getDefault().post(text.toString());
            }
        });
        Bundle b = new Bundle();
        b.putString("HeadName", getIntent().getStringExtra("HeadName"));
        if (otherFragment == null)
            otherFragment = new MissionBussinessOtherFragment();
        if (bussniessFragment == null) {
            bussniessFragment = new MissionBussniessFragment();
        }
        otherFragment.setUIArguments(b);
        bussniessFragment.setUIArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, bussniessFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getResources().getString(R.string.taking).equals(getIntent().getStringExtra("HeadName"))){//揽收
            getAllTakingMerchant();//获取揽收商家列表
        }else {
            getAllCountingMerchant();
        }
    }

    public static List<Merchant> merchantList = new ArrayList<>();

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public  void onEvent(List<Merchant> list){
        if(merchantList.size()==0) {
            merchantList = list;
            for (int i = 0; i < list.size(); i++) {//简拼
                merchantList.get(i).setMerchantFirtSpell(PinYinUtil.getFirstSpell(list.get(i).getMerchantName()));
            }
            for (int i = 0; i < list.size(); i++) {//全拼
                merchantList.get(i).setMerchantFullSpell(PinYinUtil.getFullSpell(list.get(i).getMerchantName()));
            }
        }
    }



    //获取揽收所有商家的任务数量
    public void getAllTakingMerchant(){
        BirdApi.getTakingListCountMerchant(this, "each/unTaking", new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                try {
                    merchantListEntity = GsonHelper.getPerson(object.toString(),MerchantListEntity.class);
                    dealMerchant(merchantListEntity);
                    otherFragment.getUIArguments().putSerializable("merchantList", (Serializable) merchantListEntity.getMerchantCounts());
                    bussniessFragment.getUIArguments().putSerializable("merchantList", (Serializable) merchantListEntity.getMerchantCounts());
                    EventBus.getDefault().post(merchantListEntity.getMerchantCounts());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void errorCallBack(JSONObject object) {

            }
        }, tag, false);
    }

    //获取清点所有商家的任务数量
    public void getAllCountingMerchant(){
        BirdApi.getCountingListCountMerchant(this, "each/unCounting", new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                try {
                    merchantListEntity = GsonHelper.getPerson(object.toString(), MerchantListEntity.class);
                    dealMerchant(merchantListEntity);
                    otherFragment.getUIArguments().putSerializable("merchantList", (Serializable) merchantListEntity.getMerchantCounts());
                    bussniessFragment.getUIArguments().putSerializable("merchantList", (Serializable) merchantListEntity.getMerchantCounts());
                    EventBus.getDefault().post(merchantListEntity.getMerchantCounts());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorCallBack(JSONObject object) {

            }
        }, tag, false);
    }

    //将商家列表跟商家任务数量匹配在一起
    public void dealMerchant(MerchantListEntity listEntity){
        MerchantEntity entity = MyApplication.merchantList;
        for(int i=0;i<listEntity.getMerchantCounts().size();i++){
            for(int j=0;j<entity.getList().size();j++){
                if(entity.getList().get(j).getMerchantId().equals(listEntity.getMerchantCounts().get(i).getMerchantId())){
                    listEntity.getMerchantCounts().get(i).setMerchantName(entity.getList().get(j).getMerchantName());
//                    listEntity.getMerchantCounts().get(i).setShowCo(entity.getList().get(j).getShowCo());
                    break;
                }
            }
        }

    }



    @Override
    public ClearEditText getClearEditText() {
        return edt_input_business;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        SoftKeyboardUtil.hideSoftKeyboard(this);
        EventBus.getDefault().post(code);
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
