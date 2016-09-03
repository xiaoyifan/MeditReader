package com.uchicago.yifan.meditreader.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uchicago.yifan.meditreader.Model.Post;
import com.uchicago.yifan.meditreader.R;

/**
 * Created by Yifan on 7/17/16.
 */
public class PostTextViewHolder extends RecyclerView.ViewHolder{

    public TextView authorView;
    public ImageView authorAvatar;
    public TextView dateView;
    public ImageView starView;
    public TextView numStarsView;
    public ImageView commentView;
    public TextView numCommentView;
    public TextView titleView;
    public TextView bodyView;
    public ImageView bookmarkView;

    public PostTextViewHolder(View itemView) {
        super(itemView);

        authorAvatar = (ImageView)itemView.findViewById(R.id.post_author_photo);
        dateView = (TextView)itemView.findViewById(R.id.post_date);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.heart);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        commentView = (ImageView)itemView.findViewById(R.id.show_comment_button);
        numCommentView = (TextView)itemView.findViewById(R.id.post_num_comment);
        titleView = (TextView)itemView.findViewById(R.id.text_title);
        bodyView = (TextView) itemView.findViewById(R.id.text_body);
        bookmarkView = (ImageView)itemView.findViewById(R.id.bookmark_button);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener, View.OnClickListener commentClickListener, View.OnClickListener bookmarkClickListener) {
        //authorView.setText(post.author);
        dateView.setText(post.date);
        numStarsView.setText(String.valueOf(post.starCount));
        numCommentView.setText("responses " + String.valueOf(post.commentCount));
        titleView.setText(post.title);
        bodyView.setText(post.description);

        starView.setOnClickListener(starClickListener);
        commentView.setOnClickListener(commentClickListener);
        bookmarkView.setOnClickListener(bookmarkClickListener);
    }
}
