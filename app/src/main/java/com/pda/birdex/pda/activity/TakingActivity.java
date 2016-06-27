package com.pda.birdex.pda.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.CommonSimpleAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/17.
 */
public class TakingActivity extends BarScanActivity implements View.OnClickListener {
    String tag = "TakingActivity";
    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.edt_input)
    ClearEditText edt_input;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_taking;
    }

    @Override
    public void barInitializeContentViews() {
        title.setTitle(getString(R.string.scan_logistics));
//        for (int i = 0; i < 5; i++) {
//            saveList.add("aaa" + i);
//        }
        edt_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String string = v.getText().toString();
                    ClearEditTextCallBack(string);
                }
                return false;
            }
        });
    }

    //获取暂存列表
    public void getSaveList(){

    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_input;
    }

    //扫描回调接口
    @Override
    public void ClearEditTextCallBack(String code) {
        BirdApi.Check(this, code, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
//                T.showShort(TakingActivity.this,"show");
//               GsonHelper.getPerson(object.toString(),)
                try {
                    if(object.getBoolean("isExist")){
                        Intent intent = new Intent(TakingActivity.this,TakingToolActivity.class);
                        intent.putExtra("takingOrderNo",object.getString("takingOrderNo"));
                        startActivity(intent);
                    }else{
                        T.showShort(TakingActivity.this, getString(R.string.taking_isExist));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorCallBack(JSONObject object) {

            }

        },tag,true);
//        Intent intent = new Intent(this, TakingDetailActivity.class);
//        intent.putExtra("code",code);
//        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        BirdApi.cancelRequestWithTag(tag);
        super.onDestroy();
    }

    //暂存的list
    List<String> saveList = new ArrayList<String>();

    @OnClick(R.id.img_add)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add:
                getSaveList();
                showMenuWindow(v, saveList);
                break;
        }
    }

    private void showPopupWindow(View viewID, RecyclerView.Adapter adapter) {
        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View popWindow = LayoutInflater.from(this).inflate(R.layout.common_recycleview_layout, null);
//        popWindow.setBackgroundColor(Color.TRANSPARENT);
        RecyclerView rcy = (RecyclerView) popWindow.findViewById(R.id.rcy);
        rcy.setLayoutManager(new LinearLayoutManager(this));
        rcy.setAdapter(adapter);
        int width = getWindowManager().getDefaultDisplay().getWidth();
//        int width = viewID.getWidth();
        mPopupWindow = new PopupWindow(popWindow, width / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();
//        if(rcy!=null){
//            rcy.setBackgroundResource(R.drawable.pop_mid_bg);
//        }
        mPopupWindow.showAsDropDown(viewID, 0, 0);
    }

    PopupWindow mPopupWindow;

    public void showMenuWindow(final View viewID, final List<String> list) {
        CommonSimpleAdapter adapter = new CommonSimpleAdapter(this, list);
        adapter.setOnRecyclerViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                ((ClearEditText)viewID).setText(list.get(position));
            }
        });
        showPopupWindow(viewID, adapter);
    }

}
