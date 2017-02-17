package com.tefah.popularmovies;

import java.io.Serializable;

/**
 * Class to save the movie features
 */

public class Movie implements Serializable {
    private String imagePath, title, overview, releaseDate;
    private int rate;

    public Movie(String imagePath,String title, String overview, String releaseDate, int rate){
        this.imagePath   = imagePath;
        this.title       = title;
        this.overview    = overview;
        this.releaseDate = releaseDate;
        this.rate        = rate;
    }
    public String getImageUrl(){ return imagePath;}
    public String getTitle(){ return title;}
    public String getOverview(){return overview;}
    public String getReleaseDate(){return releaseDate;}
    public int getRate(){return rate;}
}