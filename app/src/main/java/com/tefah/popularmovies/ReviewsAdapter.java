package com.tefah.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter class for reviews recycler view
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>{
    List<String> reviews;
    Context context;

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int itemId = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(itemId,parent,shouldAttachToParentImmediately);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (reviews == null)
            return 0;
        return reviews.size();
    }

    public void setData(List<String> reviews){
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder{
        TextView reviewTV ;
        TextView reviewNum;
        public ReviewsViewHolder(View itemView) {
            super(itemView);
            reviewTV = (TextView) itemView.findViewById(R.id.review_tv);
            reviewNum = (TextView) itemView.findViewById(R.id.review_num);
        }

        public void bind(int position) {
            reviewTV.setText(reviews.get(getAdapterPosition()));
            reviewNum.setText(position+1 +"");
        }
    }
}
