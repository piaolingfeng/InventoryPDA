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
 * 绑定入库单
 */
public class StorageBindOrderFragment extends BarScanBaseFragment implements View.OnClickListener{
    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;
    @Bind(R.id.edt_storage_order)
    ClearEditText edt_storage_order;
    @Bind(R.id.tv_storage_order)
    TextView tv_storage_order;
    @Bind(R.id.btn_edit)
    Button btn_edit;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_storage_bindorder_layout;
    }

    @Override
    public void barInitializeContentViews() {
        edt_storage_order.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        return edt_storage_order;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if(this.isVisible){

        }
    }

    private void editMode(){
        btn_edit.setVisibility(View.INVISIBLE);
        edt_storage_order.setVisibility(View.VISIBLE);
        tv_storage_order.setVisibility(View.INVISIBLE);
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
    }

    private void disEnableEditMode(){
        btn_edit.setVisibility(View.VISIBLE);
        edt_storage_order.setVisibility(View.INVISIBLE);
        tv_storage_order.setVisibility(View.VISIBLE);
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
