package com.uchicago.yifan.meditreader.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by Yifan on 7/16/16.
 */
public class TrendingPostsFragment extends PostListFragment {
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("posts").orderByChild("starCount").limitToFirst(50);
    }
}
