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

        // Configurar a Toolbar
        toolbar.setNavigationOnClickListener(v -> finish()); // Ação de voltar

        // Configurar RecyclerView e Adapter
        setupRecyclerView();

        // Obter o ViewModel
        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        // Observar a lista de favoritos
        viewModel.getFavoriteDrinks().observe(this, favorites -> {
            updateUI(favorites);
        });

        // Configurar o clique em um item favorito para abrir os detalhes
        favoritesAdapter.setOnItemClickListener(drink -> {
            Intent intent = new Intent(FavoritesActivity.this, DrinkDetailActivity.class);
            // É crucial passar o objeto Drink para a próxima tela
            intent.putExtra(DrinkDetailActivity.EXTRA_DRINK, drink);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        favoritesAdapter = new FavoritesAdapter();
        recyclerViewFavorites.setAdapter(favoritesAdapter);
        // O LayoutManager (GridLayoutManager) já foi definido no XML.
    }

    private void updateUI(List<Drink> favorites) {
        if (favorites != null && !favorites.isEmpty()) {
            // Use a classe View padrão do Android
            recyclerViewFavorites.setVisibility(View.VISIBLE);
            textViewNoFavorites.setVisibility(View.GONE);
            favoritesAdapter.setFavorites(favorites);
        } else {
            // Se não houver favoritos, mostra a mensagem e esconde a lista.
            recyclerViewFavorites.setVisibility(View.GONE);
            textViewNoFavorites.setVisibility(View.VISIBLE);
        }
    }
}