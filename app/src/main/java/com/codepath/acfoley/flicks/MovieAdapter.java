package com.codepath.acfoley.flicks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.acfoley.flicks.models.Config;
import com.codepath.acfoley.flicks.models.GlideApp;
import com.codepath.acfoley.flicks.models.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //list of movies
    ArrayList<Movie> movies;
    //we need config for image urls
    Config config;
    //context for rendering
    Context context;

    //initialize with list of movies
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setConfig(Config config){this.config = config;}

    //creates and inflates a new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //get the context and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create viewholder using item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        //return new ViewHolder
        return new ViewHolder(movieView);
    }

    //binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    //get the movie data at the specified position
        Movie movie = movies.get(position);

        //populate View with movie data
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        //build image url using config
        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());

        //load image using glide
        GlideApp.with(context)
                .load(imageUrl)
                .transform(new RoundedCorners(25))
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .into(viewHolder.ivPosterImage);
    }

    //returns total number of items in list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create ViewHolder - static inner class
    static class ViewHolder extends RecyclerView.ViewHolder{

        //track view objects
        ImageView ivPosterImage;
        TextView tvOverview;
        TextView tvTitle;


        public ViewHolder(View itemView) {
            super(itemView);
            //lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage); //Why is casting to an ImageView necessary here?
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
