package com.codepath.acfoley.flicks.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {

    //values obtained from API
    private String title;
    private String posterPath;
    private String overview;
    private String backdropPath;

    //constructor--initialize from JSON data
    public Movie (JSONObject object) throws JSONException {
        title = object.getString("title"); //use keys
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
    }

    public String getOverview() {
        return overview;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() { return backdropPath; }

    public String getPosterPath() { return posterPath; }

}
