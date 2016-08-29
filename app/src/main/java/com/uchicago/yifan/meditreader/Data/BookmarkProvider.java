package com.uchicago.yifan.meditreader.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Yifan on 8/28/16.
 */
public class BookmarkProvider extends ContentProvider {

    private BookmarkDbHelper mOpenHelper;

    static final int BOOKMARK = 100;
    static final int BOOKMARK_WITH_ID = 101;

    private static final SQLiteQueryBuilder sBookmarkByIDQueryBuilder;

    static {
        sBookmarkByIDQueryBuilder = new SQLiteQueryBuilder();
        sBookmarkByIDQueryBuilder.setTables(BookmarkContract.BookmarkEntry.TABLE_NAME);
    }

    private static final String sBookmarkIDSelection = BookmarkContract.BookmarkEntry.TABLE_NAME +
                                                        "." + BookmarkContract.BookmarkEntry.COLUMN_POST_ID + " = ?";

    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BookmarkContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, BookmarkContract.PATH_BOOKMARK, BOOKMARK);
        matcher.addURI(authority, BookmarkContract.PATH_BOOKMARK + "/#", BOOKMARK_WITH_ID);

        return matcher;
    }

    private Cursor getBookmarkWithID(Uri uri, String[] projection, String sortOrder){
        String post_id = BookmarkContract.BookmarkEntry.getBookmarkIDFromUri(uri);

        return sBookmarkByIDQueryBuilder.query(mOpenHelper.getWritableDatabase(),
                projection,
                sBookmarkIDSelection,
                new String[]{post_id},
                null,
                null,
                sortOrder);

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BookmarkDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
