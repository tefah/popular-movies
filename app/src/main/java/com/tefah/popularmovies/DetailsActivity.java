package com.tefah.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    Movie movie;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        if (intent.hasExtra("movie")){
            movie = (Movie) intent.getSerializableExtra("movie");
            tv = (TextView) findViewById(R.id.show_movie);
            tv.setText(String.valueOf(movie));
        }


    }
}
