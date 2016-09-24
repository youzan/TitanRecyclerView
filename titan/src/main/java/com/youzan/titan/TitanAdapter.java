package com.youzan.titan;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youzan.titan.holder.AutoViewHolder;
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

    public final static int HEADER_TYPE = Integer.MIN_VALUE;
    public final static int FOOTER_TYPE = Integer.MAX_VALUE - 1;
    public final static int MORE_TYPE = Integer.MAX_VALUE;
    public final static int EMPTY_TYPE = Integer.MAX_VALUE - 2;

    private View mCustomLoadMoreView;
    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;

    private boolean mIsHeadViewEmpty = false;
    private boolean mIsFootViewEmpty = false;
    private boolean mIsEmptyViewEnable = false;

    @LayoutRes private int mLoadMoreResId;

    private boolean mHasMore;
    private boolean mHasHeader;
    private boolean mHasFooter;

    protected List<T> mData;

    private ItemClickSupport mItemClickSupport;

    /**
     * Create view holder according to view types.
     * @param parent the parent view group
     * @param viewType the view type
     * @return the created view holder
     */
    protected abstract RecyclerView.ViewHolder createVHolder(ViewGroup parent, int viewType);

    /**
     * Show item view.
     * @param holder the view holder
     * @param position the position of the view holder
     */
    protected abstract void showItemView(RecyclerView.ViewHolder holder, int position);

    /**
     * Get adpater item id by position.
     * @param position item position
     * @return adapter item id
     */
    public abstract long getAdapterItemId(int position);

    /**
     *  Get the count of adapter items.
     * @return the count of adapter items
     */
    public int getAdapterItemCount() {
        return null != mData ? mData.size() : 0;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case MORE_TYPE:
                holder = getMoreViewHolder(parent);
                break;
            case HEADER_TYPE:
                holder = getHeaderViewHolder(parent);
                break;
            case FOOTER_TYPE:
                holder = getFooterViewHolder(parent);
                break;
            case EMPTY_TYPE:
                holder = new AutoViewHolder(mEmptyView);
                break;
            default:
                holder = createVHolder(parent, viewType);
                break;
        }
        return holder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = holder.getItemViewType();
        switch (viewType) {
            case MORE_TYPE:
                holder.itemView.setVisibility(getItemCount() > getCustomsNum() && mHasMore ? View.VISIBLE : View.GONE);
                break;
            case HEADER_TYPE:
                holder.itemView.setVisibility(mHasHeader ? View.VISIBLE : View.GONE);
                break;
            case FOOTER_TYPE:
                holder.itemView.setVisibility(mHasFooter ? View.VISIBLE : View.GONE);
                break;
            case EMPTY_TYPE:
                break;
            default:
                holder.itemView.setOnClickListener(this);
                holder.itemView.setOnLongClickListener(this);
                showItemView(holder, mHasHeader ? position - 1 : position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        int customTypeCount = 0;
        if (mHasMore && null != mData && 0 < mData.size()) {
            customTypeCount++;
        }
        if (mHasHeader) {
            customTypeCount++;
        }
        if (mHasFooter) {
            customTypeCount++;
        }

        if ((null == mData || 0 == mData.size()) && mIsEmptyViewEnable) {
            customTypeCount = 1;
            if (!mIsFootViewEmpty && mHasFooter) {
                customTypeCount++;
            }
            if (!mIsHeadViewEmpty && mHasHeader) {
                customTypeCount++;
            }
        }

        return getAdapterItemCount() + customTypeCount;
    }

    @Override
    public long getItemId(int position) {
        if (mHasMore && 0 != position && getItemCount() - 1 == position) {
            return -1;
        }
        return getAdapterItemId(mHasHeader ? position - 1 : position);
    }

    public T getItem(int position) {
        return null != mData && position <= mData.size() - 1 ? mData.get(position) : null;
    }

    @Override
    public int getItemViewType(int position) {

        boolean isDataEmpty = null == mData || 0 == mData.size();

        if (mIsEmptyViewEnable && isDataEmpty) {

            if (!mIsHeadViewEmpty && mHasHeader && 0 == position) {
                return HEADER_TYPE;
            }

            if (!mIsFootViewEmpty && mHasFooter && getItemCount() - 1 == position) {
                return FOOTER_TYPE;
            }

            return EMPTY_TYPE;

        } else {

            if (mHasHeader && 0 == position) {
                return HEADER_TYPE;
            }

            if (mHasFooter && getItemCount() - 1 == position) {
                return FOOTER_TYPE;
            }

            if (mHasMore && !isDataEmpty) {
                if (!mHasFooter && getItemCount() - 1 == position) {
                    return MORE_TYPE;
                } else if (mHasFooter && getItemCount() - 2 == position) {
                    return MORE_TYPE;
                }
            }
        }

        return getAttackItemViewType(mHasHeader ? position - 1 : position);
    }

    /**
     * 设置emptyView
     * @param isHeadViewEmpty is headerview empty that show emptyview
     * @param isFootViewEmpty is footerview empty that show emptyview
     * @param emptyView custom empty view
     */
    public void setEmptyView(boolean isHeadViewEmpty, boolean isFootViewEmpty, View emptyView) {
        mIsHeadViewEmpty = isHeadViewEmpty;
        mIsFootViewEmpty = isFootViewEmpty;
        mEmptyView = emptyView;
        mIsEmptyViewEnable = true;
    }

    /**
     * 设置emptyView isHeadViewEmpty and isFootViewEmpty is true
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        setEmptyView(true, true, emptyView);
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * 使用TitanRecyclerView专用getItemViewType
     *
     * @param position
     * @return
     */
    protected int getAttackItemViewType(int position) {
        return 0;
    }

    protected RecyclerView.ViewHolder getMoreViewHolder(ViewGroup parent) {
        if (null != mCustomLoadMoreView) {
            return new LoadMoreViewHolder(mCustomLoadMoreView);
        }
        if (0 != mLoadMoreResId) {
            mCustomLoadMoreView = LayoutInflater.from(parent.getContext()).inflate(mLoadMoreResId, parent, false);
        } else {
            mCustomLoadMoreView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_default_more_view, parent, false);
        }
        return new LoadMoreViewHolder(mCustomLoadMoreView);
    }

    protected RecyclerView.ViewHolder getHeaderViewHolder(ViewGroup parent) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mHeaderView.setLayoutParams(layoutParams);
        return new HeaderViewHolder(mHeaderView);
    }

    protected RecyclerView.ViewHolder getFooterViewHolder(ViewGroup parent) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mFooterView.setLayoutParams(layoutParams);
        return new FooterViewHolder(mFooterView);
    }

    public void setCustomLoadMoreView(View customView) {
        mLoadMoreResId = 0;
        mCustomLoadMoreView = customView;
    }

    public void setCustomLoadMoreView(int resourceId) {
        mCustomLoadMoreView = null;
        mLoadMoreResId = resourceId;
    }

    public void setHeaderView(View headerView) {
        this.mHeaderView = headerView;
        this.mHasHeader = true;
    }

    public void removeHeaderView() {
        this.mHeaderView = null;
        this.mHasHeader = false;
        notifyDataSetChanged();
    }

    public void setFooterView(View footerView) {
        this.mFooterView = footerView;
        this.mHasFooter = true;
    }

    public void removeFooterView() {
        this.mFooterView = null;
        this.mHasFooter = false;
        notifyDataSetChanged();
    }

    public void setHasMore(boolean hasMore) {
        this.mHasMore = hasMore;
        notifyDataSetChanged();
    }

    public boolean hasMore() {
        return this.mHasMore;
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
        if (mHasHeader) {
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
        customs = mHasFooter ? ++customs : customs;
        customs = mHasHeader ? ++customs : customs;
        customs = mHasMore ? ++customs : customs;
        if ((null == mData || 0 == mData.size()) && mIsEmptyViewEnable) {
            customs = 1;
            customs = mHasFooter && !mIsFootViewEmpty ? ++customs : customs;
            customs = mHasHeader && !mIsHeadViewEmpty ? ++customs : customs;
        }
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
        mData = data;
        notifyDataSetChanged();
    }

    public void clearData() {
        if (null != mData) {
            this.mData.clear();
            notifyDataSetChanged();
        }
    }

    public void setItemClickSupport(ItemClickSupport itemClickSupport) {
        this.mItemClickSupport = itemClickSupport;
    }

    public boolean hasHeader() {
        return mHasHeader;
    }

    public boolean hasFooter() {
        return mHasFooter;
    }

    public void filterData(List<T> data) {
        // Dummy
    }

    @Override
    public void onClick(View v) {
        if (null != mItemClickSupport) {
            mItemClickSupport.onItemClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (null != mItemClickSupport) {
            return mItemClickSupport.onItemLongClick(v);
        }
        return false;
    }
}
