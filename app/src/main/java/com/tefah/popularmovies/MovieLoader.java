package com.tefah.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.view.View;

import com.tefah.popularmovies.data.DataUtils;

import java.util.List;

/**
 * movie loader to handle background task properly and cache the data
 */

class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private String sortOrder;
    private View loadingIndicator;
    private List<Movie> movies;
    public MovieLoader(Context context, String sortOrder, View view) {
        super(context);
        this.sortOrder = sortOrder;
        this.loadingIndicator = view;
    }

    @Override
    protected void onStartLoading() {

        loadingIndicator.setVisibility(View.VISIBLE);
        if (movies != null)
            deliverResult(movies);
        else
            forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        List<Movie> movies;
        if (getId() >= MainActivity.FAVORITES_LOADER_ID){
            movies = DataUtils.getDataFromDB(getContext());
        }else {
            movies = QueryUtils.fetchMoviesData(sortOrder);
        }
        return movies;
    }

    @Override
    public void deliverResult(List<Movie> data) {
        this.movies = data;
        loadingIndicator.setVisibility(View.GONE);
        super.deliverResult(data);
    }
}