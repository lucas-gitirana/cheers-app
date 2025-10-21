package com.example.cheers.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheers.R;
import com.example.cheers.model.Drink;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private List<Drink> favoriteDrinks = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_drink, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Drink currentDrink = favoriteDrinks.get(position);
        holder.bind(currentDrink);
    }

    @Override
    public int getItemCount() {
        return favoriteDrinks.size();
    }

    public void setFavorites(List<Drink> favorites) {
        this.favoriteDrinks = favorites;
        notifyDataSetChanged();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewFavorite;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFavorite = itemView.findViewById(R.id.imageViewFavoriteDrink);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(favoriteDrinks.get(position));
                }
            });
        }

        public void bind(Drink drink) {
            Glide.with(itemView.getContext())
                    .load(drink.getThumbnail())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageViewFavorite);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Drink drink);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}