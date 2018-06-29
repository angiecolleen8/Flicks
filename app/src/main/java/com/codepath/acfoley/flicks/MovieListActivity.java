package com.codepath.acfoley.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.codepath.acfoley.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    //constants
    //base url for API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //parameter name for API key
    public final static String API_KEY_PARAM = "api_key";
    //tag for logging
    public final static String TAG = "MovieListActivity";

    //parsed list of currently playing movies
    ArrayList<Movie> movies;
    //the recycler view
    RecyclerView rvMovies;
    //Adapter wired to recycler view
    MovieAdapter adapter;


    //instance fields - only have values associated with instances of MovieListActivity
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //initialize client
        client = new AsyncHttpClient();
        //initialize ArrayList movies
        movies = new ArrayList<>();
        //initialize adapter - movies cannot be reintialized after this point
        adapter = new MovieAdapter(movies);

        //resolve the recycler view and connect a layout manager and the adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this)); //'this' is reference to activity
        rvMovies.setAdapter(adapter);

        //get configuration upon app creation
        getConfiguration();
    }

    //get list of currently playing movies from api
    private void getNowPlaying() {
        //create url
        String url = API_BASE_URL + "/movie/now_playing";
        //set request params
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key --required
        //execute GET request. expect JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load results into movie array
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        //notify adapter that row was added
                        adapter.notifyItemInserted(movies.size() -1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing.", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                logError("Failed to get data from now playing endpoint.", throwable, true);
            }
        });

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

                    Log.i(TAG, String.format("Loaded configuration imageBaseURL %s and posterSize %s", posterSize, imageBaseUrl));
                    //get now playing movie list
                    getNowPlaying(); //moved here so that getConfiguration has to execute BEFORE getNowPlaying

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
