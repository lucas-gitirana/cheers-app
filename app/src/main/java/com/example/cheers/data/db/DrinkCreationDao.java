package com.example.cheers.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cheers.model.DrinkCreation;

import java.util.List;

@Dao
public interface DrinkCreationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DrinkCreation creation);

    @Query("SELECT * FROM drink_creations ORDER BY timestamp DESC")
    LiveData<List<DrinkCreation>>  getAllCreations();
}
