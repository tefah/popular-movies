package com.tefah.popularmovies;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class and methods to git the data from TMDB
 */

public final class QueryUtils {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";

    private static final String baseImagePath = "http://image.tmdb.org/t/p/w500/";
    //my TMDB api key
    private static final String API_KEY = "216c00407684e35f04d752a5c0a434e4";
    // default constructor
    private QueryUtils(){}

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * creat url that will be used by Picasso to download the image
     *
     * @param imagePath the unique image path
     *
     * @return String representing the url of the image
     */
    public static final String creatImageUrl(String imagePath){
        return baseImagePath + imagePath;
    }

    /**
     * by using helper methods it take a String sort and do the networking work, parsing the data into list
     * of movies and return this list
     * @param sortOrder string used to build URL
     * @return List of Movie object that contain the data received
     */
    public static List<Movie> fetchMoviesData (String sortOrder){
         URL url = creatUrl(sortOrder);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Movie> movies = parsingJsonData(jsonResponse);
        return movies;
    }

    /**
     * parse json response to list of Movie objects
     * @param jsonResponse String have the JSON response from TMDB
     */
    private static List<Movie> parsingJsonData(String jsonResponse){
        //in case there is no response return null
        if (TextUtils.isEmpty(jsonResponse))
            return null;
        List<Movie> movies = new ArrayList<>();
        try{
            // JSONObject of the whole response
            JSONObject root = new JSONObject(jsonResponse);
            // JSONArray for the key results
            JSONArray result = root.getJSONArray("results");

            //extracting data for each movie in the result array
            for (int i = 0; i<result.length(); i++){
                String imagePath, title, overview, releaseDate;
                int rate;
                JSONObject currentMovie = result.getJSONObject(i);

                // extract the needed data from the object
                imagePath   = currentMovie.getString("poster_path");
                title       = currentMovie.getString("original_title");
                overview    = currentMovie.getString("overview");
                releaseDate = currentMovie.getString("release_date");
                rate        = currentMovie.getInt("vote_average");

                movies.add(new Movie(imagePath,title,overview,releaseDate,rate));
            }

        }catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return movies;
    }

    /**
     * build a url to specific sort preference
     *
     * @param sortOrder
     * @return URL object
     */
    private static URL creatUrl(String sortOrder){
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(sortOrder);
        uriBuilder.appendQueryParameter("api_key",API_KEY);

        URL url = null;
        try {
            url = new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL
     * @param url
     * @return String response from TMDB
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
