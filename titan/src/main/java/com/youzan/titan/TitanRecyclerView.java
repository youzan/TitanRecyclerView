package com.youzan.titan;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.youzan.titan.internal.ItemClickSupport;
import com.youzan.titan.internal.OnEndlessScrollListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * TitanRecyclerView is beyond RecyclerView.
 *
 * @author monster @Hangzhou Youzan Technology Co.Ltd
 * @date 16/3/10
 */
public class TitanRecyclerView extends RecyclerView {

    @IntDef({TOP, BOTTOM, LEFT, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation { }

    public static final int TOP = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    public interface OnLayoutChangeListener {
        void onLayoutChange();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnScrollStateChangedListener {
        void onStateChanged(int newState);
    }

    private OnEndlessScrollListener mOnEndlessScrollListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private TitanAdapter mTitanAdapter;
    private OnScrollStateChangedListener mOnScrollStateChangedListener;
    private ItemClickSupport mItemClickSupport;
    private ItemClickSupport.OnItemClickListener mOnItemClickListener;
    private ItemClickSupport.OnItemLongClickListener mOnItemLongClickListener;


    @LayoutRes protected int mLoadMoreResourceId;
    protected View mCustomLoadMoreView;
    protected int mScrollbarStyle;
    private boolean mHasMore;

    public TitanRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public TitanRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init(context);
    }

    public TitanRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
        init(context);
    }

    protected void init(Context context) {
        setLayoutManager(new LinearLayoutManager(context));
        if (mLoadMoreResourceId == 0) {
            mLoadMoreResourceId = R.layout.layout_default_more_view;
        }
        addEndlessScrollListener();
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitanRecyclerView);

        try {
            mLoadMoreResourceId = typedArray.getResourceId(R.styleable.TitanRecyclerView_layout_more,
                    R.layout.layout_default_more_view);
            mScrollbarStyle = typedArray.getInt(R.styleable.TitanRecyclerView_scrollbarStyle, -1);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * 滑至底部监听
     * 实现loadmore
     */
    private void addEndlessScrollListener() {
        mOnEndlessScrollListener = new OnEndlessScrollListener() {
            @Override
            public void onLoadMore() {
                if (mOnLoadMoreListener != null && mTitanAdapter != null
                        && mTitanAdapter.hasMore()) {
                    mOnLoadMoreListener.onLoadMore();
                }
            }

            @Override
            public void onScrollStateChanged(int newState) {
                if (mOnScrollStateChangedListener != null) {
                    mOnScrollStateChangedListener.onStateChanged(newState);
                }
            }
        };
        addOnScrollListener(mOnEndlessScrollListener);
    }


    private void setupAdapter() {
        if (mTitanAdapter != null) {
            mTitanAdapter.setHasMore(mHasMore);
            if (null != mCustomLoadMoreView) {
                mTitanAdapter.setCustomLoadMoreView(mCustomLoadMoreView);
            } else {
                mTitanAdapter.setCustomLoadMoreView(mLoadMoreResourceId);
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof TitanAdapter) {
            mTitanAdapter = (TitanAdapter) adapter;
            mItemClickSupport = new ItemClickSupport(this, (TitanAdapter) adapter);
            mItemClickSupport.setOnItemClickListener(mOnItemClickListener);
            mItemClickSupport.setOnItemLongClickListener(mOnItemLongClickListener);
            optimizeGridLayout(getLayoutManager());
            setupLoadMore();
            setupAdapter();
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        optimizeGridLayout(layout);
        setupLoadMore();
        setupAdapter();
    }

    public void setOnLoadMoreListener(@NonNull OnLoadMoreListener onLoadMoreListener) {
        mHasMore = true;
        mOnLoadMoreListener = onLoadMoreListener;
        setupAdapter();
    }

    public void setHasMore(boolean hasMore) {
        mHasMore = hasMore;
        setupAdapter();
    }

    public void setCustomLoadMoreView(View customView) {
        mLoadMoreResourceId = 0;
        mCustomLoadMoreView = customView;
        setupAdapter();
    }

    public void setCustomLoadMoreView(int resourceId) {
        this.mCustomLoadMoreView = null;
        this.mLoadMoreResourceId = resourceId;
        setupAdapter();
    }

    public void setOnItemClickListener(@NonNull ItemClickSupport.OnItemClickListener
                                               onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        if (mItemClickSupport != null) {
            mItemClickSupport.setOnItemClickListener(onItemClickListener);
        }
    }

    public void setOnItemLongClickListener(@NonNull ItemClickSupport.OnItemLongClickListener
                                                   onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
        if (mItemClickSupport != null) {
            mItemClickSupport.setOnItemLongClickListener(onItemLongClickListener);
        }
    }

    public void addLayoutChangeListener(@Orientation final int orientation,
                                        @NonNull final OnLayoutChangeListener listener) {

        addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                boolean isLayoutChange = false;

                switch (orientation) {
                    case TOP:
                        isLayoutChange = top > oldTop;
                        break;
                    case BOTTOM:
                        isLayoutChange = bottom < oldBottom;
                        break;
                    case LEFT:
                        isLayoutChange = left > oldLeft;
                        break;
                    case RIGHT:
                        isLayoutChange = right < oldRight;
                        break;
                }
                if (isLayoutChange) {
                    // 异步执行，防止attackView中数据不能被操作
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listener.onLayoutChange();
                        }
                    }, 100);
                }
            }
        });
    }

    /**
     * 设置滑动时，是否加载数据。
     *
     * @param isNeed true为加载数据，false为不加载。
     */
    public void notifyDataChangeOnScroll(boolean isNeed) {
        if (!isNeed) {
            return;
        }

        mOnEndlessScrollListener.setNeedLoadData(true);
    }

    public void setOnScrollStateChangedListener(OnScrollStateChangedListener listener) {
        mOnScrollStateChangedListener = listener;
        addEndlessScrollListener();
    }

    /**
     * 重写SpanSizeLookup，优化GridLayoutManager Header/Footer/LoadMore 显示
     * 如果用户已重写，则不再重写，用户重写应考虑Header/Footer/LoadMore 显示问题
     *
     * @param layoutManager the layout manager
     */
    private void optimizeGridLayout(@NonNull final RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof GridLayoutManager)) return;

        GridLayoutManager.SpanSizeLookup lookup = ((GridLayoutManager) layoutManager).getSpanSizeLookup();
        if (!(lookup instanceof GridLayoutManager.DefaultSpanSizeLookup)) return;

        ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mTitanAdapter == null) {
                    return 0;
                }

                int itemCount = mTitanAdapter.getItemCount();
                boolean flag = (mTitanAdapter.hasHeader() && 0 == position)
                        || (mTitanAdapter.hasFooter() && 0 != position
                        && itemCount - 1 == position)
                        || (mHasMore && (!mTitanAdapter.hasFooter()
                        && 0 != position && itemCount - 1 == position))
                        || (mHasMore && (mTitanAdapter.hasFooter()
                        && 0 != position && itemCount - 2 == position));

                return flag ? ((GridLayoutManager) layoutManager).getSpanCount() : 1;
            }
        });
    }

    /**
     * 处理水平loadmoreview
     */
    private void setupLoadMore() {

        boolean isHorizonal = false;

        if (getLayoutManager() instanceof LinearLayoutManager) {
            isHorizonal = ((LinearLayoutManager) getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL;
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            isHorizonal = ((StaggeredGridLayoutManager) getLayoutManager()).getOrientation() == StaggeredGridLayoutManager.HORIZONTAL;
        }

        mLoadMoreResourceId = R.layout.layout_default_more_view == mLoadMoreResourceId && isHorizonal ? R.layout.layout_default_horizontal_more_view : R.layout.layout_default_more_view;
    }
}

