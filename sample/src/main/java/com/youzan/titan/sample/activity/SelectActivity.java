package com.youzan.titan.sample.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.youzan.titan.sample.R;
import com.youzan.titan.sample.fragment.MultipleSelectFragment;
import com.youzan.titan.sample.fragment.SingleSelectFragment;

/**
 * description
 *
 * @author monster @Hangzhou Youzan Technology Co.Ltd
 * @date 16/3/13
 */
public class SelectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int index = getIntent().getIntExtra("position", 0);
        Log.d("NormalTextViewHolder", "onClick--> index = " + index);
        String title = getIntent().getStringExtra("title");
        setTitle(title);
        updateFragment(index);
    }

    private void updateFragment(int index) {
        if (index == 10) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SingleSelectFragment.newInstance())
                    .commit();
        } else if (index == 11) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MultipleSelectFragment.newInstance())
                    .commit();
        }
    }
}
