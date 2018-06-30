package com.codepath.acfoley.flicks.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Movie {

    //values obtained from API
    String title;
    String posterPath;
    String overview;
    String backdropPath;
    Double voteAverage;

    public Movie() {}

    //constructor--initialize from JSON data
    public Movie (JSONObject object) throws JSONException {
        title = object.getString("title"); //use keys
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
    }

    public String getOverview() {
        return overview;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() { return backdropPath; }

    public String getPosterPath() { return posterPath; }

    public Double getVoteAverage() { return voteAverage; }
}
