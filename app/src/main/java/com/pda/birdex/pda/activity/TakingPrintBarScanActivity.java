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
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.PrintEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.HideSoftKeyboardUtil;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/6/17.
 */
public class TakingPrintBarScanActivity extends BasePrintBarScanActivity implements View.OnClickListener,RequestCallBackInterface {

    String tag = "TakingPrintBarScanActivity";

    @Bind(R.id.titleView)
    com.pda.birdex.pda.widget.TitleView title;

    @Bind(R.id.code_et)
    com.pda.birdex.pda.widget.ClearEditText codeEt;

    @Bind(R.id.no_tv)
    TextView no_tv;

    @Bind(R.id.button_ll)
    com.zhy.android.percent.support.PercentLinearLayout button_ll;

    @Bind(R.id.main)
    com.zhy.android.percent.support.PercentRelativeLayout main;

    @Override
    public int getPrintContentLayoutResId() {
        return R.layout.activity_lanshouprint;
    }

    @Override
    public void printInitializeContentViews() {
        String titleText = getIntent().getExtras().getString("title");
        String name = getIntent().getExtras().getString("inputname");
        if(!TextUtils.isEmpty(titleText)) {
            title.setTitle(titleText);
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
        HideSoftKeyboardUtil.hideSoftKeyboard(this);
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
                printSame();
                break;
            case R.id.printnew:

                break;
        }
    }

    //打印相同箱单
    private void printSame(){
        BirdApi.postCodeSamePrint(this,codeEt.getText().toString(),this,tag,true);
    }

    //打印相同箱单
    private void printNew(){
//        RequestParams params = new RequestParams();
//        params.put("count", 1);
//            params.put("owner", takingOrder.getPerson().getCo());
//            params.put("tkNo", takingOrder.getBaseInfo().getTakingOrderNo());
//        } else {
//        BirdApi.postCodePrint(this,);
    }

    @Override
    public TitleView printTitleView() {
        return title;
    }

    @Override
    public void successCallBack(JSONObject object) {
        PrintEntity entity = GsonHelper.getPerson(object.toString(), PrintEntity.class);
        if(entity!=null) {
            print(entity.getData());
        }else{
            T.showLong(this,getString(R.string.parse_error));
        }
    }

    private void print(List<String> list){
        for (String s : list){
            sendMessage(s);
        }
    }

    @Override
    public void errorCallBack(JSONObject object) {

    }
}
