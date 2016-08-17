package com.uchicago.yifan.meditreader.Activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

public class PostDetailActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "PostDetailActivity";

    private DatabaseReference mPostReference;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private String mPostKey;

    private CommentAdapter mAdapter;
    private RecyclerView recyclerView;


    private EditText commentInputView;
    private Button commentPostButton;

    public static final String EXTRA_POST_KEY = "post_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

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

        commentPostButton = (Button)findViewById(R.id.comment_post_button);
        commentPostButton.setOnClickListener(this);
        commentInputView = (EditText)findViewById(R.id.comment_input_view);

        commentInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0)
                {
                    commentPostButton.setEnabled(true);
                }
                else {
                    commentPostButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.comment_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAdapter = new CommentAdapter(this, mCommentsReference);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAdapter.cleanupListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_post_button:
                postComment();
                break;
        }
    }

    private void postComment() {
        final String uid = getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);
                        String authorName = user.username;

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
