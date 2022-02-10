package com.example.roofio.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roofio.ListingDetailsActivity;
import com.example.roofio.ListingsByCategoryActivity;
import com.example.roofio.R;
import com.example.roofio.models.Category;
import com.example.roofio.models.PropertyInfo;

import java.util.List;

public class ListingsByCategoryAdapter extends RecyclerView.Adapter<ListingsByCategoryAdapter.ListingsByCategoryViewHolder> {

    Context context;
    List<PropertyInfo> listingsByCategoryList;

    public ListingsByCategoryAdapter(Context context, List<PropertyInfo> listingsByCategoryList) {
        this.context = context;
        this.listingsByCategoryList = listingsByCategoryList;
    }

    @NonNull
    @Override
    public ListingsByCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.listings_by_category_row, parent, false);

       return new ListingsByCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingsByCategoryViewHolder holder, int position) {
        holder.location.setText(listingsByCategoryList.get(position).getLokacija());
        holder.price.setText(listingsByCategoryList.get(position).getCijena().toString());
        holder.name.setText(listingsByCategoryList.get(position).getNaziv());
        holder.status.setText(listingsByCategoryList.get(position).getStatus().toString());
        holder.numRoom.setText(listingsByCategoryList.get(position).getBrojSoba().toString());
        Glide.with(context).load(listingsByCategoryList.get(position).getSlika()).into(holder.featuredImage);
    }

    @Override
    public int getItemCount() {
        return listingsByCategoryList.size();
    }

    public class ListingsByCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView location, price, name, status, numRoom;
        ImageView featuredImage;

        public ListingsByCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            location = itemView.findViewById(R.id.locationTextView);
            price = itemView.findViewById(R.id.priceTextView);
            name = itemView.findViewById(R.id.nameTextView);
            status = itemView.findViewById(R.id.statusViewText);
            numRoom = itemView.findViewById(R.id.numRoomTextVIew);
            featuredImage = itemView.findViewById(R.id.featureImage);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            PropertyInfo c = listingsByCategoryList.get(getAdapterPosition());
            Intent i = new Intent(v.getContext(), ListingDetailsActivity.class);
            i.putExtra("listingKey", c.getKey());
            context.startActivity(i);
        }
    }




}
