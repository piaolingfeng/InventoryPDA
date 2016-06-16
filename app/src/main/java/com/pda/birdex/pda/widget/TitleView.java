package com.pda.birdex.pda.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.utils.Constant;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.List;

/**
 * Created by chuming.zhuang on 2016/4/12.
 */
public class TitleView extends RelativeLayout implements View.OnClickListener {
    Context mContext;
    View view;
    TextView title;
    ImageView menu;
    TextView save;
    ImageView back_iv;
    List<String> menuList;//menu菜单list
    PercentRelativeLayout prl_title;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (isInEditMode()) {
            return;
        }
        initView();
    }


    public TitleView(Context context) {
        super(context);
        mContext = context;
        if (isInEditMode()) {
            return;
        }
        initView();
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        if (isInEditMode()) {
            return;
        }
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_iv:
                if (mContext != null)
                    ((Activity) mContext).finish();
                break;
        }
    }

    PopupWindow mPopupWindow;



    private void initView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.title, null, false);
        title = (TextView) view.findViewById(R.id.title);
        menu = (ImageView) view.findViewById(R.id.menu);
        back_iv = (ImageView) view.findViewById(R.id.back_iv);
        back_iv.setOnClickListener(this);
        save = (TextView) view.findViewById(R.id.save);
        prl_title = (PercentRelativeLayout) view.findViewById(R.id.prl_title);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT>=19&& Constant.Status_Height!=0){
            prl_title.setPadding(0,Constant.Status_Height,0,0);
//            params.setMargins(0,Constant.Status_Height,0,0);
        }
        addView(view, params);
    }

    public void setMenuRecycleviewListener(OnClickListener listener) {
        menu.setOnClickListener(listener);
    }

    public View getMenuView() {
        return menu;
    }

    public void setMenuVisble(boolean flag) {
        if (flag)
            menu.setVisibility(View.VISIBLE);
        else {
            menu.setVisibility(View.INVISIBLE);
        }
    }

    public void setTitle(String titleText) {
        title.setText(titleText);
    }

    public void setSaveText(String text) {
        save.setVisibility(View.VISIBLE);
        save.setText(text);
    }

    public void setSaveCompoundDrawables(Drawable left,Drawable top,Drawable right,Drawable bottom){
        save.setCompoundDrawables(left,top,right,bottom);
    }

    public void setSaveListener(OnClickListener listener){
        save.setVisibility(VISIBLE);
        save.setOnClickListener(listener);
    }

    public void setBackground(int col){
        prl_title.setBackgroundColor(col);
    }

    public void setTitleTextcolor(int col){
        title.setTextColor(col);
    }

    public void setBackIv(Bitmap bitmap){
        back_iv.setImageBitmap(bitmap);
    }

    public void setBackIvVisble(boolean flag){
        if (flag){
            back_iv.setVisibility(View.VISIBLE);
        }else {
            back_iv.setVisibility(View.INVISIBLE);
        }
    }

    public void setMenu(int resId){
        menu.setImageResource(resId);
    }
    /*
     *库存titleview
     */
    public void setInventoryDetail(String titleStr,int colorID){
        setMenuVisble(false);
        title.setText("");
        prl_title.setBackgroundResource(colorID);
        save.setText(titleStr);
    }
}
