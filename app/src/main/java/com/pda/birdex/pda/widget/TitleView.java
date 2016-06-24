package com.pda.birdex.pda.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.CommonSimpleAdapter;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.interfaces.TitleBarBackInterface;
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

    TitleBarBackInterface backInterface;

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
                    if (backInterface != null) {
                        backInterface.onBackClick();
                    } else
                        ((Activity) mContext).finish();
                break;
            case R.id.save:
//                show
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
        save.setOnClickListener(this);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= 19 && Constant.Status_Height != 0) {
            prl_title.setPadding(0, Constant.Status_Height, 0, 0);
//            params.setMargins(0,Constant.Status_Height,0,0);
        }
        addView(view, params);
    }

    public void setMenuRecycleviewListener(OnClickListener listener) {
        menu.setOnClickListener(listener);
    }

    public void setBackInterface(TitleBarBackInterface backInterface) {
        this.backInterface = backInterface;
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

    public void showMenuWindow(View viewID, final List<String> list, final int w) {
        CommonSimpleAdapter adapter = new CommonSimpleAdapter(mContext, list);
        adapter.setOnRecyclerViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }

            }
        });
        showPopupWindow(viewID, w, adapter);
    }

    private void showPopupWindow(View viewID, int w, RecyclerView.Adapter adapter) {
        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View popWindow = LayoutInflater.from(mContext).inflate(R.layout.common_recycleview_layout, null);
//        popWindow.setBackgroundColor(Color.TRANSPARENT);
        RecyclerView rcy = (RecyclerView) popWindow.findViewById(R.id.rcy);
        rcy.setLayoutManager(new LinearLayoutManager(mContext));
        rcy.setAdapter(adapter);
        //        int width = getWindowManager().getDefaultDisplay().getWidth();
        int width = viewID.getWidth();
        mPopupWindow = new PopupWindow(popWindow, width / w, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();
        if(rcy!=null){
            rcy.setBackgroundResource(R.drawable.pop_right_bg);
        }
        if (w > 1)
            mPopupWindow.showAsDropDown(viewID, (width / w) * (w - 1), 0);
        else
            mPopupWindow.showAsDropDown(viewID, 0, 0);
    }

    public void setSaveCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        save.setCompoundDrawables(left, top, right, bottom);
    }

    public void setSaveListener(OnClickListener listener) {
        save.setVisibility(VISIBLE);
        save.setOnClickListener(listener);
    }

    public void setBackground(int col) {
        prl_title.setBackgroundColor(col);
    }

    public void setTitleTextcolor(int col) {
        title.setTextColor(col);
    }

    public void setBackIv(Bitmap bitmap) {
        back_iv.setImageBitmap(bitmap);
    }

    public void setBackIvVisble(boolean flag) {
        if (flag) {
            back_iv.setVisibility(View.VISIBLE);
        } else {
            back_iv.setVisibility(View.INVISIBLE);
        }
    }

    public void setMenu(int resId) {
        menu.setImageResource(resId);
    }

    /*
     *库存titleview
     */
    public void setInventoryDetail(String titleStr, int colorID) {
        setMenuVisble(false);
        title.setText("");
        prl_title.setBackgroundResource(colorID);
        save.setText(titleStr);
    }
}
