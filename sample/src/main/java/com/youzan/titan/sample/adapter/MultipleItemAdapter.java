package com.youzan.titan.sample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youzan.titan.TitanAdapter;
import com.youzan.titan.sample.R;

import java.util.ArrayList;

/**
 * description
 *
 * @author monster @Hangzhou Youzan Technology Co.Ltd
 * @date 16/3/14
 */
public class MultipleItemAdapter extends TitanAdapter<String> {

    public enum ITEM_TYPE {
        ITEM_TYPE_IMAGE,
        ITEM_TYPE_TEXT
    }

    private LayoutInflater mLayoutInflater;
    private String[] mTitles;

    public MultipleItemAdapter(Context context) {
        mTitles = context.getResources().getStringArray(R.array.titles);
        mData = new ArrayList<>();
        for (String str: mTitles) {
            mData.add(str);
        }
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    protected RecyclerView.ViewHolder createVHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal()) {
            return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_image, parent, false));
        } else {
            return new TextViewHolder(mLayoutInflater.inflate(R.layout.item_text, parent, false));
        }
    }

    @Override
    protected void showItemView(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).mTextView.setText(mData.get(position));
        } else if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).mTextView.setText(mData.get(position));
        }
    }

    @Override
    protected int getAttackItemViewType(int position) {
        return position % 2 == 0 ? ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal() : ITEM_TYPE.ITEM_TYPE_TEXT.ordinal();
    }

    @Override
    public long getAdapterItemId(int position) {
        return position;
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        TextViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text_view);
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;

        ImageViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text_view);
            mImageView = (ImageView) view.findViewById(R.id.image_view);
        }
    }
}
