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
 * 拍照
 */
public class StoragePhotoFragment extends BarScanBaseFragment implements View.OnClickListener {
    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;
    @Bind(R.id.btn_edit)
    Button btn_edit;
    @Bind(R.id.edt_upc)
    ClearEditText edt_upc;
    @Bind(R.id.tv_upc)
    TextView tv_upc;
    @Bind(R.id.btn_commit)
    Button btn_commit;

    // 图片 fragment
    private PhotoFragment photoFragment;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_storage_photo_layout;
    }

    @Override
    public void barInitializeContentViews() {
        edt_upc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    String string = v.getText().toString();
                    ClearEditTextCallBack(string);
                }
                return false;
            }
        });

        photoFragment = new PhotoFragment();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.framelayout_1, photoFragment).commit();
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_upc;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if (this.isVisible && edt_upc.getVisibility() == View.VISIBLE) {

        }
    }

    private void commit() {

    }

    private void editMode() {
        btn_edit.setVisibility(View.INVISIBLE);
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
        edt_upc.setVisibility(View.VISIBLE);
        tv_upc.setVisibility(View.INVISIBLE);
    }

    private void disableEditMode() {
        btn_edit.setVisibility(View.VISIBLE);
        btn_commit.setClickable(false);
        btn_commit.setBackgroundResource(R.drawable.rect_fullgray);
        btn_commit.setTextColor(getResources().getColor(R.color.white));
        edt_upc.setVisibility(View.INVISIBLE);
        tv_upc.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.btn_commit, R.id.btn_edit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                commit();
                disableEditMode();
                break;
            case R.id.btn_edit:
                editMode();
                break;
        }
    }
}
