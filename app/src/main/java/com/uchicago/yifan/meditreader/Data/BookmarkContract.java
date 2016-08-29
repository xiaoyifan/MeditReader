package com.uchicago.yifan.meditreader.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Yifan on 8/28/16.
 */
public class BookmarkContract {

    public static final String CONTENT_AUTHORITY = "com.uchicago.yifan.meditreader";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOOKMARK = "bookmark";

    public static final class PostEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKMARK).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKMARK;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKMARK;

        public static final String TABLE_NAME = "bookmark";

        public static final String COLUMN_POST_ID = "post_id";

        public static final String COLUMN_USER_ID = "uid";
        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_URL = "url";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_POST_TYPE = "post_type";


    }

}
