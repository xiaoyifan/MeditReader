package com.uchicago.yifan.meditreader.Activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.uchicago.yifan.meditreader.R;
import com.uchicago.yifan.meditreader.fragment.BookmarkFragment;

public class BookmarkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        if (savedInstanceState == null){

            BookmarkFragment fragment = new BookmarkFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.bookmark_fragement_container, fragment)
                    .commit();
        }

    }
}
