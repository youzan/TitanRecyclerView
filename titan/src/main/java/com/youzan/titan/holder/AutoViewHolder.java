package com.youzan.titan.holder;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by monster on 15/12/16.
 */
public class AutoViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> viewHolder;
    private View view;

    public AutoViewHolder(View itemView) {
        super(itemView);
        viewHolder = new SparseArray<>();
        view = itemView;
    }

    public <T extends View> T get(int id) {
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public View getConvertView() {
        return view;
    }

    public TextView getTextView(int id) {
        return get(id);
    }

    public Button getButton(int id) {
        return get(id);
    }

    public ImageView getImageView(int id) {
        return get(id);
    }

    public void setTextView(int id, CharSequence charSequence) {
        getTextView(id).setText(charSequence);
    }

    public void setTextView(int id, int resourceId) {
        getTextView(id).setText(resourceId);
    }
}
