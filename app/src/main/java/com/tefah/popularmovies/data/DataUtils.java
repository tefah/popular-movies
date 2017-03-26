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
    public static final int     IS_FAVORITE_IND = ID_IND +1;

    /**
     * query the favorites movies from database and parse it into list of movies using helper method
     * @param context
     * @return list of movies to be shown as favorites
     */
    public static List<Movie> getDataFromDB(Context context){
        Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null, null, null, null);
        List<Movie> movies = fetchDataFromCursor(cursor);
        return  movies;
    }

    /**
     * fetch data from cursor to list of movies
     * @param cursor
     * @return list of movies
     */
    private static List<Movie> fetchDataFromCursor(Cursor cursor){
        List<Movie> movies = new ArrayList<>();
        while (cursor.moveToNext()){
            Movie movie;
            String posterPath, backdropPath, title, overview, releaseDate;
            double rate;
            int id, isFavorite;

            posterPath      = cursor.getString(POSTER_IND);
            backdropPath    = cursor.getString(BACKDROP_IND);
            title           = cursor.getString(TITLE_IND);
            overview        = cursor.getString(OVERVIEW_IND);
            releaseDate     = cursor.getString(RELEASE_IND);
            rate            = cursor.getDouble(RATE_IND);
            id              = cursor.getInt(ID_IND);
            isFavorite      = cursor.getInt(IS_FAVORITE_IND);

            movie = new Movie(posterPath, backdropPath, title, overview, releaseDate, rate, id);
            if (isFavorite > 0)
                movie.setFavorite(true);
            movies.add(movie);
        }
        return movies;
    }
}
