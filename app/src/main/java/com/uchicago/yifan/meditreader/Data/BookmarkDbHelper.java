package com.uchicago.yifan.meditreader.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yifan on 8/28/16.
 */
public class BookmarkDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "meditreader.db";

    public BookmarkDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_BOOKMARK_TABLE = "CREATE TABLE " + BookmarkContract.BookmarkEntry.TABLE_NAME + " (" +
                BookmarkContract.BookmarkEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BookmarkContract.BookmarkEntry.COLUMN_POST_ID + " TEXT NOT NULL, " +
                BookmarkContract.BookmarkEntry.COLUMN_USER_ID + " TEXT NOT NULL, " +
                BookmarkContract.BookmarkEntry.COLUMN_TITLE + " TEXT, " +
                BookmarkContract.BookmarkEntry.COLUMN_URL + " TEXT," +
                BookmarkContract.BookmarkEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                BookmarkContract.BookmarkEntry.COLUMN_DESCRIPTION + " TEXT, " +
                BookmarkContract.BookmarkEntry.COLUMN_POST_TYPE + " TEXT NOT NULL, " +
                "UNIQUE (" + BookmarkContract.BookmarkEntry.COLUMN_POST_ID + " ) ON CONFLICT REPLACE)";

        sqLiteDatabase.execSQL(SQL_CREATE_BOOKMARK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BookmarkContract.BookmarkEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
