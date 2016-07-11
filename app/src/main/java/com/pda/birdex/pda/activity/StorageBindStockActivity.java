package com.pda.birdex.pda.activity;

import android.view.View;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import butterknife.Bind;

/**
 * Created by hyj on 2016/7/11.
 */
public class StorageBindStockActivity extends BarScanActivity{

    // 入库单号
    @Bind(R.id.edt_storage_num)
    ClearEditText edt_storage_num;

    // 容器号
    @Bind(R.id.edt_storage_container)
    ClearEditText edt_storage_container;

    @Bind(R.id.titleView)
    TitleView titleView;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_storage_bindstock;
    }

    @Override
    public void barInitializeContentViews() {
        titleView.setTitle(getString(R.string.storage_bind_stock));

        edt_storage_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_storage_num.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_storage_num);
                }
            }
        });
        edt_storage_container.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_storage_container.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_storage_container);
                }
            }
        });
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_storage_num;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if (edt_storage_num.hasFocus()) {
            setEdt_input(edt_storage_container);
            edt_storage_container.requestFocus();
        } else {
            setEdt_input(edt_storage_num);
            edt_storage_num.requestFocus();
        }
    }
}
