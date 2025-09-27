package com.example.cheers.network;

import com.example.cheers.model.CategoriesResponse;
import com.example.cheers.model.DrinksResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CocktailRepository {
    private static final String BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/";
    private CocktailApi api;

    public CocktailRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(CocktailApi.class);
    }

    public Call<DrinksResponse> getPopularDrinks() {
        return api.getPopularDrinks();
    }

    public Call<CategoriesResponse> getCategories() { return api.getCategories(); }
    public Call<DrinksResponse> getDrinksByCategory(String category) {
        return api.getDrinksByCategory(category);
    }

    public Call<DrinksResponse> searchDrinks(String query) {
        return api.searchDrinks(query);
    }

    public Call<DrinksResponse> getDrinkById(String id) {
        return api.getDrinkById(id);
    }

}
