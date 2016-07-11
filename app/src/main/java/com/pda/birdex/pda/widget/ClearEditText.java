package com.pda.birdex.pda.widget;


import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.utils.SoftKeyboardUtil;


/**
 * @说明： 自定义带删除按钮的EditText
 */
public class ClearEditText extends EditText implements View.OnFocusChangeListener,
        TextWatcher {
    //EditText右侧的删除按钮
    private Drawable mClearDrawable;
    private boolean hasFoucs;
    private OnClearETChangeListener listener;
    //设置清除文本时间
    private OnClearTextListener clearTextListener;

    public ClearEditText(Context context) {
        this(context, null);
    }

    private Context mContext;

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
        mContext = context;
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,获取图片的顺序是左上右下（0,1,2,3,）
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(
                    R.drawable.delete_edt);
        }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());
        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    private int count = 0;
    private long firClick = 0;
    private long secClick = 0;

    /* @说明：isInnerWidth, isInnerHeight为ture，触摸点在删除图标之内，则视为点击了删除图标
     * event.getX() 获取相对应自身左上角的X坐标
     * event.getY() 获取相对应自身左上角的Y坐标
     * getWidth() 获取控件的宽度
     * getHeight() 获取控件的高度
     * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
     * getPaddingRight() 获取删除图标右边缘到控件右边缘的距离
     * isInnerWidth:
     *  getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
     *  getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     * isInnerHeight:
     *  distance 删除图标顶部边缘到控件顶部边缘的距离
     *  distance + height 删除图标底部边缘到控件顶部边缘的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = getCompoundDrawables()[2].getBounds();
                int height = rect.height();
                int distance = (getHeight() - height) / 2;
                boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight()) && x < (getWidth() - getPaddingRight());
                boolean isInnerHeight = y > distance && y < (distance + height);
                if (isInnerWidth && isInnerHeight) {
                    this.setText("");
                    if (clearTextListener != null) {
                        clearTextListener.clearTextListenr();
                    }
                }
            }
        }
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            count++;
            if (count == 1) {
                firClick = System.currentTimeMillis();
                SoftKeyboardUtil.hideSoftKeyboard(mContext, this);
                this.requestFocus();
            } else if (count == 2) {
                secClick = System.currentTimeMillis();
                if (secClick - firClick < 500) {
                    //双击事件
                    SoftKeyboardUtil.openSoftKeyboard(mContext, this);
                }
                count = 0;
                firClick = 0;
                secClick = 0;
                this.requestFocus();
            }
        }
        return true;
    }

    /**
     * 当ClearEditText焦点发生变化的时候，
     * 输入长度为零，隐藏删除图标，否则，显示删除图标
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    // 重写过 onFocusChange 后，需要调用
    public void overrideOnFocusChange(boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    private String preString = "";

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
        if (preString != null) {
            if (!preString.equals(s.toString())) {
                preString = s.toString();
                if (listener != null) {
                    listener.textChange(s);
                    if (count >= 1) {
                        setSelection(start);
                    } else {
                        setSelection(start + 1);
                    }
                }
            }
        } else {
            preString = s.toString();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface OnClearETChangeListener {
        public void textChange(CharSequence text);
    }

    public void setOnClearETChangeListener(OnClearETChangeListener listener) {
        this.listener = listener;
    }

    public interface OnClearTextListener {
        void clearTextListenr();
    }

    public void setOnClearTextListener(OnClearTextListener listener) {
        if (listener != null) {
            this.clearTextListener = listener;
        }
    }
}