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
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class and methods to git the data from TMDB
 */

public final class QueryUtils {

    //strings to  different end points
    public static final String VIDEOS_END_POINT = "videos";
    public static final String REVIEWS_END_POINT = "reviews";

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String BASE_IMAGE_PATH = "http://image.tmdb.org/t/p/w500/";
    private static final String BASE_IMAGE_PATH_THUMBNAIL = "http://image.tmdb.org/t/p/w185/";
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch";

    //my TMDB api key
    private static final String API_KEY = "216c00407684e35f04d752a5c0a434e4";
    // default constructor
    private QueryUtils(){}

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * create the trailer url that will be used in an implicit intent
     * @param key query parameter that every url to video on youtube has one
     * @return url of the trailer
     */
    public static URL createTrailerUrl(String key){
        Uri baseUri = Uri.parse(BASE_YOUTUBE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("v", key);

        URL url = null;
        try {
            url = new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * create url to TMDB to search with specific movie id to two different end point
     * @param id the movie id we want search
     * @param endPoint the data to be got, there is only two here videos and reviews
     * @return url to TMDB
     */
    private static URL createDetailsUrl (int id, String endPoint){
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(String.valueOf(id));
        uriBuilder.appendPath(endPoint);
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
     * parse the json response by extracting the keys of the trailers to  list of keys
     * @param jsonResponse
     * @return List of strings each one represents a trailer key
     * @throws JSONException
     */
    private static List<String> parseTrailers(String  jsonResponse) throws JSONException{
        JSONObject root = new JSONObject(jsonResponse);
        JSONArray results = root.getJSONArray("results");
        if (results.length() == 0) return null;
        List<String> tralerKeys  = new ArrayList<>(results.length());
        for (int i=0; i<results.length(); i++){
            tralerKeys.add(i, results.getJSONObject(i).getString("key"));
        }
        return tralerKeys;
    }

    /**
     * using helper methods it take the movie id and create url to videos end point
     * and get a json response from TMDB then parsing it to list of keys to finally
     * return it
     * @param id id of the movie
     * @return list of keys of videos
     */
    public static List<String> getTrailers(int id){
        List<String> keys = null;
        URL url = createDetailsUrl(id, VIDEOS_END_POINT);
        try {
            String response = makeHttpRequest(url);
            keys = parseTrailers(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys;
    }

    /**
     * parse the json response of reviews end point into list of strings each one represent review
     * @param response json response from TMDB
     * @return list of reviews
     * @throws JSONException
     */
    private static List<String> parseReviews(String response) throws JSONException{
        JSONObject root = new JSONObject(response);
        JSONArray results = root.getJSONArray("results");
        List<String> reviews = null;
        if (results.length() > 0){
           reviews = new ArrayList<>();
            for (int i =0; i< results.length(); i++)
                reviews.add(i, results.getJSONObject(i).getString("content"));
        }
        return reviews;
    }

    /**
     * it do the same as getTrailers but for reviews end point
     * @param id of the movie
     * @return list of reviews
     */
    public static List<String> getReviews(int id){
        List<String> reviews = null;
        URL url = createDetailsUrl(id, REVIEWS_END_POINT);
        try {
            String response = makeHttpRequest(url);
            reviews = parseReviews(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviews;
    }

    /**
     * creat url that will be used by Picasso to download the image
     *
     * @param imagePath the unique image path
     *
     * @return String representing the url of the image
     */
    public static final String creatImageUrl(String imagePath){
        return BASE_IMAGE_PATH + imagePath;
    }

    /**
     * for thumbnail poster
     * @param imagePath
     * @return string representing the url of the thumbnail image
     */
    public static final String creatImageUrlSmall(String imagePath){
        return BASE_IMAGE_PATH_THUMBNAIL + imagePath;
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
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }catch (IOException e) {
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
                String posterPath,backdropPath, title, overview, releaseDate;
                double rate;
                int    id;
                JSONObject currentMovie = result.getJSONObject(i);

                // extract the needed data from the object
                posterPath      = currentMovie.getString("poster_path");
                backdropPath    = currentMovie.getString("backdrop_path");
                title           = currentMovie.getString("original_title");
                overview        = currentMovie.getString("overview");
                releaseDate     = currentMovie.getString("release_date");
                rate            = currentMovie.getDouble("vote_average");
                id              = currentMovie.getInt("id");

                movies.add(new Movie(posterPath,backdropPath,title,overview,releaseDate,rate,id));
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
