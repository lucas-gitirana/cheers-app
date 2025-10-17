package com.example.cheers.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cheers.model.Drink;
import com.example.cheers.model.DrinksResponse;
import com.example.cheers.network.CocktailRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {
    private MutableLiveData<List<Drink>> searchResults = new MutableLiveData<>();
    private CocktailRepository repository;

    public SearchViewModel(@NonNull Application app) {
        super(app);
        repository = new CocktailRepository();
    }

    public LiveData<List<Drink>> getSearchResults() { return searchResults; }

    public void searchDrinks(String query) {
        if (query == null || query.trim().isEmpty()) {
            searchResults.postValue(new ArrayList<>()); // limpa lista
            return;
        }

        repository.searchDrinks(query).enqueue(new Callback<DrinksResponse>() {
            @Override
            public void onResponse(Call<DrinksResponse> call, Response<DrinksResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Drink> drinks = response.body().getDrinks();

                    if (drinks == null) {
                        searchResults.postValue(new ArrayList<>());
                        return;
                    }

                    for (Drink drink : drinks) {
                        drink.buildIngredientsList();
                    }
                    searchResults.postValue(drinks);
                } else {
                    searchResults.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<DrinksResponse> call, Throwable t) {
                searchResults.postValue(new ArrayList<>());
            }
        });
    }
}

