package com.tefah.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    Movie movie;
    ImageView poster, backdrop;
    TextView  name, releaseDate, overview, rate, title;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
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
        name        = (TextView)  findViewById(R.id.name);
        releaseDate = (TextView)  findViewById(R.id.date);
        overview    = (TextView)  findViewById(R.id.overview);
        rate        = (TextView)  findViewById(R.id.text_rate);
        title       = (TextView)  findViewById(R.id.title);
        ratingBar   = (RatingBar) findViewById(R.id.rate);



        bind();


    }
    public void bind(){
        Picasso.with(this).load(QueryUtils.creatImageUrlSmall(movie.getPosterPath())).into(poster);
        Picasso.with(this).load(QueryUtils.creatImageUrl(movie.getBackdropPath())).into(backdrop);
        name.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        overview.setText(movie.getOverview());
        ratingBar.setMax(10);
        ratingBar.setProgress((int) movie.getRate());
        rate.setText( String.valueOf(movie.getRate()));

    }


}
