package com.pda.birdex.pda.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.CommonSimpleAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.CheckResultEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/17.
 */
public class TakingScanActivity extends BarScanActivity implements View.OnClickListener,OnEditorActionListener {
    String tag = "TakingScanActivity";
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
        edt_input.setOnEditorActionListener(this);
    }

    //获取暂存列表
    public void getSaveList() {

    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_input;
    }

    //扫描回调接口
    @Override
    public void ClearEditTextCallBack(final String code) {
        BirdApi.Check(this, code, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                CheckResultEntity entity = GsonHelper.getPerson(object.toString(), CheckResultEntity.class);
                if (entity.isExist()) {
                    Intent intent = new Intent(TakingScanActivity.this, TakingToolActivity.class);

                    Bundle b = new Bundle();
                    b.putSerializable("takingOrder", entity.getOrderInfo());
                    b.putString("location_position","1");
                    intent.putExtras(b);
                    startActivity(intent);
                } else {
//                    T.showShort(TakingScanActivity.this, getString(R.string.taking_isExist));
                    Intent i = new Intent(TakingScanActivity.this,TakingSelectBussinessActivity.class);
                    i.putExtra("expressNo",code);
                    startActivity(i);
                }
            }

            @Override
            public void errorCallBack(JSONObject object) {

            }

        }, tag, true);
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
                ((ClearEditText) viewID).setText(list.get(position));
            }
        });
        showPopupWindow(viewID, adapter);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH ) {
            String string = v.getText().toString();
            ClearEditTextCallBack(string);
        }
        T.showShort(this,"actionId"+actionId);
        return false;
    }
}
