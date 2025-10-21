package com.example.cheers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheers.R;
import com.example.cheers.model.Drink;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private FavoritesViewModel viewModel;
    private FavoritesAdapter favoritesAdapter;
    private RecyclerView recyclerViewFavorites;
    private TextView textViewNoFavorites;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Inicializar Views
        toolbar = findViewById(R.id.toolbarFavorites);
        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites);
        textViewNoFavorites = findViewById(R.id.textViewNoFavorites);
        toolbar.setNavigationOnClickListener(v -> finish());

        setupRecyclerView();

        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        viewModel.getFavoriteDrinks().observe(this, favorites -> {
            updateUI(favorites);
        });

        favoritesAdapter.setOnItemClickListener(drink -> {
            Intent intent = new Intent(FavoritesActivity.this, DrinkDetailActivity.class);
            intent.putExtra(DrinkDetailActivity.EXTRA_DRINK, drink);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        favoritesAdapter = new FavoritesAdapter();
        recyclerViewFavorites.setAdapter(favoritesAdapter);
    }

    private void updateUI(List<Drink> favorites) {
        if (favorites != null && !favorites.isEmpty()) {
            recyclerViewFavorites.setVisibility(View.VISIBLE);
            textViewNoFavorites.setVisibility(View.GONE);
            favoritesAdapter.setFavorites(favorites);
        } else {
            recyclerViewFavorites.setVisibility(View.GONE);
            textViewNoFavorites.setVisibility(View.VISIBLE);
        }
    }
}