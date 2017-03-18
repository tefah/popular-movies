package com.tefah.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * Class to save the movie features
 */
public class Movie implements Parcelable {
    private String posterPath, backdropPath, title, overview, releaseDate;
    private double rate;
    private int    id;

    public Movie(String posterPath, String backdropPath, String title, String overview, String releaseDate, double rate, int id){
        this.posterPath     = posterPath;
        this.backdropPath   = backdropPath;
        this.title          = title;
        this.overview       = overview;
        this.releaseDate    = releaseDate;
        this.rate           = rate;
        this.id             = id;
    }
    public Movie(Parcel parcel){
        this.posterPath     = parcel.readString();
        this.backdropPath   = parcel.readString();
        this.title          = parcel.readString();
        this.overview       = parcel.readString();
        this.releaseDate    = parcel.readString();
        this.rate           = parcel.readDouble();
        this.id             = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeDouble(rate);
        parcel.writeInt(id);
    }

    public String getPosterPath(){ return posterPath;}
    public String getBackdropPath(){return backdropPath;}
    public String getTitle(){ return title;}
    public String getOverview(){return overview;}
    public String getReleaseDate(){return releaseDate;}
    public double getRate(){return rate;}
    public int getId() {
        return id;
    }

    public final static Creator<Movie> CREATOR = new Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}