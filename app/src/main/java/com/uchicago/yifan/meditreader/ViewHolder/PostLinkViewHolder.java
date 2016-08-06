package com.uchicago.yifan.meditreader.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uchicago.yifan.meditreader.Model.Post;
import com.uchicago.yifan.meditreader.R;

/**
 * Created by Yifan on 8/3/16.
 */
public class PostLinkViewHolder extends RecyclerView.ViewHolder{

    public TextView authorView;
    public TextView dateView;
    public ImageView starView;
    public TextView numStarsView;
    public ImageView bookmarkView;
    public TextView numCommentView;
    public TextView linkTitleView;
    public TextView linkView;
    public TextView linkDescriptionView;

    public PostLinkViewHolder(View itemView) {
        super(itemView);

        dateView = (TextView)itemView.findViewById(R.id.post_date);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.heart);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bookmarkView = (ImageView)itemView.findViewById(R.id.bookmark);
        numCommentView = (TextView)itemView.findViewById(R.id.post_num_comment);
        linkTitleView = (TextView)itemView.findViewById(R.id.link_title_view);
        linkView = (TextView)itemView.findViewById(R.id.link_view);
        linkDescriptionView = (TextView) itemView.findViewById(R.id.link_description_view);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener, View.OnClickListener bookmarkClickListener) {
        authorView.setText(post.author);
        dateView.setText(post.date);
        numStarsView.setText(String.valueOf(post.starCount));
        numCommentView.setText("responses " + String.valueOf(post.commentCount));
        linkTitleView.setText(post.title);
        linkView.setText(post.url);
        linkDescriptionView.setText(post.description);
        starView.setOnClickListener(starClickListener);
        bookmarkView.setOnClickListener(bookmarkClickListener);
    }
}
