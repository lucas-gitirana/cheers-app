package com.example.cheers.network;

import com.example.cheers.model.CategoriesResponse;
import com.example.cheers.model.DrinksResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CocktailApi {
    @GET("filter.php?c=Cocktail")
    Call<DrinksResponse> getPopularDrinks();

    @GET("list.php?c=list")
    Call<CategoriesResponse> getCategories();

    @GET("filter.php")
    Call<DrinksResponse> getDrinksByCategory(@Query("c") String category);

    @GET("search.php")
    Call<DrinksResponse> searchDrinks(@Query("s") String query);

    @GET("lookup.php")
    Call<DrinksResponse> getDrinkById(@Query("i") String id);

}
