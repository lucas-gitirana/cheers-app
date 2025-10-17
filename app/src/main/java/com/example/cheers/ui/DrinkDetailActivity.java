package com.example.cheers.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.cheers.R;
import com.example.cheers.model.Drink;

public class DrinkDetailActivity extends AppCompatActivity {

    public static final String EXTRA_DRINK = "extra_drink";

    private ImageView imageViewDrink;
    private TextView textViewName, textViewInstructions, textViewIngredients;
    private Button buttonFavorite;
    private DrinkDetailViewModel viewModel;
    private Drink currentDrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drink_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageViewDrink = findViewById(R.id.imageViewDrink);
        textViewName = findViewById(R.id.textViewDrinkName);
        textViewInstructions = findViewById(R.id.textViewInstructions);
        textViewIngredients = findViewById(R.id.textViewIngredients);
        buttonFavorite = findViewById(R.id.buttonFavorite);

        Drink drink = (Drink) getIntent().getSerializableExtra(EXTRA_DRINK);
        if (drink != null) {
            textViewName.setText(drink.getName());
            textViewInstructions.setText(drink.getInstructions());

            StringBuilder ingredients = new StringBuilder();
            for (int i = 0; i < drink.getIngredients().size(); i++) {
                ingredients.append(drink.getIngredients().get(i))
                        .append("\n");
            }
            textViewIngredients.setText(ingredients.toString());

            Glide.with(this)
                    .load(drink.getThumbnail())
                    .into(imageViewDrink);
        }

        // Inicializa o ViewModel
        viewModel = new ViewModelProvider(this).get(DrinkDetailViewModel.class);

        Drink drinkFromIntent = (Drink) getIntent().getSerializableExtra(EXTRA_DRINK);

        if (drinkFromIntent != null) {
            // Observa o drink do banco de dados para obter o estado de favorito atualizado
            viewModel.getDrinkById(drinkFromIntent.getId()).observe(this, drinkFromDb -> {
                // Se o drink não está no banco, usamos o que veio da Intent
                currentDrink = (drinkFromDb != null) ? drinkFromDb : drinkFromIntent;
                updateUI(currentDrink);
            });
        }

        buttonFavorite.setOnClickListener(v -> {
            if (currentDrink != null) {
                viewModel.toggleFavorite(currentDrink);
            }
        });
    }

    private void updateUI(Drink drink) {
        if (drink == null) return;

        textViewName.setText(drink.getName());
        textViewInstructions.setText(drink.getInstructions());

        // ... seu código para ingredientes e Glide

        // Atualiza a aparência do botão de favorito
        if (drink.isFavorite()) {
            buttonFavorite.setText("Remover dos Favoritos");
            // Opcional: mude o ícone ou a cor
            // buttonFavorite.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_filled));
        } else {
            buttonFavorite.setText("Adicionar aos Favoritos");
            // Opcional: mude o ícone ou a cor
            // buttonFavorite.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border));
        }
    }
}