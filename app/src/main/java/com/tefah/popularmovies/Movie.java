package com.tefah.popularmovies;

import java.io.Serializable;

/**
 * Class to save the movie features
 */

public class Movie implements Serializable {
    private String imageUrl, title, overview, releaseDate;
    private int rate;
    public String getImageUrl(){ return imageUrl;}
    public String getTitle(){ return title;}
    public String getOverview(){return overview;}
    public String getReleaseDate(){return releaseDate;}
    public int getRate(){return rate;}
}