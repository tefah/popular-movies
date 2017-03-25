package com.tefah.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tefah.popularmovies.data.DataUtils;
import com.tefah.popularmovies.data.MovieContract.MovieEntry;
import com.tefah.popularmovies.databinding.ActivityDetailsBinding;
import java.net.URL;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerClickListener {

    Movie movie;
    List<String> keys;
    List<String> reviews;
    TrailerAdapter trailerAdapter;
    ReviewsAdapter reviewsAdapter;

    ActivityDetailsBinding binding;
    ImageView poster, backdrop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        Intent intent = getIntent();
        if (intent.hasExtra("movie")){
            movie = intent.getParcelableExtra("movie");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(movie.getTitle().trim());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        poster      = (ImageView) findViewById(R.id.poster_view);
        backdrop    = (ImageView) findViewById(R.id.backdrop);
        bind();

        trailerAdapter = new TrailerAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.trailerRv.setLayoutManager(layoutManager);
        binding.trailerRv.setAdapter(trailerAdapter);
        binding.trailerRv.setHasFixedSize(true);

        LinearLayoutManager reviewsLM = new LinearLayoutManager(this);
        reviewsAdapter = new ReviewsAdapter();
        binding.reviewRv.setLayoutManager(reviewsLM);
        binding.reviewRv.setAdapter(reviewsAdapter);
        binding.reviewRv.setHasFixedSize(true);

        new AsyncTask<Integer, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Integer... ids) {
                String selection = "id=?";
                String[] selectionArgs = {ids[0].toString()};
                Cursor cursor = getContentResolver().query(MovieEntry.CONTENT_URI,
                        null,
                        selection,
                        selectionArgs,
                        null);
                return cursor;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                if (cursor.moveToNext())
                    if (cursor.getInt(DataUtils.IS_FAVORITE_IND) > 0) {
                        movie.setFavorite(true);
                        binding.favorite.setChecked(true);
                    }
            }
        }.execute(movie.getId());

        binding.favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    if (movie.isFavorite())
                        return;
                    movie.setFavorite(true);
                    ContentValues values = new ContentValues();

                    values.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                    values.put(MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
                    values.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
                    values.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                    values.put(MovieEntry.COLUMN_RATE, movie.getRate());
                    values.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                    values.put(MovieEntry.COLUMN_ID, movie.getId());
                    values.put(MovieEntry.COLUMN_IS_FAVORITE, movie.isFavorite());

                    Uri uri = getContentResolver().insert(MovieEntry.CONTENT_URI, values);
                    if (uri == null)
                        movie.setFavorite(false);

                }else {
                    String[] selectionArgs = {String.valueOf(movie.getId())};
                    int numOfRows = getContentResolver().delete(MovieEntry.CONTENT_URI,
                            "id=?", selectionArgs);
                    if (numOfRows > 0)
                        movie.setFavorite(false);
                }
            }
        });

        /**
         * background task to handle graping reviews and trailers from the TMDB
         */
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                keys    = QueryUtils.getTrailers(movie.getId());
                reviews = QueryUtils.getReviews(movie.getId());
                return keys;
            }

            @Override
            protected void onPostExecute(List<String> keyList) {
                trailerAdapter.setData(keys);
                reviewsAdapter.setData(reviews);
            }
        }.execute();

    }

    /**
     * function to bind views with data using dataBinding class
     */
    public void bind(){
        Picasso.with(this).load(QueryUtils.creatImageUrlSmall(movie.getPosterPath())).into(poster);
        Picasso.with(this).load(QueryUtils.creatImageUrl(movie.getBackdropPath())).into(backdrop);
        binding.name.setText(movie.getTitle());
        binding.date.setText(movie.getReleaseDate());
        binding.overview.setText(movie.getOverview());
        binding.rate.setMax(10);
        binding.rate.setProgress((int) movie.getRate());
        binding.textRate.setText( String.valueOf(movie.getRate()));
    }


    //// TODO: 3/21/2017 update any landscape layouts
    //// TODO: 3/21/2017 befor insert check if the movie is already inserted
    //// TODO: 3/21/2017 change the colors to some thing better may be teel
    //// TODO: 3/21/2017 add some comments
    //// TODO: 3/21/2017 delete these shit and upload we rbna yostr
    //// TODO: 3/22/2017 clean the project automatically
    //// TODO: 3/22/2017 clean it again manually

    @Override
    public void onTrailerClick(String key) {
        URL url = QueryUtils.createTrailerUrl(key);
        Uri uri = Uri.parse(url.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
