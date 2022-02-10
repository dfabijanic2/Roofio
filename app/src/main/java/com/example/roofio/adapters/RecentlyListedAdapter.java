package com.example.roofio.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roofio.ListingDetailsActivity;
import com.example.roofio.ListingsByCategoryActivity;
import com.example.roofio.R;
import com.example.roofio.models.Category;
import com.example.roofio.models.Property;
import com.example.roofio.models.PropertyInfo;

import java.util.List;

public class RecentlyListedAdapter extends RecyclerView.Adapter<RecentlyListedAdapter.RecentlyListingViewHolder> {

    Context context;
    List<PropertyInfo> recentlyListedList;

    public RecentlyListedAdapter(Context context, List<PropertyInfo> recentlyListedList) {
        this.context = context;
        this.recentlyListedList = recentlyListedList;
    }

    public void setProperties(List<PropertyInfo> properties){
        recentlyListedList = properties;
    }

    @NonNull
    @Override
    public RecentlyListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.recently_listed_row, parent, false);

       return new RecentlyListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyListingViewHolder holder, int position) {
        holder.location.setText(recentlyListedList.get(position).getLokacija());
        holder.price.setText(recentlyListedList.get(position).getCijena().toString());
        Glide.with(context).load(recentlyListedList.get(position).getSlika()).into(holder.featuredImage);
    }

    @Override
    public int getItemCount() {
        return recentlyListedList.size();
    }

    public class RecentlyListingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView location, price;
        ImageView featuredImage;

        public RecentlyListingViewHolder(@NonNull View itemView) {
            super(itemView);

            location = itemView.findViewById(R.id.locationTextView);
            price = itemView.findViewById(R.id.priceTextView);
            featuredImage = itemView.findViewById(R.id.featureImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PropertyInfo pi = recentlyListedList.get(getAdapterPosition());
            Intent i = new Intent(v.getContext(), ListingDetailsActivity.class);
            i.putExtra("listingKey", pi.getKey());
            context.startActivity(i);
        }
    }


}
