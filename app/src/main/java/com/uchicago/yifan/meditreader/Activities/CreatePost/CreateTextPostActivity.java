package com.uchicago.yifan.meditreader.Activities.CreatePost;

import android.widget.EditText;

import com.uchicago.yifan.meditreader.Model.Post;
import com.uchicago.yifan.meditreader.R;

import java.util.HashMap;
import java.util.Map;

public class CreateTextPostActivity extends CreatePostActivity {


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_text_post;
    }

    @Override
    void writeNewPost(String userId) {

        String key = mDatabase.child("posts").push().getKey();
        EditText TextTitle = (EditText) findViewById(R.id.text_title);
        EditText TextContent = (EditText) findViewById(R.id.text_content);
        Post post = new Post(userId, "TEXT", TextTitle.getText().toString(), TextContent.getText().toString());

        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);

        finish();
    }
}
