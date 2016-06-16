package com.pda.birdex.pda.utils.decoration;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by weiyu.wei on 2015/12/4.
 */
public class MyLayoutManager extends LinearLayoutManager {
    private int itemRow = 1; //item的行数

    public MyLayoutManager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        View view = recycler.getViewForPosition(0);
        if (view != null) {
            measureChild(view, widthSpec, heightSpec);
            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
            int measuredHeight = view.getMeasuredHeight() * itemRow;
//            int measuredHeight =0;
//            for (int i = 0; i < itemRow; i++) {
//                if (recycler.getViewForPosition(i) != null)
//                    measuredHeight += recycler.getViewForPosition(i).getMeasuredHeight();
//            }

            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }

    public void setItemRow(int itemRow) {
        this.itemRow = itemRow;
    }
}
