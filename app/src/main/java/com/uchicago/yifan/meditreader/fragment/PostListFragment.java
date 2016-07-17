package com.uchicago.yifan.meditreader.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uchicago.yifan.meditreader.Model.Post;
import com.uchicago.yifan.meditreader.R;
import com.uchicago.yifan.meditreader.ViewHolder.PostItemViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostListFragment extends Fragment {

    private static final String TAG = "PostListFragment";

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Post, PostItemViewHolder> mAdapter;
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

}
