package com.uchicago.yifan.meditreader.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.uchicago.yifan.meditreader.Model.Post;
import com.uchicago.yifan.meditreader.*;

/**
 * Created by Yifan on 7/17/16.
 */
public class PostItemViewHolder extends RecyclerView.ViewHolder{

    public TextView authorView;
    public TextView dateView;
    public ImageView starView;
    public TextView numStarsView;
    public ImageView bookmarkView;
    public TextView numCommentView;
    public WebView bodyView;

    public PostItemViewHolder(View itemView) {
        super(itemView);

        dateView = (TextView)itemView.findViewById(R.id.post_date);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.heart);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bookmarkView = (ImageView)itemView.findViewById(R.id.bookmark);
        numCommentView = (TextView)itemView.findViewById(R.id.post_num_comment);
        bodyView = (WebView) itemView.findViewById(R.id.post_body);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener, View.OnClickListener bookmarkClickListener) {
        authorView.setText(post.author);
        dateView.setText(post.date);
        numStarsView.setText(String.valueOf(post.starCount));
        numCommentView.setText("responses " + String.valueOf(post.commentCount));
        //bodyView.setText(post.body);

        starView.setOnClickListener(starClickListener);
        bookmarkView.setOnClickListener(bookmarkClickListener);
    }
}
