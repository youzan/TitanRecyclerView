package com.youzan.titan.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youzan.titan.TitanAdapter;
import com.youzan.titan.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author monster @Hangzhou Youzan Technology Co.Ltd
 * @date 16/3/14
 */
public class DefaultMultipleAdapter extends TitanAdapter<String> {

    private List<String> selectedItems = new ArrayList<>();
    private boolean isActionModeShow = false;

    @Override
    protected RecyclerView.ViewHolder createVHolder(ViewGroup parent, int viewType) {
        return new MultiSettingSelectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_select, parent, false), this);
    }

    @Override
    protected void showItemView(RecyclerView.ViewHolder holder, int position) {
        ((MultiSettingSelectViewHolder) holder).bindViewData(mData.get(position));
    }

    @Override
    public long getAdapterItemId(int position) {
        return 0;
    }

    public void addSelectPosition(String selectedItem) {
        if (null != selectedItems && !selectedItems.contains(selectedItem)) {
            selectedItems.add(selectedItem);
        }
    }

    public void removeSelectPosition(String selectedItem) {
        if (null != selectedItems && selectedItems.contains(selectedItem)) {
            selectedItems.remove(selectedItem);
        }
    }

    public List<String> getMultiSelectItems() {
        return null == selectedItems ? new ArrayList<String>() : selectedItems;
    }

    public boolean isSelected(String selectedItem) {
        if (null != selectedItems) {
            return selectedItems.contains(selectedItem);
        }
        return false;
    }

    public boolean isActionModeShow() {
        return isActionModeShow;
    }

    public void setActionModeShow(boolean actionModeShow) {
        isActionModeShow = actionModeShow;
    }

    static class MultiSettingSelectViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;
        ImageView mIvCheck;

        DefaultMultipleAdapter mAdapter;

        MultiSettingSelectViewHolder(View view, DefaultMultipleAdapter adapter) {
            super(view);
            mIvCheck = (ImageView) view.findViewById(R.id.iv_check);
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mAdapter = adapter;
        }

        public void bindViewData(String name) {
            mIvCheck.setVisibility((mAdapter.isSelected(name)) ? View.VISIBLE : View.GONE);
            mTvName.setText(name);
        }
    }
}
