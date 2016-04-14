package com.youzan.titan.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youzan.titan.TitanRecyclerView;
import com.youzan.titan.sample.adapter.HeaderBottomItemAdapter;
import com.youzan.titan.sample.R;

/**
 * description
 *
 * @author monster @Hangzhou Youzan Technology Co.Ltd
 * @date 16/3/14
 */
public class MultipleHeaderBottomFragment extends Fragment {
    public static final int TYPE_LINEAR_LAYOUT = 1;
    public static final int TYPE_GRID_LAYOUT = 2;
    public static final int TYPE_STAGGERED_GRID_LAYOUT = 3;
    private TitanRecyclerView mAttackView;

    private int type = TYPE_LINEAR_LAYOUT;
    private GridLayoutManager gridLayoutManager;
    private HeaderBottomItemAdapter mAdapter;

    public static MultipleHeaderBottomFragment newInstance(int type) {
        MultipleHeaderBottomFragment fragment = new MultipleHeaderBottomFragment();
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
        mAttackView = (TitanRecyclerView) rootView.findViewById(R.id.attackview);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (type == TYPE_GRID_LAYOUT) {
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            mAttackView.setLayoutManager(gridLayoutManager);//这里用线性宫格显示 类似于grid view
        } else if (type == TYPE_STAGGERED_GRID_LAYOUT) {
            mAttackView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        } else {
            mAttackView.setLayoutManager(new LinearLayoutManager(getActivity()));//这里用线性显示 类似于list view
        }
        mAdapter = new HeaderBottomItemAdapter(getActivity());
        mAdapter.setHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.item_image, null));
        mAdapter.setFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.item_image, null));
        mAttackView.setAdapter(mAdapter);
    }

}
