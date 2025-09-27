package com.example.cheers.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {
    @SerializedName("strCategory")
    private String name;
    private List<Drink> drinks;

    public String getName() { return name; }
    public List<Drink> getDrinks() { return drinks; }
    public void setDrinks(List<Drink> drinks) { this.drinks = drinks; }
}

