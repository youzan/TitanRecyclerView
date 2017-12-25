package com.youzan.titan.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.youzan.titan.QuickAdapter;
import com.youzan.titan.TitanRecyclerView;
import com.youzan.titan.holder.AutoViewHolder;
import com.youzan.titan.internal.ItemClickSupport;
import com.youzan.titan.sample.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author monster @Hangzhou Youzan Technology Co.Ltd
 * @date 2017/6/7
 */

public class TestActivity extends Activity implements ItemClickSupport.OnItemClickListener {

    QuickAdapter<String> adapter;
    TitanRecyclerView titan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        Button btnAdd = (Button) findViewById(R.id.add_item_listener);
        Button btnClear = (Button) findViewById(R.id.clear_item_listener);
        Button badNet = (Button) findViewById(R.id.show_bad_net);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.showEmptyView();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.showDefaultView();
            }
        });

        badNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.showBadNetView();
            }
        });

        titan = (TitanRecyclerView) findViewById(R.id.titan);

        String[] strs = getResources().getStringArray(R.array.items);

        titan.setAdapter(adapter = new QuickAdapter<String>(R.layout.item_text) {
            @Override
            public void bindView(AutoViewHolder holder, int position, String model) {
                holder.getTextView(R.id.text_view).setText(model);
            }
        });

        List<String> data = new ArrayList<>();
        Collections.addAll(data, strs);

        adapter.setBadNetView(LayoutInflater.from(this).inflate(R.layout.bad_net_view, titan, false));
        adapter.setDefaultView(LayoutInflater.from(this).inflate(R.layout.default_view, titan, false));
        adapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, titan, false));

//        adapter.setData(data);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Toast.makeText(this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
    }
}
