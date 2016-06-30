package com.pda.birdex.pda.widget.pullreflash;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by chuming.zhuang on 2016/4/21.
 */
public class PullableRecycleView extends RecyclerView implements Pullable {
    private boolean canpullup = false;
    private boolean canpulldown = false;

    public PullableRecycleView(Context context) {
        super(context);
    }

    public PullableRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        LayoutManager layoutManager = getLayoutManager();
        int firstVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
            firstVisibleItemPosition = findMin(into);
        } else {
            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        if (getChildCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (firstVisibleItemPosition == 0
                && getChildAt(0).getTop() >= 0) {
            // 滑到顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp() {
        LayoutManager layoutManager = getLayoutManager();
        int lastVisibleItemPosition;
        int firstVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
            lastVisibleItemPosition = findMax(into);
            firstVisibleItemPosition = findMin(into);
        } else {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        if (lastVisibleItemPosition == (getChildCount() - 1)) {
            // 滑到底部了
            if (getChildAt(lastVisibleItemPosition - firstVisibleItemPosition) != null
                    && getChildAt(
                    lastVisibleItemPosition
                            - firstVisibleItemPosition).getBottom() <= getMeasuredHeight())
                return canpullup;
        }
        return false;
    }

    public void setCanPullDown(boolean flag) {
        canpulldown = flag;
    }

    public void setCanPullUp(boolean flag) {
        canpullup = flag;
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }
}
