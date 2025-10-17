package com.example.cheers.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheers.R;
import com.example.cheers.ui.FavoritesAdapter;
import com.example.cheers.ui.CocktailViewModel;
import com.example.cheers.ui.DrinkDetailActivity;

public class HomeFragment extends Fragment {

    private CocktailViewModel cocktailViewModel;
    private RecyclerView recyclerViewHome;
    private FavoritesAdapter drinkAdapter;
    private ProgressBar progressBarHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewHome = view.findViewById(R.id.recyclerViewHome);
        progressBarHome = view.findViewById(R.id.progressBarHome);

        setupRecyclerView();
        cocktailViewModel = new ViewModelProvider(this).get(CocktailViewModel.class);
        observeViewModel();

        drinkAdapter.setOnItemClickListener(drink -> {
            Intent intent = new Intent(getActivity(), DrinkDetailActivity.class);
            intent.putExtra(DrinkDetailActivity.EXTRA_DRINK, drink);
            startActivity(intent);
        });

        if (cocktailViewModel.getCocktails().getValue() == null) {
            progressBarHome.setVisibility(View.VISIBLE);
            cocktailViewModel.loadPopularDrinks();
        }
    }

    private void setupRecyclerView() {
        drinkAdapter = new FavoritesAdapter();
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewHome.setAdapter(drinkAdapter);
    }

    private void observeViewModel() {
        cocktailViewModel.getCocktails().observe(getViewLifecycleOwner(), drinks -> {
            progressBarHome.setVisibility(View.GONE);
            if (drinks != null && !drinks.isEmpty()) {
                recyclerViewHome.setVisibility(View.VISIBLE);
                drinkAdapter.setFavorites(drinks);
            } else {
                recyclerViewHome.setVisibility(View.GONE);
            }
        });
    }
}
