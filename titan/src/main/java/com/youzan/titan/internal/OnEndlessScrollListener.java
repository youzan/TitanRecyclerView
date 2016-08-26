package com.youzan.titan.internal;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by monster on 15/12/28.
 */
public abstract class OnEndlessScrollListener extends RecyclerView.OnScrollListener {

    public boolean mIsLoading = true;

    /**
     * 最后一个可见的item的位置
     */
    private int mLastVisibleItemPos;

    /**
     * 滑动时是否加载数据
     * 滑动时默认不加载
     */
    private boolean mIsNeedLoadData = false;

    // The total number of items in the dataset after the last load
    public int mPreviousTotal = 0;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            mLastVisibleItemPos = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            mLastVisibleItemPos = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] positions = new int [((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(positions);
            mLastVisibleItemPos = Maths.max(positions);
        } else {
            throw new RuntimeException("Unsupported LayoutManager used. " +
                    "Valid ones are LinearLayoutManager, " +
                    "GridLayoutManager and StaggeredGridLayoutManager");
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        if (mIsLoading) {
            if (totalItemCount != mPreviousTotal) {
                mIsLoading = false;
                mPreviousTotal = totalItemCount;
            }
        }

        if (!mIsLoading && (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                (mLastVisibleItemPos) >= totalItemCount - 1)) {
            mIsLoading = true;
            onLoadMore();
        }

        boolean isScrolling = newState != RecyclerView.SCROLL_STATE_IDLE;
        if (mIsNeedLoadData && !isScrolling && recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        onScrollStateChanged(newState);
    }

    public void setNeedLoadData(boolean isNeedLoadData) {
        this.mIsNeedLoadData = isNeedLoadData;
    }

    public abstract void onLoadMore();

    /**
     * 默认滑动不加载数据
     * 提供滑动状态供重写
     *
     * @param newState 滑动状态
     */
    public abstract void onScrollStateChanged(int newState);
}
