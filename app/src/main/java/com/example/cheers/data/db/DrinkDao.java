package com.example.cheers.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cheers.model.Drink;

import java.util.List;

@Dao
public interface DrinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Drink drink);

    @Query("SELECT * FROM favorite_drinks WHERE isFavorite = 1")
    LiveData<List<Drink>> getFavoriteDrinks();

    @Query("SELECT * FROM favorite_drinks WHERE id = :drinkId")
    LiveData<Drink> getDrinkById(String drinkId);

    @Query("UPDATE favorite_drinks SET isFavorite = :isFavorite WHERE id = :drinkId")
    void updateFavoriteStatus(String drinkId, boolean isFavorite);

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_drinks WHERE id = :drinkId LIMIT 1)")
    LiveData<Boolean> isFavorite(String drinkId);
}
