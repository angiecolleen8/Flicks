package com.codepath.acfoley.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    //secure base url for loading images
    String imageBaseUrl;
    //poster size to use when fetching images
    String posterSize;

    //JSON constructor
    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        imageBaseUrl = images.getString("secure_base_url");
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //try to use option 3, else size 342
        posterSize = posterSizeOptions.optString(3, "w342");

    }
}
