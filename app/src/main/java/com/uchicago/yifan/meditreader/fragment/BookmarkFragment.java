package com.uchicago.yifan.meditreader.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uchicago.yifan.meditreader.BookmarkAdapter;
import com.uchicago.yifan.meditreader.Data.BookmarkContract;
import com.uchicago.yifan.meditreader.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private BookmarkAdapter adapter;
    private ListView listView;

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

    public static final int COL_ID = 0;
    public static final int COL_POST_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_DATE = 3;
    public static final int COL_DESCRIPTION = 4;
    public static final int COL_POST_TYPE = 5;
    public static final int COL_URL = 6;
    public static final int COL_USER_ID = 7;

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
        adapter = new BookmarkAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_bookmark, container, false);

        listView = (ListView)rootView.findViewById(R.id.bookmark_list);
        listView.setAdapter(adapter);
        return rootView;
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
