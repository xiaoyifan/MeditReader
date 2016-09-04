package com.uchicago.yifan.meditreader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uchicago.yifan.meditreader.fragment.BookmarkFragment;

/**
 * Created by Yifan on 8/30/16.
 */
public class BookmarkAdapter extends CursorAdapter {

    public BookmarkAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
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
                TextView quoteTextView = (TextView)view.findViewById(R.id.quote_text);
                quoteTextView.setText(cursor.getString(BookmarkFragment.COL_TITLE));
                TextView quoteSourceView = (TextView)view.findViewById(R.id.quote_source);
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
