package com.example.cheers.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cheers.data.db.AppDatabase;
import com.example.cheers.data.db.DrinkDao;
import com.example.cheers.model.Drink;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DrinkDetailViewModel extends AndroidViewModel {

    private DrinkDao drinkDao;
    private ExecutorService executorService;

    public DrinkDetailViewModel(Application application) {
        super(application);
        drinkDao = AppDatabase.getDatabase(application).drinkDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Drink> getDrinkById(String drinkId) {
        return drinkDao.getDrinkById(drinkId);
    }

    public LiveData<Boolean> isFavorite(String drinkId) {
        return drinkDao.isFavorite(drinkId);
    }

    public void toggleFavorite(Drink drink) {
        executorService.execute(() -> {
            boolean isCurrentlyFavorite = drink.isFavorite();
            drink.setFavorite(!isCurrentlyFavorite);
            drinkDao.insertOrUpdate(drink);
        });
    }
}