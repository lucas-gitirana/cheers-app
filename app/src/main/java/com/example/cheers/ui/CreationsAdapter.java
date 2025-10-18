package com.example.cheers.ui;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheers.R;
import com.example.cheers.model.DrinkCreation;

import java.util.ArrayList;
import java.util.List;

public class CreationsAdapter extends RecyclerView.Adapter<CreationsAdapter.CreationViewHolder> {

    private List<DrinkCreation> creations = new ArrayList<>();

    @NonNull
    @Override
    public CreationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_creation, parent, false);
        return new CreationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreationViewHolder holder, int position) {
        DrinkCreation currentCreation = creations.get(position);
        holder.textDrinkName.setText(currentCreation.drinkName);
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(currentCreation.imageUri))
                .into(holder.imageCreation);
    }

    @Override
    public int getItemCount() {
        return creations.size();
    }

    public void setCreations(List<DrinkCreation> creations) {
        this.creations = creations;
        notifyDataSetChanged();
    }

    static class CreationViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCreation;
        TextView textDrinkName;

        public CreationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCreation = itemView.findViewById(R.id.imageCreation);
            textDrinkName = itemView.findViewById(R.id.textCreationDrinkName);
        }
    }
}
