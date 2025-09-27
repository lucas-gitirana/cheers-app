package com.example.cheers.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesResponse {
    @SerializedName("drinks")
    private List<Category> categories;
    public List<Category> getCategories() { return categories; }
}

