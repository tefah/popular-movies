package com.tefah.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

/**
 * the recycler view adapter
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    List<Movie> movies ;
    Context context;

    /**
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our recyclerView
     */
    private final MovieClickListener movieClickListener;

    /**
     * constructor to the MovieAdapter
     * @param mClicklistener listener for the item click
     */
    public MovieAdapter(MovieClickListener mClicklistener) {
        movieClickListener = mClicklistener;
    }

    /**
     * interface to handle recycler view click
     */
    public interface MovieClickListener{
        public void onMovieClick(Movie movie);
    }

    /**
     * set the movies data of the MovieAdapter to the data we received
     * @param movies the new data to display
     */
    public void setMovies(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent    The ViewGroup that these ViewHolders are contained within.
     * @param viewType
     *
     * @return A new MovieViewHolder that holds the View for each list item
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int itemId = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(itemId,parent,shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    /**
     * this method called by the recycler view to update the view with data
     * passing the view holder should be updated
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
       if (null == movies) {
           return 0;
       }
        return movies.size();

    }

    /**
     * ViewHolder class to cache of the children views for an item.
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        final ImageView poster;

        /**
         * constructor for the ViewHolder
         * @param itemView
         */
        public MovieViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poster_view);

            itemView.setOnClickListener(this);
        }

        /**
         * bind the image view with data
         * @param movie
         */
        public void bind(Movie movie){
            String imageUrl = QueryUtils.creatImageUrl(movie.getPosterPath());
            Picasso.with(context).load(imageUrl).into(poster);
        }

        @Override
        public void onClick(View view) {
            Movie movie = movies.get(getAdapterPosition());
            movieClickListener.onMovieClick(movie);
        }
    }
}
