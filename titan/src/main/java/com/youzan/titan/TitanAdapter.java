package com.youzan.titan;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youzan.titan.holder.FooterViewHolder;
import com.youzan.titan.holder.HeaderViewHolder;
import com.youzan.titan.internal.ItemClickSupport;
import com.youzan.titan.holder.LoadMoreViewHolder;

import java.util.List;

/**
 * Created by monster on 15/11/30.
 */
public abstract class TitanAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private final static int MORE_TYPE = 10000;
    private final static int HEADER_TYPE = 10001;
    private final static int FOOTER_TYPE = 10002;

    private View customLoadMoreView;
    private View headerView;
    private View footerView;
    private int loadMoreResourceId;
    private boolean hasMore = false;
    private boolean hasHeader = false;
    private boolean hasFooter = false;
    private ItemClickSupport itemClickSupport;

    protected List<T> mData;

    /**
     * 绑定itemlayout
     *
     * @param parent
     * @return
     */
    protected abstract RecyclerView.ViewHolder createVHolder(ViewGroup parent, int viewType);

    /**
     * 设置数据，展现内容
     *
     * @param holder
     * @param position
     */
    protected abstract void showItemView(RecyclerView.ViewHolder holder, int position);

    public abstract long getAdapterItemId(int position);

    public int getAdapterItemCount() {
        return null != mData ? mData.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (MORE_TYPE == viewType) {
            return getMoreViewHolder(parent);
        } else if (HEADER_TYPE == viewType) {
            return getHeaderViewHolder(parent);
        } else if (FOOTER_TYPE == viewType) {
            return getFooterViewHolder(parent);
        } else {
            holder = createVHolder(parent, viewType);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LoadMoreViewHolder) {
            holder.itemView.setVisibility(getItemCount() > getCustomsNum() && hasMore ? View.VISIBLE : View.GONE);
        } else if (holder instanceof HeaderViewHolder) {
            holder.itemView.setVisibility(hasHeader ? View.VISIBLE : View.GONE);
        } else if (holder instanceof FooterViewHolder) {
            holder.itemView.setVisibility(hasFooter ? View.VISIBLE : View.GONE);
        } else {
            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);
            showItemView(holder, hasHeader ? position - 1 : position);
        }
    }

    @Override
    public int getItemCount() {
        int customTypeCount = 0;
        if (hasMore) {
            customTypeCount++;
        }
        if (hasHeader) {
            customTypeCount++;
        }
        if (hasFooter) {
            customTypeCount++;
        }
        return getAdapterItemCount() + customTypeCount;
    }

    @Override
    public long getItemId(int position) {
        if (hasMore && 0 != position && getItemCount() - 1 == position) {
            return -1;
        }
        return getAdapterItemId(hasHeader ? position - 1 : position);
    }

    public T getItem(int position) {
        return null != mData && position <= mData.size() - 1 ? mData.get(position) : null;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader && 0 == position) {
            return HEADER_TYPE;
        }

        if (hasFooter && getItemCount() - 1 == position) {
            return FOOTER_TYPE;
        }

        if (hasMore) {
            if (!hasFooter && getItemCount() - 1 == position) {
                return MORE_TYPE;
            } else if (hasFooter && getItemCount() - 2 == position) {
                return MORE_TYPE;
            }
        }
        return getAttackItemViewType(hasHeader ? position - 1 : position);
    }

    /**
     * 使用attackview专用getItemViewType
     *
     * @param position
     * @return
     */
    protected int getAttackItemViewType(int position) {
        return 0;
    }

    protected RecyclerView.ViewHolder getMoreViewHolder(ViewGroup parent) {
        if (null != customLoadMoreView) {
            return new LoadMoreViewHolder(customLoadMoreView);
        }
        if (0 != loadMoreResourceId) {
            this.customLoadMoreView = LayoutInflater.from(parent.getContext()).inflate(loadMoreResourceId, parent, false);
        } else {
            this.customLoadMoreView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_default_more_view, parent, false);
        }
        return new LoadMoreViewHolder(customLoadMoreView);
    }

    protected RecyclerView.ViewHolder getHeaderViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(headerView);
    }

    protected RecyclerView.ViewHolder getFooterViewHolder(ViewGroup parent) {
        return new FooterViewHolder(footerView);
    }

    public void setCustomLoadMoreView(View customView) {
        loadMoreResourceId = 0;
        customLoadMoreView = customView;
    }

    public void setCustomLoadMoreView(int resourceId) {
        customLoadMoreView = null;
        loadMoreResourceId = resourceId;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        this.hasHeader = true;
    }

    public void removeHeaderView() {
        this.headerView = null;
        this.hasHeader = false;
        notifyDataSetChanged();
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
        this.hasFooter = true;
    }

    public void removeFooterView() {
        this.footerView = null;
        this.hasFooter = false;
        notifyDataSetChanged();
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    public boolean hasMore() {
        return this.hasMore;
    }

    /**
     * 获取自定义的数量
     * notifyItemInserted等一系列的操作
     * 都是针对recyclerview的所有item
     * 只针对mData操作时需考虑自定义数
     *
     * @return
     */
    public int getCustomHeaderNum() {
        int customHeaderNum = 0;
        if (hasHeader) {
            ++customHeaderNum;
        }
        return customHeaderNum;
    }

    /**
     * 获取所有自定义数量
     *
     * @return
     */
    private int getCustomsNum() {
        int customs = 0;
        customs = hasFooter ? ++customs : customs;
        customs = hasHeader ? ++customs : customs;
        customs = hasMore ? ++customs : customs;
        return customs;
    }

    public void addDataEnd(T data) {
        if (data != null && null != mData) {
            int startIndex = this.mData.size();
            this.mData.add(data);
            filterData(this.mData);
            notifyItemInserted(startIndex + getCustomHeaderNum());
        }
    }

    public void addData(T data, int index) {
        if (null != data && null != mData && 0 <= index && getAdapterItemCount() >= index) {
            this.mData.add(index, data);
            filterData(mData);
            notifyItemInserted(index + getCustomHeaderNum());
        }
    }

    public void addDataEnd(List<T> data) {
        if (data != null && null != mData && data.size() > 0 && data != this.mData) {
            int startIndex = this.mData.size();
            this.mData.addAll(data);
            filterData(this.mData);
            notifyItemRangeInserted(startIndex + getCustomHeaderNum(), data.size());
        }
    }

    public void addDataTop(List<T> data) {
        if (null != data && null != mData && 0 < data.size() && data != this.mData) {
            this.mData.addAll(0, data);
            filterData(mData);
            notifyItemRangeInserted(getCustomHeaderNum(), data.size());
        }
    }

    public void addDataTop(T data) {
        if (null != data && null != mData) {
            this.mData.add(0, data);
            filterData(mData);
            notifyItemInserted(getCustomHeaderNum());
        }
    }

    public void remove(T data) {
        if (data != null && null != mData) {
            if (mData.contains(data)) {
                int startIndex = this.mData.indexOf(data);
                mData.remove(data);
                if (startIndex != -1) {
                    filterData(this.mData);
                    notifyItemRemoved(startIndex + getCustomHeaderNum());
                }

            }
        }
    }

    public void remove(int index) {
        if (null != mData && 0 <= index && getAdapterItemCount() >= index) {
            mData.remove(index);
            filterData(mData);
            notifyItemRemoved(index + getCustomHeaderNum());
        }
    }

    public void update(T data) {
        if (data != null && null != mData) {
            int startIndex = this.mData.indexOf(data);
            if (startIndex != -1) {
                mData.set(startIndex, data);
            }
            notifyItemChanged(startIndex + getCustomHeaderNum());
        }
    }

    public void update(int index, T data) {
        if (null != mData && index >= 0 && index < mData.size() && data != null) {
            mData.set(index, data);
            filterData(this.mData);
            notifyItemChanged(index + getCustomHeaderNum());
        }
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(@NonNull  List<T> data) {

        notifyDataSetChanged();
    }

    public void clearData() {
        if (null != mData) {
            this.mData.clear();
            notifyDataSetChanged();
        }
    }

    public void setItemClickSupport(ItemClickSupport itemClickSupport) {
        this.itemClickSupport = itemClickSupport;
    }

    public boolean hasHeader() {
        return hasHeader;
    }

    public boolean hasFooter() {
        return hasFooter;
    }

    public void filterData(List<T> data) {
        // Dummy
    }

    @Override
    public void onClick(View v) {
        if (null != itemClickSupport) {
            itemClickSupport.onItemClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (null != itemClickSupport) {
            return itemClickSupport.onItemLongClick(v);
        }
        return false;
    }
}
