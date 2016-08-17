package com.uchicago.yifan.meditreader.Activities.CreatePost;

import android.widget.EditText;

import com.uchicago.yifan.meditreader.Model.Post;
import com.uchicago.yifan.meditreader.R;

import java.util.HashMap;
import java.util.Map;

public class CreateQuotePostActivity extends CreatePostActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_quote_post;
    }

    @Override
    void writeNewPost(String userId) {

        String key = mDatabase.child("posts").push().getKey();
        EditText QuoteText = (EditText) findViewById(R.id.quote_text);
        EditText QuoteSource = (EditText) findViewById(R.id.quote_source);
        Post post = new Post(userId, "QUOTE", QuoteText.getText().toString(), QuoteSource.getText().toString());

        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);

        finish();
    }
}
