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
public class PostImageViewHolder extends RecyclerView.ViewHolder{

    public ImageView authorAvatar;
    public TextView authorView;
    public TextView dateView;
    public ImageView starView;
    public TextView numStarsView;
    public ImageView commentView;
    public TextView numCommentView;
    public ImageView imageView;
    public TextView textView;

    public PostImageViewHolder(View itemView) {
        super(itemView);

        authorAvatar = (ImageView)itemView.findViewById(R.id.post_author_photo);
        dateView = (TextView)itemView.findViewById(R.id.post_date);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.heart);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        commentView = (ImageView)itemView.findViewById(R.id.show_comment_button);
        numCommentView = (TextView)itemView.findViewById(R.id.post_num_comment);
        imageView = (ImageView)itemView.findViewById(R.id.image_content);
        textView = (TextView) itemView.findViewById(R.id.image_text);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener, View.OnClickListener commentClickListener) {
        //authorView.setText(post.author);
        dateView.setText(post.date);
        numStarsView.setText(String.valueOf(post.starCount));
        numCommentView.setText("responses " + String.valueOf(post.commentCount));

        //bind the image view later
        textView.setText(post.description);

        starView.setOnClickListener(starClickListener);
        commentView.setOnClickListener(commentClickListener);
    }
}
