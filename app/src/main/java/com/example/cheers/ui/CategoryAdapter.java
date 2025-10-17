package com.example.cheers.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheers.R;
import com.example.cheers.model.Category;
import com.example.cheers.model.Drink;
import com.example.cheers.model.DrinksResponse;
import com.example.cheers.network.CocktailRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private Context context;
    private CocktailRepository repository;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
        this.repository = new CocktailRepository();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.title.setText(category.getName());

        List<Drink> initialDrinks = category.getDrinks() != null ? category.getDrinks() : new ArrayList<>();

        DrinkAdapter drinkAdapter = new DrinkAdapter(context, category.getDrinks(), R.layout.item_category_drink);
        holder.recyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        );
        holder.recyclerView.setAdapter(drinkAdapter);

        // Se ainda não houver drinks carregados, buscar da API
        if (category.getDrinks() == null) {
            repository.getDrinksByCategory(category.getName()).enqueue(new Callback<DrinksResponse>() {
                @Override
                public void onResponse(Call<DrinksResponse> call, Response<DrinksResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Drink> drinks = response.body().getDrinks();
                        category.setDrinks(drinks);
                        drinkAdapter.setDrinks(drinks); // Atualiza a lista, listener já existe
                        drinkAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<DrinksResponse> call, Throwable t) {
                    Toast.makeText(context, "Erro ao carregar drinks da categoria", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openDrinkDetail(Drink drink) {
        Intent intent = new Intent(context, DrinkDetailActivity.class);
        intent.putExtra(DrinkDetailActivity.EXTRA_DRINK, drink); // Drink deve ser Serializable ou Parcelable
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView recyclerView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.categoryTitle);
            recyclerView = itemView.findViewById(R.id.recyclerViewDrinks);
        }
    }
}
