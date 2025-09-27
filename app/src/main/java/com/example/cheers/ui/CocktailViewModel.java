package com.example.cheers.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cheers.model.Drink;
import com.example.cheers.model.DrinksResponse;
import com.example.cheers.network.CocktailRepository;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CocktailViewModel extends AndroidViewModel {
    private MutableLiveData<List<Drink>> cocktails = new MutableLiveData<>();
    private CocktailRepository repository;

    public CocktailViewModel(@NonNull Application application) {
        super(application);
        repository = new CocktailRepository();
    }

    public LiveData<List<Drink>> getCocktails() {
        return cocktails;
    }

    public void loadPopularDrinks() {
        repository.getPopularDrinks().enqueue(new Callback<DrinksResponse>() {
            @Override
            public void onResponse(Call<DrinksResponse> call, Response<DrinksResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Drink> drinks = response.body().getDrinks();
                    for (Drink drink : drinks) {
                        drink.buildIngredientsList();
                    }
                    cocktails.postValue(drinks);
                }
            }

            @Override
            public void onFailure(Call<DrinksResponse> call, Throwable t) {
                cocktails.postValue(Collections.emptyList());
            }
        });
    }
}
