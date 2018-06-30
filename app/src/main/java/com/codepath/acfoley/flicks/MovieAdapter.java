package com.codepath.acfoley.flicks;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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

import org.parceler.Parcels;

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

    public void setConfig(Config config) {
        this.config = config;
    }

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

        //determine current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        //build image url using config
        String imageUrl = null;

        //if in portrait mode, load portrait poster
        if (isPortrait) {
            imageUrl = config.getImageUrl(/*config.getPosterSize()*/"", movie.getPosterPath());
        } else {
            imageUrl = config.getImageUrl(/*config.getBackdropSize()*/ "", movie.getBackdropPath()); //make getBackropPosterSize method
        }
        //get correct placeholder and imageView for the current orientation
        int placeholderID = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? viewHolder.ivPosterImage : viewHolder.ivBackdropView;

        //load image using glide
        GlideApp.with(context)
                .load(imageUrl)
                .transform(new RoundedCorners(25))
                .placeholder(placeholderID)
                .error(placeholderID)
                .into(imageView);
    }

    //returns total number of items in list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create ViewHolder - static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropView;
        TextView tvOverview;
        TextView tvTitle;


        public ViewHolder(View itemView) {
            super(itemView);
            //lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            ivBackdropView = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this is why class can't be static
                Movie movie = movies.get(position);
                // create intent for new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize movie using parceller, short name is key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
