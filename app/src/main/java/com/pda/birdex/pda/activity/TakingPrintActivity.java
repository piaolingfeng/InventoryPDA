package com.pda.birdex.pda.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.widget.ClearEditText;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/6/17.
 */
public class TakingPrintActivity extends BarScanActivity implements View.OnClickListener {

    @Bind(R.id.titleView)
    com.pda.birdex.pda.widget.TitleView titleView;

    @Bind(R.id.code_et)
    com.pda.birdex.pda.widget.ClearEditText codeEt;

    @Bind(R.id.no_tv)
    TextView no_tv;

    @Bind(R.id.button_ll)
    com.zhy.android.percent.support.PercentLinearLayout button_ll;

    @Bind(R.id.main)
    com.zhy.android.percent.support.PercentRelativeLayout main;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_lanshouprint;
    }

    @Override
    public void barInitializeContentViews() {
        String title = getIntent().getExtras().getString("title");
        String name = getIntent().getExtras().getString("inputname");
        if(!TextUtils.isEmpty(title)) {
            titleView.setTitle(title);
        }
        if(!TextUtils.isEmpty(name)){
            no_tv.setText(name);
        }

        codeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(codeEt.getText())){
                    gone();
                }
            }
        });

        codeEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                visible();
                return false;
            }
        });

        main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                main.setFocusable(true);
                main.setFocusableInTouchMode(true);
                main.requestFocus();

                if(!TextUtils.isEmpty(codeEt.getText())){
                    if(button_ll.getVisibility() != View.VISIBLE) {
                        visible();
                    }
                }

                return false;
            }
        });
    }

    @Override
    public ClearEditText getClearEditText() {
        return codeEt;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if(!TextUtils.isEmpty(code)){
            visible();
        }
    }

    // Visible 动画
    private void visible(){
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                2.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        button_ll.startAnimation(mShowAction);
        button_ll.setVisibility(View.VISIBLE);
    }

    // gone 动画
    private void gone(){
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                2.0f);
        mHiddenAction.setDuration(500);
        button_ll.startAnimation(mHiddenAction);
        button_ll.setVisibility(View.GONE);
    }

    @OnClick({R.id.printsame, R.id.printnew})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.printsame:

                break;
            case R.id.printnew:

                break;
        }
    }
}
