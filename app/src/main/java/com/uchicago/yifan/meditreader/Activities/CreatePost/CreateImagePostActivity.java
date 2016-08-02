package com.uchicago.yifan.meditreader.Activities.CreatePost;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.uchicago.yifan.meditreader.Activities.BaseActivity;
import com.uchicago.yifan.meditreader.R;

import java.util.ArrayList;

public class CreateImagePostActivity extends BaseActivity {

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_image_post);

        Intent intent = getIntent();
        ArrayList<Uri> image_urls = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
        ImageView thumbnail = (ImageView)findViewById(R.id.media_image);

        Glide.with(this)
                .load(image_urls.get(0).toString())
                .fitCenter()
                .into(thumbnail);
    }
}
