package com.pda.birdex.pda.widget.pullreflash;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable
{
	private int downX;
	private int downY;
	private int mTouchSlop;
	public PullableScrollView(Context context)
	{
		super(context);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public PullableScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public PullableScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	public boolean canPullDown()
	{
		if (getScrollY() == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean canPullUp()
	{
//		if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
//			return true;
//		else
			return false;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int action = e.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				downX = (int) e.getRawX();
				downY = (int) e.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				int moveY = (int) e.getRawY();
				if (Math.abs(moveY - downY) > mTouchSlop) {
					return true;
				}
		}
		return super.onInterceptTouchEvent(e);
	}
}
