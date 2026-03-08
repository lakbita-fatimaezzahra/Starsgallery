package com.example.galeriedestars.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.galeriedestars.R;
import com.example.galeriedestars.beans.Star;
import com.example.galeriedestars.service.StarService;

import java.util.ArrayList;
import java.util.List;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> implements Filterable {

    private List<Star> stars;
    private List<Star> starsFilter;
    private Context context;
    private NewFilter mfilter;

    public StarAdapter(Context context, List<Star> stars) {
        this.context = context;
        this.stars = stars;
        this.starsFilter = new ArrayList<>(stars);
        this.mfilter = new NewFilter(this);
    }

    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.star_item, parent, false);
        final StarViewHolder holder = new StarViewHolder(v);

        holder.itemView.setOnClickListener(view -> {
            int position = holder.getAdapterPosition();
            Star star = starsFilter.get(position);

            View popup = LayoutInflater.from(context).inflate(R.layout.star_edit_item, null, false);
            ImageView img = popup.findViewById(R.id.img);
            RatingBar ratingBar = popup.findViewById(R.id.ratingBar);
            TextView idText = popup.findViewById(R.id.idss);

            ratingBar.setRating(star.getRating());
            idText.setText(String.valueOf(star.getId()));

            Glide.with(context)
                    .load(star.getImg())
                    .placeholder(R.drawable.images)
                    .circleCrop()
                    .into(img);

            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Notez : " + star.getName())
                    .setView(popup)
                    .setPositiveButton("Valider", (dialogInterface, i) -> {
                        star.setRating(ratingBar.getRating());
                        StarService.getInstance().update(star);
                        notifyItemChanged(position);
                    })
                    .setNegativeButton("Annuler", null)
                    .create();

            dialog.show();
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder holder, int position) {
        Star s = starsFilter.get(position);

        holder.name.setText(s.getName());
        holder.rating.setRating(s.getRating());

        Glide.with(context)
                .load(s.getImg())
                .placeholder(R.drawable.images)
                .circleCrop()
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return starsFilter.size();
    }

    @Override
    public Filter getFilter() { return mfilter; }

    static class StarViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, idText;
        RatingBar rating;

        StarViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgStar);
            name = itemView.findViewById(R.id.tvName);
            rating = itemView.findViewById(R.id.rating);
        }
    }

    private class NewFilter extends Filter {
        private RecyclerView.Adapter mAdapter;

        NewFilter(RecyclerView.Adapter adapter) { this.mAdapter = adapter; }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Star> filtered = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filtered.addAll(stars);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Star s : stars) {
                    if (s.getName().toLowerCase().contains(filterPattern)) filtered.add(s);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            results.count = filtered.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            starsFilter = (List<Star>) filterResults.values;
            mAdapter.notifyDataSetChanged();
        }
    }
}