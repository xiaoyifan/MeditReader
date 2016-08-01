package com.uchicago.yifan.meditreader.Activities.CreatePost;

import android.os.Bundle;

import com.google.firebase.storage.StorageReference;
import com.uchicago.yifan.meditreader.Activities.BaseActivity;
import com.uchicago.yifan.meditreader.R;

public class CreateImagePostActivity extends BaseActivity {

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_image_post);
    }
}
