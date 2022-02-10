package com.example.roofio.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.roofio.R;

import java.util.List;
import java.util.Objects;

public class ListingDetailsAdapter extends RecyclerView.Adapter<ListingDetailsAdapter.SliderViewHolder>   {

    private List<String> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;

    public ListingDetailsAdapter(Context context, List<String> sliderItems) {
        this.context = context;
        this.sliderItems = sliderItems;
//        this.viewPager2 = viewPager2;
    }

    public void setImages(List<String> images){ this.sliderItems = images;}

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_item, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Glide.with(context).load(sliderItems.get(position).toString()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImage);
        }
    }
}
