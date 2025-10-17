package com.example.cheers.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cheers.data.db.AppDatabase;
import com.example.cheers.data.db.DrinkDao;
import com.example.cheers.model.Drink;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private final DrinkDao drinkDao;
    private final LiveData<List<Drink>> favoriteDrinks;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getDatabase(application);
        drinkDao = database.drinkDao();
        favoriteDrinks = drinkDao.getFavoriteDrinks();
    }

    public LiveData<List<Drink>> getFavoriteDrinks() {
        return favoriteDrinks;
    }
}
