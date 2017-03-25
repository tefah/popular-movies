package com.tefah.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by TEFA on 3/12/2017.
 */

public class MovieContract {
    public static final String  AUTHORITY = "com.tefah.popularmovies";
    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public final static String MOVIES_PATH = "movie";

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "FavouriteMovies";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(MOVIES_PATH).build();

        public static final String COLUMN_POSTER_PATH   = "poster";
        public static final String COLUMN_BACKDROP_PATH = "backdrop";
        public static final String COLUMN_TITLE         = "title";
        public static final String COLUMN_OVERVIEW      = "overview";
        public static final String COLUMN_RELEASE_DATE  = "release";
        public static final String COLUMN_RATE          = "rate";
        public static final String COLUMN_ID            = "id";
        public static final String COLUMN_IS_FAVORITE   = "is_favorite";


    }
}
