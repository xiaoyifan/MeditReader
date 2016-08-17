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
public class PostQuoteViewHolder extends RecyclerView.ViewHolder{

    public ImageView authorAvatar;
    public TextView authorView;
    public TextView dateView;
    public ImageView starView;
    public TextView numStarsView;
    public ImageView bookmarkView;
    public TextView numCommentView;
    public TextView titleView;
    public TextView bodyView;

    public PostQuoteViewHolder(View itemView) {
        super(itemView);

        authorAvatar = (ImageView)itemView.findViewById(R.id.post_author_photo);
        dateView = (TextView)itemView.findViewById(R.id.post_date);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.heart);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bookmarkView = (ImageView)itemView.findViewById(R.id.bookmark);
        numCommentView = (TextView)itemView.findViewById(R.id.post_num_comment);
        titleView = (TextView)itemView.findViewById(R.id.quote_text_view);
        bodyView = (TextView) itemView.findViewById(R.id.quote_source_view);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener, View.OnClickListener bookmarkClickListener) {
        //authorView.setText(post.author);
        dateView.setText(post.date);
        numStarsView.setText(String.valueOf(post.starCount));
        numCommentView.setText("responses " + String.valueOf(post.commentCount));
        titleView.setText("\"" + post.title + "\"");
        bodyView.setText(" -  " + post.description);
        starView.setOnClickListener(starClickListener);
        bookmarkView.setOnClickListener(bookmarkClickListener);
    }
}
