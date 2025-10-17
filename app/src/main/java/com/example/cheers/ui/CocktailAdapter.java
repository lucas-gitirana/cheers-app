package com.example.cheers.ui;

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
import com.example.cheers.R;
import com.example.cheers.model.Drink;

import java.util.List;

public class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder> {
    private Context context;
    private List<Drink> drinks;

    public CocktailAdapter(Context context, List<Drink> drinks) {
        this.context = context;
        this.drinks = drinks;
    }

    @NonNull
    @Override
    public CocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cocktail, parent, false);
        return new CocktailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CocktailViewHolder holder, int position) {
        Drink drink = drinks.get(position);
        holder.nameTextView.setText(drink.getName());

        Glide.with(context)
                .load(drink.getThumbnail())
                .into(holder.thumbnailImageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DrinkDetailActivity.class);
            intent.putExtra(DrinkDetailActivity.EXTRA_DRINK, drink);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return drinks != null ? drinks.size() : 0;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
        notifyDataSetChanged();
    }

    static class CocktailViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView thumbnailImageView;

        public CocktailViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cocktailName);
            thumbnailImageView = itemView.findViewById(R.id.cocktailImage);
        }
    }
}
