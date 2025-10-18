package com.example.cheers.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "drink_creations")
public class DrinkCreation {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String drinkId;
    public String drinkName;
    public String imageUri;
    public long timestamp;

    public DrinkCreation(String drinkId, String drinkName, String imageUri, long timestamp) {
        this.drinkId = drinkId;
        this.drinkName = drinkName;
        this.imageUri = imageUri;
        this.timestamp = timestamp;
    }
}
