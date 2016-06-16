package com.pda.birdex.pda.utils.decoration;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by weiyu.wei on 2015/12/10.
 */
public class MyGridManager extends GridLayoutManager {

    private int itemRow = 1; //item的行数

    public MyGridManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyGridManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public MyGridManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        View view = recycler.getViewForPosition(0);
        if (view != null) {
            measureChild(view, widthSpec, heightSpec);
            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
            int measuredHeight = view.getMeasuredHeight() * itemRow;
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }

    public void setItemRow(int itemRow) {
        this.itemRow = itemRow;
    }
}
