package com.example.quickbites.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// api service for themealdb
public interface MealApiService {
    
    // search meals by main ingredient
    // example: www.themealdb.com/api/json/v1/1/filter.php?i=chicken
    @GET("filter.php")
    Call<MealApiResponse> searchByIngredient(@Query("i") String ingredient);
    
    // get meal details by id
    // example: www.themealdb.com/api/json/v1/1/lookup.php?i=52772
    @GET("lookup.php")
    Call<MealApiResponse> getMealById(@Query("i") String mealId);
    
    // search meals by name
    // example: www.themealdb.com/api/json/v1/1/search.php?s=chicken
    @GET("search.php")
    Call<MealApiResponse> searchByName(@Query("s") String mealName);
    
    // get random meal
    // example: www.themealdb.com/api/json/v1/1/random.php
    @GET("random.php")
    Call<MealApiResponse> getRandomMeal();
}
