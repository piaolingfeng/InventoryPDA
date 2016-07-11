package com.pda.birdex.pda.fragments;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.widget.ClearEditText;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/7/8.
 * 追踪箱号
 */
public class StorageTrackFragment extends BarScanBaseFragment implements View.OnClickListener{
    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;
    @Bind(R.id.edt_vessel_num)
    ClearEditText edt_vessel_num;
    @Bind(R.id.btn_edit)
    Button btn_edit;
    @Bind(R.id.edt_storage_old_no)
    ClearEditText edt_storage_old_no;
    @Bind(R.id.tv_storage_old_no)
    TextView tv_storage_old_no;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    String location_position;//判断入口
    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_storage_track_layout;
    }

    @Override
    public void barInitializeContentViews() {

        location_position = bundle.getString("location_position");
        if("SecondIndex".equals(location_position)){
            edt_vessel_num.setVisibility(View.VISIBLE);
            tv_vessel_num.setVisibility(View.INVISIBLE);
        }
        edt_storage_old_no.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    String string = v.getText().toString();
                    ClearEditTextCallBack(string);
                }
                return false;
            }
        });
    }

    @Override
    public ClearEditText getClearEditText() {
        if("SecondIndex".equals(location_position)){
            return edt_vessel_num;
        }
        return edt_storage_old_no;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if(this.isVisible){
            if(getEdt_input() == edt_vessel_num && "SecondIndex".equals(location_position)){//入库首页入口进入
                edt_storage_old_no.requestFocus();
                return;
            }
            if(edt_storage_old_no.getVisibility() == View.VISIBLE){//工具包进入

            }
        }
    }

    private void editMode(){
        btn_edit.setVisibility(View.INVISIBLE);
        edt_storage_old_no.setVisibility(View.VISIBLE);
        tv_storage_old_no.setVisibility(View.INVISIBLE);
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
    }

    private void disEnableEditMode(){
        btn_edit.setVisibility(View.VISIBLE);
        edt_storage_old_no.setVisibility(View.INVISIBLE);
        tv_storage_old_no.setVisibility(View.VISIBLE);
        btn_commit.setClickable(false);
        btn_commit.setBackgroundResource(R.drawable.rect_fullgray);
        btn_commit.setTextColor(getResources().getColor(R.color.white));
    }

    @OnClick({R.id.btn_commit,R.id.btn_edit})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                disEnableEditMode();
                break;
            case R.id.btn_edit:
                editMode();
                break;
        }
    }
}
