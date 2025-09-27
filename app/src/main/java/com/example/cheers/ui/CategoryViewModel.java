package com.example.cheers.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cheers.model.CategoriesResponse;
import com.example.cheers.model.Category;
import com.example.cheers.network.CocktailRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends AndroidViewModel {
    private MutableLiveData<List<Category>> categoriesLiveData = new MutableLiveData<>();
    private CocktailRepository repository;

    public CategoryViewModel(@NonNull Application app) {
        super(app);
        repository = new CocktailRepository();
    }

    public LiveData<List<Category>> getCategoriesLiveData() { return categoriesLiveData; }

    public void loadCategories() {
        repository.getCategories().enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoriesLiveData.postValue(response.body().getCategories());
                }
            }
            @Override public void onFailure(Call<CategoriesResponse> call, Throwable t) { }
        });
    }
}


