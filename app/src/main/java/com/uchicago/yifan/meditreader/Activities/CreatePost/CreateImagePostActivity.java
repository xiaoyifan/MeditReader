package com.uchicago.yifan.meditreader.Activities.CreatePost;

import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.uchicago.yifan.meditreader.Model.Post;
import com.uchicago.yifan.meditreader.Model.PostType;
import com.uchicago.yifan.meditreader.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateImagePostActivity extends CreatePostActivity {

    private StorageReference mStorageRef;
    private String imageUrl;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_image_post;
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        ArrayList<Uri> image_urls = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
        ImageView thumbnail = (ImageView)findViewById(R.id.media_image);
        imageUrl = image_urls.get(0).toString();
        Glide.with(this)
                .load(image_urls.get(0).toString())
                .fitCenter()
                .into(thumbnail);
    }

    @Override
    void writeNewPost(String userId, String username) {

        String key = mDatabase.child("posts").push().getKey();
        EditText ImageDescription = (EditText) findViewById(R.id.image_description);
        Post post = new Post(userId, PostType.IMAGE, null, imageUrl, username, ImageDescription.getText().toString());

        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
}
