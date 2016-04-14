package com.youzan.titan.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youzan.titan.divider.HorizontalDivider;
import com.youzan.titan.TitanRecyclerView;
import com.youzan.titan.internal.ItemClickSupport;
import com.youzan.titan.sample.adapter.DefaultSingleAdapter;
import com.youzan.titan.sample.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * description
 *
 * @author monster @Hangzhou Youzan Technology Co.Ltd
 * @date 16/3/14
 */
public class SingleSelectFragment extends Fragment {
    TitanRecyclerView mAttackView;

    public SingleSelectFragment() {

    }

    public static SingleSelectFragment newInstance() {
        return new SingleSelectFragment();
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
        mAttackView.setLayoutManager(new LinearLayoutManager(getActivity()));//这里用线性显示 类似于list view
        final DefaultSingleAdapter adapter = new DefaultSingleAdapter();
        ArrayList<String> items = new ArrayList<>();
        Collections.addAll(items, getActivity().getResources().getStringArray(R.array.titles));
        adapter.setData(items);
        mAttackView.setAdapter(adapter);
        mAttackView.setItemAnimator(new DefaultItemAnimator());
        mAttackView.addItemDecoration(new HorizontalDivider.Builder(getContext()).build());
        mAttackView.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
                if (adapter.isEnableSelect) {
                    adapter.setCurrentSelect(position);
                }
            }
        });
    }
}
