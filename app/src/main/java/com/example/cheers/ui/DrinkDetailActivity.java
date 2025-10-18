package com.example.cheers.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.cheers.R;
import com.example.cheers.databinding.ActivityDrinkDetailBinding;
import com.example.cheers.model.Drink;
import com.example.cheers.model.DrinkCreation;
import com.example.cheers.model.DrinksResponse;
import com.example.cheers.network.CocktailRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkDetailActivity extends AppCompatActivity {

    public static final String EXTRA_DRINK = "extra_drink";
    private DrinkDetailViewModel viewModel;

    private ActivityDrinkDetailBinding binding;
    private CocktailRepository repository;
    private Drink currentDrink;

    // Launcher para iniciar a CameraActivity e esperar o resultado
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null && currentDrink != null) {
                        // Salva a nova criação no banco de dados
                        DrinkCreation creation = new DrinkCreation(
                                currentDrink.getId(),
                                currentDrink.getName(),
                                imageUri.toString(),
                                System.currentTimeMillis()
                        );

                        viewModel.addCreation(creation);
                        Toast.makeText(this, "Sua criação foi salva!", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDrinkDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(DrinkDetailViewModel.class);
        repository = new CocktailRepository();
        currentDrink = (Drink) getIntent().getSerializableExtra(EXTRA_DRINK);

        if (currentDrink == null) {
            Toast.makeText(this, "Erro ao carregar o drink.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Verifica se os dados estão completos
        if (currentDrink.getInstructions() == null || currentDrink.getInstructions().isEmpty()) {
            loadDrinkDetailsFromApi(currentDrink.getId());
        } else {
            displayDrinkDetails(currentDrink);
        }

        observeFavoriteState();
        binding.buttonFavorite.setOnClickListener(v -> {
            if (currentDrink != null) {
                viewModel.toggleFavorite(currentDrink);
            }
        });

        binding.buttonAddCreation.setOnClickListener(v -> {
            Intent intent = new Intent(this, CameraActivity.class);
            cameraLauncher.launch(intent);
        });
    }

    private void loadDrinkDetailsFromApi(String id) {
        showLoading(true);

        repository.getDrinkById(id).enqueue(new Callback<DrinksResponse>() {
            @Override
            public void onResponse(Call<DrinksResponse> call, Response<DrinksResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null && !response.body().getDrinks().isEmpty()) {
                    currentDrink = response.body().getDrinks().get(0);
                    currentDrink.buildIngredientsList();
                    displayDrinkDetails(currentDrink);
                } else {
                    Toast.makeText(DrinkDetailActivity.this, "Detalhes não encontrados.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<DrinksResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(DrinkDetailActivity.this, "Falha ao carregar detalhes.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDrinkDetails(Drink drink) {
        binding.textDrinkName.setText(drink.getName());
        binding.textDrinkCategory.setText(drink.getCategoria() != null ? drink.getCategoria() : "Categoria desconhecida");
        binding.textDrinkInstructions.setText(drink.getInstructions() != null ? drink.getInstructions() : "Sem instruções disponíveis");

        // Exibe ingredientes, se existirem
        if (drink.getIngredients() != null && !drink.getIngredients().isEmpty()) {
            StringBuilder ingredients = new StringBuilder();
            for (String ingredient : drink.getIngredients()) {
                ingredients.append("• ").append(ingredient).append("\n");
            }
            binding.textDrinkIngredients.setText(ingredients.toString().trim());
        } else {
            binding.textDrinkIngredients.setText("Ingredientes não disponíveis.");
        }

        // Exibe imagem
        Glide.with(this)
                .load(drink.getThumbnail())
                .placeholder(android.R.color.darker_gray)
                .into(binding.imageDrinkThumb);
    }

    private void observeFavoriteState() {
        viewModel.getAllFavoriteDrinks().observe(this, favoriteDrinks -> {
            if (favoriteDrinks == null || currentDrink == null) {
                return;
            }

            boolean isFavorite = false;
            for (Drink favDrink : favoriteDrinks) {
                if (favDrink.getId().equals(currentDrink.getId())) {
                    isFavorite = true;
                    break;
                }
            }

            if (isFavorite) {
                binding.buttonFavorite.setImageResource(R.drawable.ic_favorite_filled);
            } else {
                binding.buttonFavorite.setImageResource(R.drawable.ic_favorite_border);
            }
        });
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.contentLayout.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}
