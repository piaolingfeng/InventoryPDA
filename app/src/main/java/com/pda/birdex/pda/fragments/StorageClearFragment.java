package com.pda.birdex.pda.fragments;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.widget.ClearEditText;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/7/8.
 * 清点
 */
public class StorageClearFragment extends BarScanBaseFragment implements View.OnClickListener{
    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;
    @Bind(R.id.tv_storage_order)
    TextView tv_storage_order;
    @Bind(R.id.btn_edit)
    Button btn_edit;
    @Bind(R.id.edt_upc)
    ClearEditText edt_upc;
    @Bind(R.id.tv_upc)
    TextView tv_upc;
    @Bind(R.id.edt_amount)
    ClearEditText edt_amount;
    @Bind(R.id.tv_amount)
    TextView tv_amount;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_storage_clear_layout;
    }

    @Override
    public void barInitializeContentViews() {

    }

    private void editMode() {
        btn_edit.setVisibility(View.INVISIBLE);
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
        edt_upc.setVisibility(View.VISIBLE);
        tv_upc.setVisibility(View.INVISIBLE);
        edt_amount.setVisibility(View.VISIBLE);
        tv_amount.setVisibility(View.INVISIBLE);
    }

    private void disableEditMode() {
        btn_edit.setVisibility(View.VISIBLE);
        btn_commit.setClickable(false);
        btn_commit.setBackgroundResource(R.drawable.rect_fullgray);
        btn_commit.setTextColor(getResources().getColor(R.color.white));
        edt_upc.setVisibility(View.INVISIBLE);
        tv_upc.setVisibility(View.VISIBLE);
        edt_amount.setVisibility(View.INVISIBLE);
        tv_amount.setVisibility(View.VISIBLE);
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_upc;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if(this.isVisible && edt_upc.getVisibility() == View.VISIBLE){
            if(edt_upc.hasFocus()){
                edt_amount.requestFocus();//进入下一个
            }else
                edt_upc.requestFocus();
        }
    }

    @OnClick({R.id.btn_commit,R.id.btn_edit})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                disableEditMode();
                break;
            case R.id.btn_edit:
                editMode();
                break;
        }
    }
}
