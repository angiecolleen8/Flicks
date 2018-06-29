package com.codepath.acfoley.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    //secure base url for loading images
    String imageBaseUrl;
    //poster size to use when fetching images
    String posterSize;
    //backdrop size to use for fetching images
    String backdropSize;

    //JSON constructor
    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        imageBaseUrl = images.getString("secure_base_url");
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //try to use option 3, else size 342
        posterSize = posterSizeOptions.optString(3, "w342");
        //parse the backdrop size and try to use the size at index 1, otherwise use the default size of 780
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    //helper methods for creating urls
    public String getImageUrl(String size, String path) { ///concatenate 3 pieces of image url
       return String.format("%s%s%s", imageBaseUrl, size, path);
    }

    //getter methods
    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
