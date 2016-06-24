package com.pda.birdex.pda.fragments;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.widget.ClearEditText;

/**
 * Created by chuming.zhuang on 2016/6/24.
 */
public class CountToolPhotoFragment extends BarScanBaseFragment {
    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_count_tool_photo_layout;
    }

    @Override
    public void barInitializeContentViews() {

    }

    @Override
    public ClearEditText getClearEditText() {
        return null;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }
}
