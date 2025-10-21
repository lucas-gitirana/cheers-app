package com.example.cheers.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheers.R;
import com.example.cheers.ui.FavoritesAdapter;
import com.example.cheers.ui.DrinkDetailActivity;
import com.example.cheers.ui.SearchViewModel;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private RecyclerView recyclerViewSearch;
    private FavoritesAdapter searchAdapter;
    private SearchView searchView;
    private TextView textViewNoResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch);
        searchView = view.findViewById(R.id.searchView);
        textViewNoResults = view.findViewById(R.id.textViewNoResults);

        setupRecyclerView();
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        observeViewModel();
        setupSearchView();

        searchAdapter.setOnItemClickListener(drink -> {
            Intent intent = new Intent(getActivity(), DrinkDetailActivity.class);
            intent.putExtra(DrinkDetailActivity.EXTRA_DRINK, drink);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        searchAdapter = new FavoritesAdapter();
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(searchAdapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewModel.searchDrinks(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    searchViewModel.searchDrinks("");
                }
                return false;
            }
        });
    }

    private void observeViewModel() {
        searchViewModel.getSearchResults().observe(getViewLifecycleOwner(), drinks -> {
            if (drinks != null && !drinks.isEmpty()) {
                recyclerViewSearch.setVisibility(View.VISIBLE);
                textViewNoResults.setVisibility(View.GONE);
                searchAdapter.setFavorites(drinks);
            } else {
                recyclerViewSearch.setVisibility(View.GONE);

                if (searchView.getQuery().length() > 0) {
                    textViewNoResults.setVisibility(View.VISIBLE);
                } else {
                    textViewNoResults.setVisibility(View.GONE);
                }

                if (drinks == null) {
                    searchAdapter.setFavorites(new java.util.ArrayList<>());
                }
            }
        });
    }
}
