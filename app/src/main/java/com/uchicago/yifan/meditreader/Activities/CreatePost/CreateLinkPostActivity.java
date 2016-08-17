package com.uchicago.yifan.meditreader.Activities.CreatePost;

import android.widget.EditText;

import com.uchicago.yifan.meditreader.Model.Post;
import com.uchicago.yifan.meditreader.R;

import java.util.HashMap;
import java.util.Map;

public class CreateLinkPostActivity extends CreatePostActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_link_post;
    }

    @Override
    void writeNewPost(String userId) {

        String key = mDatabase.child("posts").push().getKey();
        EditText linkTitle = (EditText) findViewById(R.id.link_title);
        EditText linkURL = (EditText) findViewById(R.id.link_url);
        EditText linkDescription = (EditText) findViewById(R.id.link_description);
        Post post = new Post(userId, "LINK", linkTitle.getText().toString(), linkURL.getText().toString(), linkDescription.getText().toString());

        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);

        finish();
    }
}
