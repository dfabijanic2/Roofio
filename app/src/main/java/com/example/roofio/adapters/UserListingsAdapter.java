package com.example.roofio.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roofio.ListingDetailsActivity;
import com.example.roofio.NewListingActivity;
import com.example.roofio.R;
import com.example.roofio.UserListingsActivity;
import com.example.roofio.models.PropertyInfo;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserListingsAdapter extends RecyclerView.Adapter<UserListingsAdapter.UserListingsViewHolder> {

    Context context;
    List<PropertyInfo> userListingsList;

    public UserListingsAdapter(Context context, List<PropertyInfo> userListingsList) {
        this.context = context;
        this.userListingsList = userListingsList;
    }

    public void setListings(List<PropertyInfo> listings){
        userListingsList = listings;
    }

    @NonNull
    @Override
    public UserListingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.user_listings_row, parent, false);

       return new UserListingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListingsViewHolder holder, int position) {
        holder.location.setText(userListingsList.get(position).getLokacija());
        holder.price.setText(userListingsList.get(position).getCijena().toString());
        holder.name.setText(userListingsList.get(position).getNaziv());
        holder.status.setText(userListingsList.get(position).getStatus().toString());
        holder.numRoom.setText(userListingsList.get(position).getBrojSoba().toString());
        Glide.with(context).load(userListingsList.get(position).getSlika()).into(holder.featuredImage);
    }


    @Override
    public int getItemCount() {
        return userListingsList.size();
    }

    public class UserListingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView location, price, name, status, numRoom;
        ImageView featuredImage;
        ImageButton btnDelete, btnUpdate;

        public UserListingsViewHolder(@NonNull View itemView) {
            super(itemView);

            location = itemView.findViewById(R.id.uaLocation);
            price = itemView.findViewById(R.id.uaPrice);
            name = itemView.findViewById(R.id.uaName);
            status = itemView.findViewById(R.id.uaStatus);
            numRoom = itemView.findViewById(R.id.uaNumRoom);
            featuredImage = itemView.findViewById(R.id.uaImage);
            btnDelete = itemView.findViewById(R.id.btnDeleteListing);
            btnUpdate = itemView.findViewById(R.id.btnUpdateListing);

            itemView.setOnClickListener(this);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = userListingsList.get(getAdapterPosition()).getKey();
                    FirebaseDatabase.getInstance().getReference().child("Nekretnine").child(key).removeValue();
                }
            });

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = userListingsList.get(getAdapterPosition()).getKey();
                    Intent i = new Intent(context, NewListingActivity.class);
                    i.putExtra("listingKey", key);
                    context.startActivity(i);
                }
            });
        }

        @Override
        public void onClick(View v) {
            PropertyInfo c = userListingsList.get(getAdapterPosition());
            Intent i = new Intent(v.getContext(), ListingDetailsActivity.class);
            i.putExtra("listingKey", c.getKey());
            context.startActivity(i);
        }
    }




}
