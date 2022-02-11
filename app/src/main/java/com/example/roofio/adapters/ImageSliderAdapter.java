package com.example.roofio.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roofio.ListingDetailsActivity;
import com.example.roofio.R;
import com.example.roofio.models.Image;
import com.example.roofio.models.PropertyInfo;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder> {

    Context context;
    List<Image> imageList;

    public ImageSliderAdapter(Context context, List<Image> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    public void setImages(List<Image> images){
        imageList = images;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.image_row, parent, false);

       return new ImageSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        Glide.with(context).load(imageList.get(position).getImageUri()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageSliderViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        Button btnRemove;

        public ImageSliderViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.listingImage);
            btnRemove = itemView.findViewById(R.id.btnRemoveImage);

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }

    }


}
