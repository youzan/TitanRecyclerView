package com.youzan.titan.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.youzan.titan.R;

/**
 * @author monster @Hangzhou Youzan Technology Co.Ltd
 * 2017/8/2
 */

public class EmptyViewHolder extends RecyclerView.ViewHolder {

    public FrameLayout container;

    public EmptyViewHolder(View itemView) {
        super(itemView);
        container = (FrameLayout) itemView.findViewById(R.id.container);
    }
}
