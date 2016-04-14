package com.youzan.titan.internal;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.youzan.titan.TitanAdapter;

/**
 * Created by monster on 16/1/15.
 */
public class ItemClickSupport {

    public interface OnItemClickListener {
        void onItemClick(RecyclerView recyclerView, View view, int position, long id);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(RecyclerView recyclerView, View view, int position, long id);
    }

    private RecyclerView recyclerView;
    private TitanAdapter adapter;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public ItemClickSupport(RecyclerView recyclerView, TitanAdapter adapter) {
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.adapter.setItemClickSupport(this);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (null != onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        if (null != onItemLongClickListener) {
            this.onItemLongClickListener = onItemLongClickListener;
        }
    }

    public void onItemClick(View v) {
        if (null != recyclerView && null != adapter && null != onItemClickListener) {
            final int position = recyclerView.getChildLayoutPosition(v);
            if (isMoreOrFooter(position)) {
                return;
            }

            final long id = adapter.getItemId(position);
            onItemClickListener.onItemClick(recyclerView, v, position - adapter.getCustomHeaderNum(), id);
        }
    }

    public boolean onItemLongClick(View v) {
        if (null != recyclerView && null != adapter && null != onItemLongClickListener) {
            final int position = recyclerView.getChildLayoutPosition(v);
            if (isMoreOrFooter(position)) {
                return false;
            }

            final long id = adapter.getItemId(position);

            return onItemLongClickListener.onItemLongClick(recyclerView, v, position - adapter.getCustomHeaderNum(), id);
        }

        return false;
    }

    private boolean isMoreOrFooter(int position) {
        return null == adapter.getData()
                || adapter.getData().size() + adapter.getCustomHeaderNum() <= position
                || adapter.getCustomHeaderNum() > position;
    }
}
