package com.example.cheers.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DrinksResponse {
    @SerializedName("drinks")
    private List<Drink> drinks;

    public List<Drink> getDrinks() {
        return drinks;
    }
}
