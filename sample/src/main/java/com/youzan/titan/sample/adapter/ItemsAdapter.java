package com.youzan.titan.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youzan.titan.TitanAdapter;
import com.youzan.titan.sample.DemoItem;
import com.youzan.titan.sample.R;


/**
 * description
 *
 * @author monster @Hangzhou Youzan Technology Co.Ltd
 * @date 16/3/13
 */
public class ItemsAdapter extends TitanAdapter<DemoItem> {

    @Override
    protected RecyclerView.ViewHolder createVHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false), this);
    }

    @Override
    protected void showItemView(RecyclerView.ViewHolder holder, int position) {
        ((NormalTextViewHolder) holder).mTextView.setText(mData.get(position).title);
    }

    @Override
    public long getAdapterItemId(int position) {
        return 0;
    }

    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ItemsAdapter mAdapter;

        NormalTextViewHolder(View view, ItemsAdapter adapter) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text_view);
            mAdapter = adapter;
        }
    }
}
