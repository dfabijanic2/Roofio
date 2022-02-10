package com.example.roofio.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roofio.ListingsByCategoryActivity;
import com.example.roofio.R;
import com.example.roofio.models.Category;

import java.net.URI;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }


    public void setCategories(List<Category> categories){
        this.categoryList = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_row, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Glide.with(context).load(categoryList.get(position).getImageUrl()).into(holder.categoryImage);
        holder.categoryName.setText(categoryList.get(position).getNaziv());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView categoryImage;
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Category c = categoryList.get(getAdapterPosition());
            Intent i = new Intent(v.getContext(), ListingsByCategoryActivity.class);
            i.putExtra("categoryKey", c.getKey());
            i.putExtra("categoryName", c.getNaziv());
            context.startActivity(i);
        }


    }
}
