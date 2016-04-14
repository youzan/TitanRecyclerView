package com.youzan.titan.sample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youzan.titan.TitanRecyclerView;
import com.youzan.titan.internal.ItemClickSupport;
import com.youzan.titan.sample.DemoItem;
import com.youzan.titan.sample.activity.DetailActivity;
import com.youzan.titan.sample.adapter.ItemsAdapter;
import com.youzan.titan.sample.R;
import com.youzan.titan.sample.activity.SelectActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author monster @Hangzhou Youzan Technology Co.Ltd
 * @date 16/3/13
 */
public class ItemsFragment extends Fragment {
    private TitanRecyclerView mTitanRecyclerView;
    private ItemsAdapter mAdapter;
    private List<DemoItem> mData;

    public static ItemsFragment newInstance() {
        return new ItemsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadItems();
    }

    private void loadItems() {
        String[] items = getActivity().getResources().getStringArray(R.array.items);
        mData = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            mData.add(new DemoItem(i, items[i]));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_normal, container, false);
        mTitanRecyclerView = (TitanRecyclerView) rootView.findViewById(R.id.titan_recycler_view);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitanRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ItemsAdapter();
        mAdapter.setData(mData);
        mTitanRecyclerView.setAdapter(mAdapter);
        mTitanRecyclerView.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
                Log.d("NormalTextViewHolder", "onClick--> position = " + position);
                DemoItem item = mAdapter.getItem(position);
                if (null == item) {
                    return;
                }

                if (position < 9) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("title", item.title);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), SelectActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("title", item.title);
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
