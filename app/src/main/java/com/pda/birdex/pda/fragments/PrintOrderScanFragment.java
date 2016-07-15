package com.pda.birdex.pda.fragments;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.widget.ClearEditText;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/7/15.
 */
public class PrintOrderScanFragment extends BarScanBaseFragment {
    @Bind(R.id.edt_order)
    ClearEditText edt_order;
    @Bind(R.id.no_tv)
    TextView no_tv;
    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_print_scan_layout;
    }

    @Override
    public void barInitializeContentViews() {
        String name = getActivity().getIntent().getExtras().getString("inputname");
        if (!TextUtils.isEmpty(name)) {
            no_tv.setText(name);
        }
        edt_order.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == KeyEvent.KEYCODE_ENTER || actionId == KeyEvent.KEYCODE_UNKNOWN || actionId == KeyEvent.KEYCODE_ENDCALL)
                    ClearEditTextCallBack(v.getText().toString());
                return false;
            }
        });
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_order;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if(!this.isHidden()){
            bus.post(code);
        }
    }
}
