package com.youzan.titan.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.youzan.titan.divider.HorizontalDivider;
import com.youzan.titan.TitanRecyclerView;
import com.youzan.titan.internal.ItemClickSupport;
import com.youzan.titan.sample.adapter.DefaultMultipleAdapter;
import com.youzan.titan.sample.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * description
 *
 * @author monster @Hangzhou Youzan Technology Co.Ltd
 * @date 16/3/14
 */
public class MultipleSelectFragment extends Fragment {
    TitanRecyclerView mAttackView;
    private DefaultMultipleAdapter mDefaultMultipleAdapter;

    public MultipleSelectFragment() {

    }

    public static MultipleSelectFragment newInstance() {
        return new MultipleSelectFragment();
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
        mDefaultMultipleAdapter = new DefaultMultipleAdapter();
        ArrayList<String> items = new ArrayList<>();
        Collections.addAll(items, getActivity().getResources().getStringArray(R.array.titles));
        mDefaultMultipleAdapter.setData(items);
        mAttackView.setAdapter(mDefaultMultipleAdapter);
        mAttackView.setItemAnimator(new DefaultItemAnimator());
        mAttackView.addItemDecoration(new HorizontalDivider.Builder(getContext()).build());
        mAttackView.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
                selected(position);
            }
        });
        mAttackView.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerView recyclerView, View view, int position, long id) {
                if (mDefaultMultipleAdapter.isActionModeShow()) {
                    selected(position);
                } else {
                    mDefaultMultipleAdapter.setActionModeShow(true);
                    AppCompatActivity activity = (AppCompatActivity) getActivity();
                    activity.startSupportActionMode(mDeleteMode);
                }
                return true;
            }
        });
    }

    private void selected(int position) {
        if (mDefaultMultipleAdapter.isActionModeShow()) {
            if (mDefaultMultipleAdapter.isSelected(mDefaultMultipleAdapter.getItem(position))) {//已选中
                mDefaultMultipleAdapter.removeSelectPosition(mDefaultMultipleAdapter.getItem(position));
            } else {//未选中
                mDefaultMultipleAdapter.addSelectPosition(mDefaultMultipleAdapter.getItem(position));
            }
            mDefaultMultipleAdapter.notifyItemChanged(position);
        }
    }

    private ActionMode.Callback mDeleteMode = new ActionMode.Callback() {
        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mDefaultMultipleAdapter.setActionModeShow(false);
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getActivity().getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_delete:
                    onDeleteItems();
                    actionMode.finish();
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    private void onDeleteItems() {
        for (String selectItem : mDefaultMultipleAdapter.getMultiSelectItems()) {
            mDefaultMultipleAdapter.remove(selectItem);
        }
        mDefaultMultipleAdapter.getMultiSelectItems().clear();
    }
}