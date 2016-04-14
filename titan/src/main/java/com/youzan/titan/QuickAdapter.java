package com.youzan.titan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youzan.titan.holder.AutoViewHolder;

import java.util.List;

/**
 * Created by monster on 15/12/16.
 */
public class QuickAdapter<T> extends TitanAdapter<T> {

    private int layoutId;

    public QuickAdapter(int layoutId) {
        this.layoutId = layoutId;
    }

    public QuickAdapter(int layoutId, List<T> mData) {
        this.layoutId = layoutId;
        this.mData = null;
        this.mData = mData;
    }

    @Override
    protected RecyclerView.ViewHolder createVHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new AutoViewHolder(view);
    }

    @Override
    protected void showItemView(RecyclerView.ViewHolder holder, int position) {
        bindView((AutoViewHolder) holder, position, getItem(position));
    }

    @Override
    public int getAdapterItemCount() {
        return null != mData ? mData.size() : 0;
    }

    @Override
    public long getAdapterItemId(int position) {
        return position;
    }

    public T getItem(int position) {
        return null != mData && 0 < mData.size() && 0 <= position ? mData.get(position) : null;
    }

    public void bindView(AutoViewHolder holder, int position, T model) {
    }
}
