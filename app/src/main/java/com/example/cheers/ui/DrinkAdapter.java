package com.example.cheers.ui;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
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
import com.example.cheers.model.DrinksResponse;
import com.example.cheers.network.CocktailRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    private Context context;
    private List<Drink> drinks;
    private int layoutResId;
    private CocktailRepository repository;

    public DrinkAdapter(Context context, List<Drink> drinks, int layoutResId) {
        this.context = context;
        this.drinks = drinks;
        this.layoutResId = layoutResId;
        this.repository = new CocktailRepository();
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutResId, parent, false);
        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        Drink drink = drinks.get(position);

        holder.nameTextView.setText(drink.getName());
        Glide.with(context).load(drink.getThumbnail()).into(holder.thumbnailImageView);

        holder.itemView.setOnClickListener(v -> {
            if (drink.getInstructions() != null && !drink.getInstructions().isEmpty()) {
                openDrinkDetail(drink);
            } else {
                repository.getDrinkById(drink.getId()).enqueue(new Callback<DrinksResponse>() {
                    @Override
                    public void onResponse(Call<DrinksResponse> call, Response<DrinksResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Drink fullDrink = response.body().getDrinks().get(0);
                            fullDrink.buildIngredientsList();
                            openDrinkDetail(fullDrink);
                        }
                    }

                    @Override
                    public void onFailure(Call<DrinksResponse> call, Throwable t) {
                        Toast.makeText(context, "Erro ao carregar detalhes", Toast.LENGTH_SHORT).show();
                    }
                });
            }
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

    private void openDrinkDetail(Drink drink) {
        Intent intent = new Intent(context, DrinkDetailActivity.class);
        intent.putExtra(DrinkDetailActivity.EXTRA_DRINK, drink);
        context.startActivity(intent);
    }

    static class DrinkViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView thumbnailImageView;

        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cocktailName);
            thumbnailImageView = itemView.findViewById(R.id.cocktailImage);
        }
    }
}
