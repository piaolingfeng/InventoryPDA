package com.pda.birdex.pda.fragments;

import com.pda.birdex.pda.widget.ClearEditText;

/**
 * Created by chuming.zhuang on 2016/7/8.
 * 追踪箱号
 */
public class StorageTrackFragment extends BarScanBaseFragment {
    @Override
    public int getbarContentLayoutResId() {
        return 0;
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
