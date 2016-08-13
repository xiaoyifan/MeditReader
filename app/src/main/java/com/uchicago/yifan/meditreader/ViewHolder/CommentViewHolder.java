package com.uchicago.yifan.meditreader.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uchicago.yifan.meditreader.R;

/**
 * Created by Yifan on 8/13/16.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    public TextView authorNameView;
    public TextView bodyView;
    public ImageView authorAvatarView;

    public CommentViewHolder(View itemView) {
        super(itemView);

        authorNameView = (TextView)itemView.findViewById(R.id.comment_username);
        authorAvatarView = (ImageView)itemView.findViewById(R.id.comment_avatar_id);
        bodyView = (TextView)itemView.findViewById(R.id.comment_content);
    }
}
