package com.tefah.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import com.tefah.popularmovies.Movie;
import java.util.ArrayList;
import java.util.List;

/**
 * Utils class to help in manipulating data in the database
 */

public class DataUtils {

    //column indices in db
    public static final int     POSTER_IND      = 0;
    public static final int     BACKDROP_IND    = POSTER_IND + 1;
    public static final int     TITLE_IND       = BACKDROP_IND + 1;
    public static final int     OVERVIEW_IND    = TITLE_IND + 1;
    public static final int     RATE_IND        = OVERVIEW_IND + 1;
    public static final int     RELEASE_IND     = RATE_IND + 1;
    public static final int     ID_IND          = RELEASE_IND + 1;

    public static List<Movie> getDataFromDB(Context context){
        Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null, null, null, null);
        List<Movie> movies = fetchDataFromCursor(cursor);
        return  movies;
    }
    public static List<Movie> fetchDataFromCursor(Cursor cursor){
        List<Movie> movies = new ArrayList<Movie>();
        while (cursor.moveToNext()){
            Movie movie;
            String posterPath, backdropPath, title, overview, releaseDate;
            double rate;
            int id;

            posterPath      = cursor.getString(POSTER_IND);
            backdropPath    = cursor.getString(BACKDROP_IND);
            title           = cursor.getString(TITLE_IND);
            overview        = cursor.getString(OVERVIEW_IND);
            releaseDate     = cursor.getString(RELEASE_IND);
            rate            = cursor.getDouble(RATE_IND);
            id              = cursor.getInt(ID_IND);

            movie = new Movie(posterPath, backdropPath, title, overview, releaseDate, rate, id);
            movies.add(movie);
        }
        return movies;
    }
}
