package com.example.cheers.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "favorite_drinks")
public class Drink implements Serializable {

    @PrimaryKey
    @NonNull
    @SerializedName("idDrink")
    private String id;

    @SerializedName("strDrink")
    private String name;

    @SerializedName("strCategory")
    private String categoria;

    @SerializedName("strInstructions")
    private String instructions;

    @SerializedName("strDrinkThumb")
    private String thumbnail;

    private boolean isFavorite = false;

    // Campos brutos para os ingredientes da API
    @Ignore @SerializedName("strIngredient1") private String strIngredient1;
    @Ignore @SerializedName("strIngredient2") private String strIngredient2;
    @Ignore @SerializedName("strIngredient3") private String strIngredient3;
    @Ignore @SerializedName("strIngredient4") private String strIngredient4;
    @Ignore @SerializedName("strIngredient5") private String strIngredient5;
    @Ignore @SerializedName("strIngredient6") private String strIngredient6;
    @Ignore @SerializedName("strIngredient7") private String strIngredient7;
    @Ignore @SerializedName("strIngredient8") private String strIngredient8;
    @Ignore @SerializedName("strIngredient9") private String strIngredient9;
    @Ignore @SerializedName("strIngredient10") private String strIngredient10;
    @Ignore @SerializedName("strIngredient11") private String strIngredient11;
    @Ignore @SerializedName("strIngredient12") private String strIngredient12;
    @Ignore @SerializedName("strIngredient13") private String strIngredient13;
    @Ignore @SerializedName("strIngredient14") private String strIngredient14;
    @Ignore @SerializedName("strIngredient15") private String strIngredient15;
    private List<String> ingredients;

    public Drink() {
        ingredients = new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public List<String> getIngredients() { return ingredients; }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void buildIngredientsList() {
        ingredients.clear();
        if (strIngredient1 != null && !strIngredient1.isEmpty()) ingredients.add(strIngredient1);
        if (strIngredient2 != null && !strIngredient2.isEmpty()) ingredients.add(strIngredient2);
        if (strIngredient3 != null && !strIngredient3.isEmpty()) ingredients.add(strIngredient3);
        if (strIngredient4 != null && !strIngredient4.isEmpty()) ingredients.add(strIngredient4);
        if (strIngredient5 != null && !strIngredient5.isEmpty()) ingredients.add(strIngredient5);
        if (strIngredient6 != null && !strIngredient6.isEmpty()) ingredients.add(strIngredient6);
        if (strIngredient7 != null && !strIngredient7.isEmpty()) ingredients.add(strIngredient7);
        if (strIngredient8 != null && !strIngredient8.isEmpty()) ingredients.add(strIngredient8);
        if (strIngredient9 != null && !strIngredient9.isEmpty()) ingredients.add(strIngredient9);
        if (strIngredient10 != null && !strIngredient10.isEmpty()) ingredients.add(strIngredient10);
        if (strIngredient11 != null && !strIngredient11.isEmpty()) ingredients.add(strIngredient11);
        if (strIngredient12 != null && !strIngredient12.isEmpty()) ingredients.add(strIngredient12);
        if (strIngredient13 != null && !strIngredient13.isEmpty()) ingredients.add(strIngredient13);
        if (strIngredient14 != null && !strIngredient14.isEmpty()) ingredients.add(strIngredient14);
        if (strIngredient15 != null && !strIngredient15.isEmpty()) ingredients.add(strIngredient15);
    }
}
