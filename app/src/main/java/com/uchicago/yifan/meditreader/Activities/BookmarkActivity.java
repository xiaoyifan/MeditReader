package com.uchicago.yifan.meditreader.Activities;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.uchicago.yifan.meditreader.R;
import com.uchicago.yifan.meditreader.fragment.BookmarkFragment;

public class BookmarkActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });

        if (savedInstanceState == null){

            BookmarkFragment fragment = new BookmarkFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.bookmark_fragement_container, fragment)
                    .commit();
        }

    }
}
