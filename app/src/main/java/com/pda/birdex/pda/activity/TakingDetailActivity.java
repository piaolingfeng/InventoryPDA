package com.pda.birdex.pda.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.CommonSimpleAdapter;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.utils.StringUtils;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/17.
 */
public class TakingDetailActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.tv_input_business)
    TextView tv_input_business;
    @Bind(R.id.prl_business)
    PercentRelativeLayout prl_business;
    @Bind(R.id.img_add)
    ImageView img_add;

    @Bind(R.id.edt_input_business)
    ClearEditText edt_input_business;
    @Bind(R.id.tv_taking_error)
    TextView tv_taking_error;
    @Bind(R.id.edt_input_count)
    ClearEditText edt_input_count;
    @Bind(R.id.tv_input_count)
    TextView tv_input_count;

    @Bind(R.id.tv_discript)
    TextView tv_discript;
    @Bind(R.id.tv_edit)
    TextView tv_edit;

    @Bind(R.id.btn_commit)
    Button btn_commit;
    @Bind(R.id.btn_save)
    Button btn_save;

    public String SAVE = "SAVE";
    public String COMMIT = "COMMIT";
    public String NOOPERATE = "NOOPERATE";

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_taking_detail;
    }

    @Override
    public void initializeContentViews() {
        title.setTitle("输入揽收信息");
        String code = getIntent().getStringExtra("code");
        getPredictCodeInWareHouse(code);

    }

    //通过网络请求查询预报单号是否在仓库内
    public void getPredictCodeInWareHouse(String code) {
        dealActivity(SAVE, "笨鸟", "10");
    }

    public void dealActivity(String flag, String input_business, String input_count) {
        //暂存
        if (flag == SAVE) {
            tv_discript.setVisibility(View.VISIBLE);
            tv_edit.setVisibility(View.VISIBLE);
            btn_commit.setClickable(true);
            btn_save.setClickable(false);
            btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
            btn_save.setBackgroundResource(R.drawable.rect_fullgray);
            btn_save.setTextColor(getResources().getColor(R.color.white));
            btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
            prl_business.setVisibility(View.GONE);
            edt_input_count.setVisibility(View.GONE);
            tv_input_business.setVisibility(View.VISIBLE);
            tv_input_count.setVisibility(View.VISIBLE);
            tv_input_business.setText(input_business);
            tv_input_count.setText(input_count);
            tv_edit.setClickable(true);
        }
        //提交过
        if (flag == COMMIT) {//分为两种：1、未审核通过；2、审核通过
            tv_discript.setVisibility(View.VISIBLE);
            tv_edit.setVisibility(View.VISIBLE);
            btn_commit.setClickable(false);
            btn_save.setClickable(false);
            btn_commit.setBackgroundResource(R.drawable.rect_fullgray);
            btn_save.setBackgroundResource(R.drawable.rect_fullgray);
            btn_save.setTextColor(getResources().getColor(R.color.white));
            btn_commit.setTextColor(getResources().getColor(R.color.white));
            prl_business.setVisibility(View.GONE);
            edt_input_count.setVisibility(View.GONE);
            tv_input_business.setText(input_business);
            tv_input_count.setText(input_count);
            tv_input_count.setVisibility(View.VISIBLE);
            tv_input_business.setVisibility(View.VISIBLE);
            if (true) {
                tv_edit.setClickable(true);
            } else {//不能点击编辑
                tv_edit.setClickable(false);
                tv_edit.setTextColor(getResources().getColor(R.color.context_2));
            }
        }
        //未操作过
        if (flag == NOOPERATE) {//1、匹配到；2、未匹配到
            tv_discript.setVisibility(View.GONE);
            tv_edit.setVisibility(View.GONE);
            btn_commit.setClickable(true);
            btn_save.setClickable(true);
            btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
            btn_save.setBackgroundResource(R.drawable.rect_fullbluew_selector);
            btn_save.setTextColor(getResources().getColor(R.color.btn_blue_selector));
            btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
            prl_business.setVisibility(View.VISIBLE);
            edt_input_count.setVisibility(View.VISIBLE);
            tv_input_count.setVisibility(View.GONE);
            tv_input_business.setVisibility(View.GONE);
            tv_input_business.setText("");
            tv_input_count.setText("");
            if (true) {
                tv_taking_error.setVisibility(View.GONE);
//                img_add.setClickable(false);
                edt_input_business.setText(input_business);
                edt_input_count.setText(input_count);
            } else {
                tv_taking_error.setVisibility(View.VISIBLE);
//                img_add.setClickable(true);
            }
        }
    }


    //获取商家信息
    public void getBusinessList() {

    }

    @OnClick({R.id.btn_commit, R.id.img_add, R.id.btn_save, R.id.tv_edit})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                //处理网络，成功回调后处理下面
                dealActivity(COMMIT, edt_input_business.getText().toString(), edt_input_count.getText().toString());
                showAlertDialog();
                break;
            case R.id.img_add:
                showMenuWindow(view, new ArrayList<String>());
                break;
            case R.id.btn_save:
                //处理网络，成功回调后处理下面
                dealActivity(SAVE, edt_input_business.getText().toString(), edt_input_count.getText().toString());
                break;
            case R.id.tv_edit:
                //处理网络，成功回调后处理下面
                dealActivity(NOOPERATE, tv_input_business.getText().toString(), tv_input_count.getText().toString());
                break;

        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showAlertDialog() {
        final Dialog myDialog = new Dialog(this, R.style.semester_dialog);
        View view = LayoutInflater.from(TakingDetailActivity.this).inflate(R.layout.dialog_print, null);
//        LinearLayout.LayoutParams params  = new LinearLayout.LayoutParams(300,250);
//        view.setLayoutParams(params);
        myDialog.setContentView(view);

//        Window window = myDialog.getWindow();
//        window.setContentView(view);
        myDialog.setCanceledOnTouchOutside(false);
        Button confirm = (Button) view.findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                //打印
            }
        });
        myDialog.show();
    }

    private void showPopupWindow(View viewID, RecyclerView.Adapter adapter) {
        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View popWindow = LayoutInflater.from(this).inflate(R.layout.common_recycleview_layout, null);
//        popWindow.setBackgroundColor(Color.TRANSPARENT);
        RecyclerView rcy = (RecyclerView) popWindow.findViewById(R.id.rcy);
        rcy.setLayoutManager(new LinearLayoutManager(this));
        rcy.setAdapter(adapter);
//        int width = getWindowManager().getDefaultDisplay().getWidth();
        int width = viewID.getWidth();
        mPopupWindow = new PopupWindow(popWindow, width, LinearLayout.LayoutParams.WRAP_CONTENT);
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

}
