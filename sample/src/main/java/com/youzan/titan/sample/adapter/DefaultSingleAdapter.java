package com.youzan.titan.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youzan.titan.TitanAdapter;
import com.youzan.titan.sample.R;

/**
 * description
 *
 * @author monster @Hangzhou Youzan Technology Co.Ltd
 * @date 16/3/14
 */
public class DefaultSingleAdapter extends TitanAdapter<String> {
    protected int mCurrentSelect = -1;
    public boolean isEnableSelect = true;

    @Override
    protected RecyclerView.ViewHolder createVHolder(ViewGroup parent, int viewType) {
        return new SettingSingleSelectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_select, parent, false), this);
    }

    @Override
    protected void showItemView(RecyclerView.ViewHolder holder, int position) {
        ((SettingSingleSelectViewHolder) holder).bindViewData(mData.get(position), position);
    }

    @Override
    public long getAdapterItemId(int position) {
        return 0;
    }

    public boolean isSelectDate() {
        return mCurrentSelect >= 0;
    }

    public void setEnableSelect(boolean isEnableSelect) {
        this.isEnableSelect = isEnableSelect;
    }

    public int getCurrentSelect() {
        return mCurrentSelect;
    }

    public void setCurrentSelect(int currentSelect) {
        notifyItemChanged(mCurrentSelect);
        this.mCurrentSelect = currentSelect;
        notifyItemChanged(mCurrentSelect);
    }

    static class SettingSingleSelectViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;
        ImageView mIvCheck;
        DefaultSingleAdapter mAdapter;

        SettingSingleSelectViewHolder(View view, DefaultSingleAdapter mAdapter) {
            super(view);
            mIvCheck = (ImageView) view.findViewById(R.id.iv_check);
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            this.mAdapter = mAdapter;
        }

        public void bindViewData(String name, int position) {
            mIvCheck.setVisibility((position == mAdapter.mCurrentSelect) ? View.VISIBLE : View.GONE);
            mTvName.setText(name);
        }
    }
}
