package com.pda.birdex.pda.fragments;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.widget.ClearEditText;

/**
 * Created by chuming.zhuang on 2016/6/25.
 */
public class TakingToolPrintNumFragment extends BarScanBaseFragment {
    String tag="TakingToolPrintNumFragment";
    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_taking_tool_print_layout;
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
