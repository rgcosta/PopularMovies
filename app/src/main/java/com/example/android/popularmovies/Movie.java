package com.example.android.popularmovies;

/**
 * Created by romul on 13/10/2016.
 */
public class Movie {
    String posterPathFull;
    long id;
    String originalTitle;
    String Synopsis;
    double voteAverage;
    String releaseDate;
    public static String posterSize = "w154";


    public Movie(long id, String posterPath, String originalTitle, String Synopsis, double voteAverage, String releaseDate){
        this.posterPathFull = posterPath;
        this.id = id;
        this.originalTitle = originalTitle;
        this.Synopsis = Synopsis;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public String getPosterURL(){
        return this.posterPathFull;
    }

}
