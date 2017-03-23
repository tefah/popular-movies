package com.tefah.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tefah.popularmovies.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
implements MovieAdapter.MovieClickListener,
        LoaderCallbacks<List<Movie>>{

    private static final String LOG_TAG     = MainActivity.class.getName();
    //strings for sorting order
    private static final String POPULAR     = "popular";
    private static final String TOP_RATED   = "top_rated";
    //constants for loader id and the key of sort order
    private static final int    LOADER_ID           = 1;
    public static final int     FAVORITES_LOADER_ID = 100;
    public static final String  SORT_KEY            = "sort key";


    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    TextView errorMessage;
    ProgressBar loadingIndicator;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayoutManager layoutManager;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
             layoutManager = new GridLayoutManager(this,3);
        } else
            layoutManager = new GridLayoutManager(this, 2);
        movieAdapter = new MovieAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setHasFixedSize(true);
        bundle  = new Bundle();

        bundle.putString(SORT_KEY,POPULAR);
        getLoaderManager().initLoader(LOADER_ID,bundle,this);
    }
    public void showDataOnScreen(){
        recyclerView.setVisibility(View.VISIBLE);
        errorMessage = (TextView) findViewById(R.id.error_message_tv);
        errorMessage.setVisibility(View.GONE);
    }
    public void showErrorMessage(){
        recyclerView.setVisibility(View.GONE);
        errorMessage = (TextView) findViewById(R.id.error_message_tv);
        errorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
        intent.putExtra("movie",movie);
        startActivity(intent);
    }

    /**
     * loader callback function to create new loader to handle task in the background
     * @param i
     * @param args bundle that holds params that would be needed in the background task
     * @return loader object
     */
    @Override
    public android.content.Loader<List<Movie>> onCreateLoader(int i, Bundle args) {
        if (args.isEmpty())
          return null;
        String sortOrder = args.getString(SORT_KEY);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        return new MovieLoader(this, sortOrder, loadingIndicator);
    }

    /**
     * a callback function that is called after the loader finish his background task
     * @param loader that handeled the background task
     * @param movies list of movies
     */
    @Override
    public void onLoadFinished(android.content.Loader<List<Movie>> loader, List<Movie> movies) {
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        if (movies!=null) {
            showDataOnScreen();
            movieAdapter.setMovies(movies);
        }else
            showErrorMessage();
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Movie>> loader) {

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.popular:
                bundle.putString(SORT_KEY,POPULAR);
                getLoaderManager().initLoader(LOADER_ID,bundle,this);
                setTitle(R.string.app_name);
                break;
            case R.id.top_rated:
                bundle.putString(SORT_KEY,TOP_RATED);
                getLoaderManager().initLoader(LOADER_ID + 1, bundle,this);
                setTitle(R.string.top_rated);
                break;
            case R.id.favorites_menu:
               setTitle(getString(R.string.favorites));
                getLoaderManager().initLoader(FAVORITES_LOADER_ID + 1, bundle,this);
                break;
            default:
                break;
        }
        return true;
    }

}

