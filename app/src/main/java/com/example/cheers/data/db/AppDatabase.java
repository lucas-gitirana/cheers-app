package com.example.cheers.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cheers.data.db.converter.Converters;
import com.example.cheers.model.Drink;
import com.example.cheers.model.DrinkCreation;

@Database(entities = {Drink.class, DrinkCreation.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract DrinkDao drinkDao();
    public abstract DrinkCreationDao drinkCreationDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "cheers_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
