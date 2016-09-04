package com.uchicago.yifan.meditreader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uchicago.yifan.meditreader.Data.BookmarkContract;
import com.uchicago.yifan.meditreader.Model.User;
import com.uchicago.yifan.meditreader.fragment.BookmarkFragment;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Yifan on 8/30/16.
 */
public class BookmarkAdapter extends CursorAdapter {

    private DatabaseReference mDatabase;

    public BookmarkAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        String post_type = cursor.getString(BookmarkFragment.COL_POST_TYPE);

        switch (post_type){
            case "IMAGE":
                return LayoutInflater.from(context).inflate(R.layout.bookmark_item_image, viewGroup, false);
            case "TEXT":
                return LayoutInflater.from(context).inflate(R.layout.bookmark_item_text, viewGroup, false);
            case "QUOTE":
                return LayoutInflater.from(context).inflate(R.layout.bookmark_item_quote, viewGroup, false);
            case "LINK":
                return LayoutInflater.from(context).inflate(R.layout.bookmark_item_link, viewGroup, false);
        }

        return null;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        String post_type = cursor.getString(BookmarkFragment.COL_POST_TYPE);

        TextView dateView = (TextView) view.findViewById(R.id.post_date);
        dateView.setText(cursor.getString(BookmarkFragment.COL_DATE));

        final TextView postAuthorView = (TextView)view.findViewById(R.id.post_author);
        final ImageView authorAvatarView = (ImageView)view.findViewById(R.id.post_author_photo);

        final String userId = cursor.getString(BookmarkFragment.COL_USER_ID);
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null){
                            postAuthorView.setText("Unidentified user");
                        }else{
                            postAuthorView.setText(user.username);
                            Glide.with(context)
                                    .load(user.avatarUri)
                                    .fitCenter()
                                    .placeholder(R.drawable.ic_action_account_circle_40)
                                    .into(authorAvatarView);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("BOOKMARK: ", "getUser:onCancelled", databaseError.toException());
                    }
                }
        );

        final String postId = cursor.getString(BookmarkFragment.COL_POST_ID);

        ImageView bookmarkView = (ImageView) view.findViewById(R.id.bookmark_button);
        bookmarkView.setImageResource(R.drawable.trash_48);
        bookmarkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Remove this bookmark?")
                        .setConfirmText("Sure")
                        .setCancelText("Cancel")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                context.getContentResolver().delete(BookmarkContract.BookmarkEntry.buildBookmarkUri(postId), null, null);
                                sDialog.cancel();
                            }
                        })
                        .show();
            }
        });
        switch (post_type){
            case "IMAGE":
            {
               TextView imageTextView = (TextView)view.findViewById(R.id.image_text);
               imageTextView.setText(cursor.getString(BookmarkFragment.COL_DESCRIPTION));

               ImageView imageView = (ImageView)view.findViewById(R.id.image_content);
                String url = cursor.getString(BookmarkFragment.COL_URL);
                Glide.with(context)
                        .load(url)
                        .fitCenter()
                        .into(imageView);
                break;
            }
            case "TEXT":
            {
                TextView textView = (TextView)view.findViewById(R.id.text_title);
                textView.setText(cursor.getString(BookmarkFragment.COL_TITLE));
                TextView contentView = (TextView)view.findViewById(R.id.text_body);
                contentView.setText(cursor.getString(BookmarkFragment.COL_DESCRIPTION));
                break;
            }
            case "QUOTE":
            {
                TextView quoteTextView = (TextView)view.findViewById(R.id.quote_text_view);

                quoteTextView.setText(cursor.getString(BookmarkFragment.COL_TITLE));
                TextView quoteSourceView = (TextView)view.findViewById(R.id.quote_source_view);

                quoteSourceView.setText(cursor.getString(BookmarkFragment.COL_DESCRIPTION));
                break;
            }
            case "LINK":
            {
                TextView linkTitleView = (TextView)view.findViewById(R.id.link_title_view);
                linkTitleView.setText(cursor.getString(BookmarkFragment.COL_TITLE) + " >");
                TextView linkDescriptionView = (TextView)view.findViewById(R.id.link_description_view);
                linkDescriptionView.setText(cursor.getString(BookmarkFragment.COL_DESCRIPTION));

                final String link = cursor.getString(BookmarkFragment.COL_URL);
                linkTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        context.startActivity(intent);
                    }
                });
                break;
            }
        }
    }
}
