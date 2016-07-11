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
 * 绑定库位
 */
public class StorageBindPositionFragment extends BarScanBaseFragment implements View.OnClickListener {
    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;//容器号
    @Bind(R.id.tv_upc)
    TextView tv_upc;//upc
    @Bind(R.id.btn_edit)
    Button btn_edit;//查看参考库位
    @Bind(R.id.edt_storage_position)
    ClearEditText edt_storage_position;
    @Bind(R.id.tv_storage_position)
    TextView tv_storage_position;
    @Bind(R.id.btn_commit)
    Button btn_commit;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_storage_bindposition_layout;
    }

    @Override
    public void barInitializeContentViews() {

    }

    private void editMode() {
        edt_storage_position.setVisibility(View.VISIBLE);
        tv_storage_position.setVisibility(View.INVISIBLE);
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
    }

    private void disableEditMode() {
        edt_storage_position.setVisibility(View.INVISIBLE);
        tv_storage_position.setVisibility(View.VISIBLE);
        btn_commit.setClickable(false);
        btn_commit.setBackgroundResource(R.drawable.rect_fullgray);
        btn_commit.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_storage_position;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if (this.isVisible && edt_storage_position.getVisibility() == View.VISIBLE) {

        }
    }

    @OnClick({R.id.btn_commit, R.id.btn_edit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                disableEditMode();
                break;
            case R.id.btn_edit:
                editMode();
                break;
        }
    }
}
