package com.uchicago.yifan.meditreader.Activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uchicago.yifan.meditreader.CommentAdapter;
import com.uchicago.yifan.meditreader.Model.Comment;
import com.uchicago.yifan.meditreader.Model.User;
import com.uchicago.yifan.meditreader.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class PostDetailActivity extends BaseActivity{

    private static final String TAG = "PostDetailActivity";

    private DatabaseReference mPostReference;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private String mPostKey;

    private CommentAdapter mAdapter;

    public static final String EXTRA_POST_KEY = "post_key";

    @BindView(R.id.comment_post_button) Button commentPostButton;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.comment_list) RecyclerView recyclerView;
    @BindView(R.id.comment_input_view) EditText commentInputView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);

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

        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child("post-comments").child(mPostKey);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAdapter = new CommentAdapter(this, mCommentsReference);
        recyclerView.setAdapter(mAdapter);
    }


    @OnTextChanged(R.id.comment_input_view)
    public void textChanged (CharSequence text) {
        if (text.length() != 0)
        {
            commentPostButton.setEnabled(true);
        }
        else {
            commentPostButton.setEnabled(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAdapter.cleanupListener();
    }

    @OnClick(R.id.comment_post_button)
    void postComment() {
        final String uid = getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);

                        // Create new comment object
                        String commentText = commentInputView.getText().toString();
                        Comment comment = new Comment(uid, commentText);

                        // Push the comment, it will appear in the list
                        mCommentsReference.push().setValue(comment);



                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/posts/" + mPostKey + "/commentCount", mAdapter.mCommentIds.size()+1);
                        childUpdates.put("/user-posts/" + uid + "/" + mPostKey + "/commentCount", mAdapter.mCommentIds.size()+1);

                        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
                        // Clear the field
                        commentInputView.setText(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
