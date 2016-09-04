package com.uchicago.yifan.meditreader.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uchicago.yifan.meditreader.BookmarkAdapter;
import com.uchicago.yifan.meditreader.Data.BookmarkContract;
import com.uchicago.yifan.meditreader.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

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

    public BookmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(BOOKMARK_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(), BookmarkContract.BookmarkEntry.CONTENT_URI,
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
