package com.example.roofio.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roofio.models.PropertyInfo;

import java.util.List;

public class TopListingAdapter extends RecyclerView.Adapter<TopListingAdapter.TopListingViewHolder> {

    Context context;
    List<PropertyInfo> topListings;
    @NonNull
    @Override
    public TopListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TopListingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return topListings.size();
    }

    public static class TopListingViewHolder extends RecyclerView.ViewHolder{

        public TopListingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
