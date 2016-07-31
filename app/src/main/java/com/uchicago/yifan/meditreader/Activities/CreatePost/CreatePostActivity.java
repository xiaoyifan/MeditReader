package com.uchicago.yifan.meditreader.Activities.CreatePost;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uchicago.yifan.meditreader.Activities.BaseActivity;
import com.uchicago.yifan.meditreader.R;

public abstract class CreatePostActivity extends BaseActivity {

    private static final String TAG = "NewPostActivity";

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    protected abstract int getLayoutResourceId();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_post:
                publishArticle();
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void publishArticle(){

    }
}
