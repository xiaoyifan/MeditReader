package com.uchicago.yifan.meditreader.Activities;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.uchicago.yifan.meditreader.BookmarkAdapter;
import com.uchicago.yifan.meditreader.Data.BookmarkContract;
import com.uchicago.yifan.meditreader.R;

public class BookmarkActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private BookmarkAdapter adapter;

    private static final int BOOKMARK_LOADER = 0;

    public static final String[] BOOKMARK_COLUMNS = {
            BookmarkContract.BookmarkEntry._ID,
            BookmarkContract.BookmarkEntry.COLUMN_POST_ID,
            BookmarkContract.BookmarkEntry.COLUMN_TITLE,
            BookmarkContract.BookmarkEntry.COLUMN_DATE,
            BookmarkContract.BookmarkEntry.COLUMN_DESCRIPTION,
            BookmarkContract.BookmarkEntry.COLUMN_POST_TYPE,
            BookmarkContract.BookmarkEntry.COLUMN_URL,
            BookmarkContract.BookmarkEntry.COLUMN_USER_ID
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        getLoaderManager().initLoader(BOOKMARK_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, BookmarkContract.BookmarkEntry.CONTENT_URI,
                BOOKMARK_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
