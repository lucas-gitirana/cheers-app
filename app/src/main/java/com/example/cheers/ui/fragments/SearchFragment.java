package com.example.cheers.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView; // Importante usar o SearchView do androidx

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheers.R;
import com.example.cheers.ui.FavoritesAdapter; // Reutilizando o adapter
import com.example.cheers.ui.DrinkDetailActivity;
import com.example.cheers.ui.SearchViewModel;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private RecyclerView recyclerViewSearch;
    private FavoritesAdapter searchAdapter; // Reutilizando o adapter
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

        // Inicializa as Views
        recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch);
        searchView = view.findViewById(R.id.searchView);
        textViewNoResults = view.findViewById(R.id.textViewNoResults);

        // Configura o Adapter e o RecyclerView
        setupRecyclerView();

        // Inicializa o ViewModel
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // Observa os resultados da busca
        observeViewModel();

        // Configura o listener do SearchView para reagir à digitação do usuário
        setupSearchView();

        // Configura o clique nos itens do resultado
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
                // Inicia a busca quando o usuário pressiona "enter"
                searchViewModel.searchDrinks(query);
                searchView.clearFocus(); // Esconde o teclado
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    searchViewModel.searchDrinks(""); // Limpa os resultados
                }
                return false;
            }
        });
    }

    private void observeViewModel() {
        searchViewModel.getSearchResults().observe(getViewLifecycleOwner(), drinks -> {
            if (drinks != null && !drinks.isEmpty()) {
                // Temos resultados, exibe a lista
                recyclerViewSearch.setVisibility(View.VISIBLE);
                textViewNoResults.setVisibility(View.GONE);
                searchAdapter.setFavorites(drinks); // O método do adapter é genérico o suficiente
            } else {
                // Não temos resultados (lista nula ou vazia)
                recyclerViewSearch.setVisibility(View.GONE);
                // Mostra a mensagem apenas se o usuário já tiver pesquisado algo
                if (searchView.getQuery().length() > 0) {
                    textViewNoResults.setVisibility(View.VISIBLE);
                } else {
                    textViewNoResults.setVisibility(View.GONE);
                }
                // Limpa a lista no adapter caso ela seja nula
                if (drinks == null) {
                    searchAdapter.setFavorites(new java.util.ArrayList<>());
                }
            }
        });
    }
}
