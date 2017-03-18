package com.tefah.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by TEFA on 3/9/2017.
 */

public class MovieProvider extends ContentProvider {
    private static final int MOVIES_CODE = 100;
    private static final int SPECIFIC_MOVIE_CODE = 101;
    
    public static final String  AUTHORITY = "com.tefah.popularmovies";
    private static UriMatcher uriMatcher = uriMatcherBuilder();
    private DBHelper dbHelper;


    private static UriMatcher uriMatcherBuilder(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, MovieContract.MOVIES_PATH, MOVIES_CODE);
        uriMatcher.addURI(AUTHORITY, MovieContract.MOVIES_PATH + "/#", SPECIFIC_MOVIE_CODE);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return  true;
    }
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)){
            case MOVIES_CODE:
                cursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        if (cursor != null){
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri = null;
        switch (uriMatcher.match(uri)){
            case MOVIES_CODE:
                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME,
                        null, contentValues);
                if (id > -1){
                    returnUri = uri.withAppendedPath(uri, id+"");

                }
                break;
            default:
                throw new UnsupportedOperationException();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        Toast.makeText(getContext(),returnUri.toString(),Toast.LENGTH_SHORT).show();
        return returnUri;
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
