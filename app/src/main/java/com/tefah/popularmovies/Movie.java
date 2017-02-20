package com.tefah.popularmovies;

import java.io.Serializable;

/**
 * Class to save the movie features
 */

public class Movie implements Serializable {
    private String posterPath, backdropPath, title, overview, releaseDate;
    private double rate;

    public Movie(String posterPath, String backdropPath, String title, String overview, String releaseDate, double rate){
        this.posterPath     = posterPath;
        this.backdropPath   = backdropPath;
        this.title          = title;
        this.overview       = overview;
        this.releaseDate    = releaseDate;
        this.rate           = rate;
    }
    public String getPosterPath(){ return posterPath;}
    public String getBackdropPath(){return backdropPath;};
    public String getTitle(){ return title;}
    public String getOverview(){return overview;}
    public String getReleaseDate(){return releaseDate;}
    public double getRate(){return rate;}
}