package com.youzan.titan.sample.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.youzan.titan.sample.R;
import com.youzan.titan.sample.fragment.ItemsFragment;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ItemsFragment.newInstance())
                    .commit();
        }
    }
}
