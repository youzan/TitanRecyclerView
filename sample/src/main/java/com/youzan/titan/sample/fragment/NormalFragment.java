package com.youzan.titan.sample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youzan.titan.TitanAdapter;
import com.youzan.titan.TitanRecyclerView;
import com.youzan.titan.sample.adapter.NormalRecyclerViewAdapter;
import com.youzan.titan.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author monster @Hangzhou Youzan Technology Co.Ltd
 * @date 16/3/14
 */
public class NormalFragment extends Fragment {
    public static final int TYPE_LINEAR_LAYOUT = 1;
    public static final int TYPE_GRID_LAYOUT = 2;
    public static final int TYPE_STAGGERED_GRID_LAYOUT = 3;
    public static final int TYPE_LINEAR_LOAD_MORE_LAYOUT = 4;
    private TitanRecyclerView mAttackview;
    private TitanAdapter<String> mTitanAdapter;
    private int loadNums;

    private int type = TYPE_LINEAR_LAYOUT;

    public static NormalFragment newInstance(int type) {
        NormalFragment fragment = new NormalFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type", TYPE_LINEAR_LAYOUT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_normal, container, false);
        mAttackview = (TitanRecyclerView) rootView.findViewById(R.id.attackview);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (type == TYPE_GRID_LAYOUT) {
            mAttackview.setLayoutManager(new GridLayoutManager(getActivity(), 2));//这里用线性宫格显示 类似于grid view
        } else if (type == TYPE_STAGGERED_GRID_LAYOUT) {
            mAttackview.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        } else {
            mAttackview.setLayoutManager(new LinearLayoutManager(getActivity()));//这里用线性显示 类似于list view
        }
        mAttackview.setAdapter(mTitanAdapter = new NormalRecyclerViewAdapter(getActivity()));
        if (TYPE_LINEAR_LOAD_MORE_LAYOUT == type) {
            mAttackview.setOnLoadMoreListener(new TitanRecyclerView.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    loadItem();
                }
            });
        }
        mAttackview.setItemAnimator(new DefaultItemAnimator());
    }

    private void loadItem() {
        Log.e("AttackView", "loadNums:" + loadNums);
        if (loadNums < 5) {
            loadNums++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int index = mTitanAdapter.getData().size();
                    int end = index + 20;
                    List<String> datas = new ArrayList<String>();
                    for (int i = index; i < end; i++) {
                        datas.add("item" + i);
                    }
                    mTitanAdapter.addDataEnd(datas);
                }
            }, 5000);
        } else {
            mTitanAdapter.setHasMore(false);
        }
    }
}
