package com.codepath.acfoley.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    //constants
    //base url for API
    public final static String API_BASE_URL = "https://api.themoviebd.org/3";
    //parameter name for API key
    public final static String API_KEY_PARAM = "api_key";
    //tag for logging
    public final static String TAG = "MovieListActivity";
    //secure base url for loading images
    String imageBaseUrl;
    String posterSize;


    //instance fields - only have values associated with instances of MovieListActivity
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //initialize client
        client = new AsyncHttpClient();
        //get configuration upon app creation
        getConfiguration();
    }

    private void getConfiguration() {
        //create url
        String url = API_BASE_URL + "/configuration";
        //set request params
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key --required
        //execute GET request. expect JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                //poster size to use when fetching images - part of url
                try {
                    //
                    JSONObject images = response.getJSONObject("images");
                    imageBaseUrl = images.getString("secure_base_url");
                    JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
                //try to use option 3, else size 342
                    posterSize = posterSizeOptions.optString(3, "w342");
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });

    }

    //handle errors and alert user
    private void logError(String message, Throwable error, boolean alertUser){
        //always log error
        Log.e(TAG, message, error);
        //avoid silent errors by alerting user
        if (alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
