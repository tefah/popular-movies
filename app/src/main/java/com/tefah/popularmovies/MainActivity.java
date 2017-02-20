package com.tefah.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
implements MovieAdapter.MovieClickListener{





    private static final String LOG_TAG     = MainActivity.class.getName();
    //strings for sorting order
    private static final String POPULAR     = "popular";
    private static final String TOP_RATED   = "top_rated";

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    MovieAsync async;
    TextView errorMessage;
    ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        movieAdapter = new MovieAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setHasFixedSize(true);

        MovieAsync async = new MovieAsync();
        async.execute(POPULAR);
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

    public class MovieAsync extends AsyncTask<String,Void,List<Movie>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... sort) {
            if (sort.length == 0) {
                return null;
            }

          List<Movie> movies;
            movies = QueryUtils.fetchMoviesData(sort[0]);
            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            if (movies!=null) {
                showDataOnScreen();
                movieAdapter.setMovies(movies);
            }else
                showErrorMessage();
        }
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
                async = new MovieAsync();
                async.execute(POPULAR);
                setTitle(R.string.app_name);
                break;
            case R.id.top_rated:
                async = new MovieAsync();
                async.execute(TOP_RATED);
                setTitle(R.string.top_rated);
                break;
            default:
                break;
        }
        return true;
    }
}
