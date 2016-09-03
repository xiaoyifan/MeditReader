package com.uchicago.yifan.meditreader.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Yifan on 8/28/16.
 */
public class BookmarkProvider extends ContentProvider {

    private BookmarkDbHelper mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

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
        matcher.addURI(authority, BookmarkContract.PATH_BOOKMARK + "/*", BOOKMARK_WITH_ID);

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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectArgs, String sortOrder) {

        Cursor retCursor;

        switch (sUriMatcher.match(uri)){
            case BOOKMARK: {
                retCursor = mOpenHelper.getWritableDatabase().query(BookmarkContract.BookmarkEntry.TABLE_NAME, projection, selection, selectArgs, null, null, sortOrder);
                break;
            }
            case BOOKMARK_WITH_ID:{
                retCursor = getBookmarkWithID(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case BOOKMARK:
                return BookmarkContract.BookmarkEntry.CONTENT_TYPE;
            case BOOKMARK_WITH_ID:
                return BookmarkContract.BookmarkEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)){
            case BOOKMARK:{
                long _id = db.insert(BookmarkContract.BookmarkEntry.TABLE_NAME, null, contentValues);
                if (_id > 0){
                    returnUri = BookmarkContract.BookmarkEntry.CONTENT_URI;
                }
                else {
                    throw new android.database.SQLException("Failed to inset row into BOOKMARK TABLE: " + uri);
                }
                break;
            }
            default:
                throw new android.database.SQLException("Failed to insert row into " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;

        if (selection == null) selection = "1";
        switch (sUriMatcher.match(uri))
        {
            case BOOKMARK:{
                rowsDeleted = db.delete(
                        BookmarkContract.BookmarkEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case BOOKMARK_WITH_ID:{
                rowsDeleted = db.delete(BookmarkContract.BookmarkEntry.TABLE_NAME,
                        BookmarkContract.BookmarkEntry.COLUMN_POST_ID + " = ?", new String[]{BookmarkContract.BookmarkEntry.getBookmarkIDFromUri(uri)});

                Log.d("DELETE: ", "bookmark " + BookmarkContract.BookmarkEntry.getBookmarkIDFromUri(uri) + " is deleted.");

                int fav = getContext().getContentResolver().query(
                        BookmarkContract.BookmarkEntry.CONTENT_URI,
                        null, null, null, null).getCount();

                Log.d("SUM: ", "There're " + fav + " movies.");

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated;

        switch (sUriMatcher.match(uri)) {
            case BOOKMARK:
                rowsUpdated = db.update(BookmarkContract.BookmarkEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }
}
