package com.example.cheers.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cheers.data.db.AppDatabase;
import com.example.cheers.data.db.DrinkCreationDao;
import com.example.cheers.model.DrinkCreation;
import com.example.cheers.network.CocktailRepository;

import java.util.List;

public class CreationsViewModel extends AndroidViewModel {
    private DrinkCreationDao drinkCreationDao;
    private CocktailRepository repository;
    private LiveData<List<DrinkCreation>> allCreations;

    public CreationsViewModel(@NonNull Application application) {
        super(application);
        drinkCreationDao = AppDatabase.getDatabase(application).drinkCreationDao();
        allCreations = drinkCreationDao.getAllCreations();
    }

    public LiveData<List<DrinkCreation>> getAllCreations() {
        return drinkCreationDao.getAllCreations();
    }
}
