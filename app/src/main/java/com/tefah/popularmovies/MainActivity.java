package com.tefah.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        URL url = QueryUtils.creatUrl("popular");
        MovieAsync movieAsync = new MovieAsync();
        movieAsync.execute(url);

    }
    public class MovieAsync extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            String jsonResponse =null;
            try {
                jsonResponse = QueryUtils.makeHttpRequest(urls[0]);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            TextView textView = (TextView) findViewById(R.id.response);
            textView.setText(response);
        }
    }
}
