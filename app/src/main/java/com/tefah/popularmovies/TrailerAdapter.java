package com.tefah.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{
    List<String> keys = null;
    Context context;

    private final TrailerClickListener clickListener;

    public interface TrailerClickListener{
        public void onTrailerClick(String key);
    }


    public TrailerAdapter(TrailerClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setData(List<String> keys){
        this.keys = keys;
        notifyDataSetChanged();
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int itemId = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(itemId,parent,shouldAttachToParentImmediately);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (keys == null)
            return 0;
        return keys.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        TextView index ;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            index = (TextView) itemView.findViewById(R.id.trailer_num);
        }
        public void bind(int position){
            index.setText(context.getResources().getString(R.string.trailer) + " " + position);
        }

        @Override
        public void onClick(View view) {
            clickListener.onTrailerClick(keys.get(getAdapterPosition()));
        }
    }
}
