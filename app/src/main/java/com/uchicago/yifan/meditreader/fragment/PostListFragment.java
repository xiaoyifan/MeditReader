package com.uchicago.yifan.meditreader.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.uchicago.yifan.meditreader.Activities.PostDetailActivity;
import com.uchicago.yifan.meditreader.Model.Post;
import com.uchicago.yifan.meditreader.Model.User;
import com.uchicago.yifan.meditreader.R;
import com.uchicago.yifan.meditreader.ViewHolder.PostImageViewHolder;
import com.uchicago.yifan.meditreader.ViewHolder.PostLinkViewHolder;
import com.uchicago.yifan.meditreader.ViewHolder.PostQuoteViewHolder;
import com.uchicago.yifan.meditreader.ViewHolder.PostTextViewHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class PostListFragment extends Fragment {

    private static final String TAG = "PostListFragment";

    private static final int IMAGE = 0;
    private static final int LINK = 1;
    private static final int TEXT = 2;
    private static final int QUOTE = 3;

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Post, RecyclerView.ViewHolder> mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    public PostListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_post_list, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.post_list);
        mRecyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Query postsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Post, RecyclerView.ViewHolder>(Post.class, R.layout.post_item_image, RecyclerView.ViewHolder.class, postsQuery)
        {

            @Override
            public int getItemViewType(int position) {

                Post post = getItem(position);

                switch (post.post_type.toString()){
                    case "IMAGE":
                        return IMAGE;
                    case "LINK":
                        return LINK;
                    case "TEXT":
                        return TEXT;
                    case "QUOTE":
                        return QUOTE;
                }

                return super.getItemViewType(position);
            }

            @Override
            protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, final Post model, final int position) {

                switch (model.post_type.toString()){
                    case "IMAGE":
                        populateImageViewHolder((PostImageViewHolder)viewHolder, model, position);
                        break;
                    case "LINK":
                        populateLinkViewHolder((PostLinkViewHolder)viewHolder, model, position);
                        break;
                    case "TEXT":
                        populateTextViewHolder((PostTextViewHolder)viewHolder, model, position);
                        break;
                    case "QUOTE":
                        populateQuoteViewHolder((PostQuoteViewHolder)viewHolder, model, position);
                        break;
                }
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                switch (viewType){
                    case IMAGE:
                        View postType1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_image, parent, false);
                        return new PostImageViewHolder(postType1);
                    case LINK:
                        View postType2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_link, parent, false);
                        return new PostLinkViewHolder(postType2);
                    case TEXT:
                        View postType3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_text, parent, false);
                        return new PostTextViewHolder(postType3);
                    case QUOTE:
                        View postType4 = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_quote, parent, false);
                        return new PostQuoteViewHolder(postType4);
                }

                return super.onCreateViewHolder(parent, viewType);
            }


            public void populateImageViewHolder(final PostImageViewHolder viewHolder, final Post model, final int position){

                final DatabaseReference postRef = getRef(position);
                final String postKey = postRef.getKey();

                if (model.stars.containsKey(getUid())){
                    viewHolder.starView.setImageResource(R.drawable.like_filled_48);
                }
                else {
                    viewHolder.starView.setImageResource(R.drawable.like_48);
                }

                final String userId = model.uid;
                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);

                                if (user == null){
                                    Log.e(TAG, "User " + userId + " is unexpectedly null");
                                    Toast.makeText(getContext(),
                                            "Error: could not fetch user.",
                                            Toast.LENGTH_SHORT).show();
                                }else{

                                    viewHolder.authorView.setText(user.username);
                                    //viewHolder.authorAvatar;
                                    Glide.with(getActivity())
                                            .load(user.avatarUri)
                                            .fitCenter()
                                            .placeholder(R.drawable.ic_action_account_circle_40)
                                            .into(viewHolder.authorAvatar);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        }
                );

                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View startView) {

                        DatabaseReference globalPostRef = mDatabase.child("posts").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("user-posts").child(model.uid).child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View commentView) {
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });

                Glide.with(getActivity())
                        .load(model.url)
                        .fitCenter()
                        .into(viewHolder.imageView);
            }

            public void populateLinkViewHolder(final PostLinkViewHolder viewHolder, final Post model, final int position){

                final DatabaseReference postRef = getRef(position);
                final String postKey = postRef.getKey();

                viewHolder.linkTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String link = model.url;
                        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(intent);
                    }
                });

                if (model.stars.containsKey(getUid())){
                    viewHolder.starView.setImageResource(R.drawable.like_filled_48);
                }
                else {
                    viewHolder.starView.setImageResource(R.drawable.like_48);
                }

                final String userId = model.uid;
                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);

                                if (user == null){
                                    Log.e(TAG, "User " + userId + " is unexpectedly null");
                                    Toast.makeText(getContext(),
                                            "Error: could not fetch user.",
                                            Toast.LENGTH_SHORT).show();
                                }else{

                                    viewHolder.authorView.setText(user.username);
                                    //viewHolder.authorAvatar;
                                    Glide.with(getActivity())
                                            .load(user.avatarUri)
                                            .fitCenter()
                                            .placeholder(R.drawable.ic_action_account_circle_40)
                                            .into(viewHolder.authorAvatar);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        }
                );

                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View startView) {

                        DatabaseReference globalPostRef = mDatabase.child("posts").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("user-posts").child(model.uid).child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View commentView) {
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });
            }

            public void populateTextViewHolder(final PostTextViewHolder viewHolder, final Post model, final int position){

                final DatabaseReference postRef = getRef(position);
                final String postKey = postRef.getKey();

                if (model.stars.containsKey(getUid())){
                    viewHolder.starView.setImageResource(R.drawable.like_filled_48);
                }
                else {
                    viewHolder.starView.setImageResource(R.drawable.like_48);
                }

                final String userId = model.uid;
                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);

                                if (user == null){
                                    Log.e(TAG, "User " + userId + " is unexpectedly null");
                                    Toast.makeText(getContext(),
                                            "Error: could not fetch user.",
                                            Toast.LENGTH_SHORT).show();
                                }else{

                                    viewHolder.authorView.setText(user.username);
                                    //viewHolder.authorAvatar;
                                    Glide.with(getActivity())
                                            .load(user.avatarUri)
                                            .fitCenter()
                                            .placeholder(R.drawable.ic_action_account_circle_40)
                                            .into(viewHolder.authorAvatar);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        }
                );

                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View startView) {

                        DatabaseReference globalPostRef = mDatabase.child("posts").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("user-posts").child(model.uid).child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View commentView) {
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });
            }

            public void populateQuoteViewHolder(final PostQuoteViewHolder viewHolder, final Post model, final int position){

                final DatabaseReference postRef = getRef(position);
                final String postKey = postRef.getKey();

                viewHolder.numCommentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });

                if (model.stars.containsKey(getUid())){
                    viewHolder.starView.setImageResource(R.drawable.like_filled_48);
                }
                else {
                    viewHolder.starView.setImageResource(R.drawable.like_48);
                }

                final String userId = model.uid;
                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);

                                if (user == null){
                                    Log.e(TAG, "User " + userId + " is unexpectedly null");
                                    Toast.makeText(getContext(),
                                            "Error: could not fetch user.",
                                            Toast.LENGTH_SHORT).show();
                                }else{

                                    viewHolder.authorView.setText(user.username);
                                    //viewHolder.authorAvatar;
                                    Glide.with(getActivity())
                                            .load(user.avatarUri)
                                            .fitCenter()
                                            .placeholder(R.drawable.ic_action_account_circle_40)
                                            .into(viewHolder.authorAvatar);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        }
                );

                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View startView) {

                        DatabaseReference globalPostRef = mDatabase.child("posts").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("user-posts").child(model.uid).child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View commentView) {
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });
            }


        };


        mRecyclerView.setAdapter(mAdapter);
    }

    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }


    public void updateBookmarkForUser(final DatabaseReference userBookmarkRef, final Post model, final String key){


        userBookmarkRef.child(key).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Post existedPost = dataSnapshot.getValue(Post.class);

                        //if the post is not bookmarked yet, add it to the user bookmark
                        if (existedPost == null){

                            Map<String, Object> postValues = model.toMap();
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put( "/" + key, postValues);

                            userBookmarkRef.updateChildren(childUpdates);
                        }else{
                            dataSnapshot.getRef().setValue(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                }
        );

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);
}
