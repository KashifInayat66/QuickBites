package com.example.quickbites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickbites.adapters.RecipeAdapter;
import com.example.quickbites.api.ApiHelper;
import com.example.quickbites.api.MealApiResponse;
import com.example.quickbites.api.RetrofitClient;
import com.example.quickbites.database.DatabaseHelper;
import com.example.quickbites.models.Recipe;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewRecipes;
    private RecipeAdapter recipeAdapter;
    private DatabaseHelper databaseHelper;
    private TextView tvNoResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        initViews();
        setupRecyclerView();
        
        // get search parameters from intent
        Intent intent = getIntent();
        String ingredientsStr = intent.getStringExtra("ingredients");
        String moodType = intent.getStringExtra("moodType");
        
        // parse ingredients
        List<String> ingredients = new ArrayList<>();
        if (ingredientsStr != null && !ingredientsStr.trim().isEmpty()) {
            String[] ingredientArray = ingredientsStr.split(",");
            for (String ingredient : ingredientArray) {
                ingredients.add(ingredient.trim().toLowerCase());
            }
        }
        
        // search for recipes
        searchRecipes(ingredients, moodType);
        
        // set title
        TextView tvResultsTitle = findViewById(R.id.tvResultsTitle);
        updateResultsTitle(tvResultsTitle, moodType, ingredients.size());
    }

    private void initViews() {
        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        tvNoResults = findViewById(R.id.tvNoResults);
        Button btnBack = findViewById(R.id.btnBack);
        databaseHelper = new DatabaseHelper(this);
        
        // set up back button click listener
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        recipeAdapter = new RecipeAdapter(new ArrayList<>(), new RecipeAdapter.OnRecipeClickListener() {
            @Override
            public void onRecipeClick(Recipe recipe) {
                openRecipeDetails(recipe);
            }

            @Override
            public void onFavoriteClick(Recipe recipe, int position) {
                toggleFavorite(recipe, position);
            }
        });
        
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecipes.setAdapter(recipeAdapter);
    }

    private void searchRecipes(List<String> ingredients, String moodType) {
        // todo: add loading spinner here
        tvNoResults.setText("loading recipes...");
        tvNoResults.setVisibility(View.VISIBLE);
        recyclerViewRecipes.setVisibility(View.GONE);
        
        // if user typed ingredients, search by first ingredient
        if (!ingredients.isEmpty()) {
            String firstIngredient = ingredients.get(0);
            searchByIngredient(firstIngredient, moodType);
        } else {
            // no ingredients, just get random meals
            getRandomMeals(moodType);
        }
    }
    
    // search api by ingredient
    private void searchByIngredient(String ingredient, String moodType) {
        RetrofitClient.getApiService().searchByName(ingredient).enqueue(new Callback<MealApiResponse>() {
            @Override
            public void onResponse(Call<MealApiResponse> call, Response<MealApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MealApiResponse.MealItem> meals = response.body().getMeals();
                    if (meals != null && !meals.isEmpty()) {
                        // convert api meals to recipes
                        List<Recipe> recipes = new ArrayList<>();
                        for (MealApiResponse.MealItem meal : meals) {
                            Recipe recipe = ApiHelper.convertMealToRecipe(meal);
                            // check if its favorited
                            recipe.setFavorite(databaseHelper.isFavorite(recipe.getId()));
                            // filter by mood if not mix
                            if (moodType.equals("mix") || recipe.getMoodType().equals(moodType)) {
                                recipes.add(recipe);
                            }
                        }
                        displayRecipes(recipes);
                    } else {
                        showNoResults();
                    }
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<MealApiResponse> call, Throwable t) {
                showError();
            }
        });
    }
    
    // get random meals when no ingredient specified
    private void getRandomMeals(String moodType) {
        // call api multiple times to get several random meals
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            RetrofitClient.getApiService().getRandomMeal().enqueue(new Callback<MealApiResponse>() {
                @Override
                public void onResponse(Call<MealApiResponse> call, Response<MealApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<MealApiResponse.MealItem> meals = response.body().getMeals();
                        if (meals != null && !meals.isEmpty()) {
                            Recipe recipe = ApiHelper.convertMealToRecipe(meals.get(0));
                            recipe.setFavorite(databaseHelper.isFavorite(recipe.getId()));
                            // filter by mood
                            if (moodType.equals("mix") || recipe.getMoodType().equals(moodType)) {
                                recipes.add(recipe);
                                displayRecipes(recipes);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<MealApiResponse> call, Throwable t) {
                    if (recipes.isEmpty()) {
                        showError();
                    }
                }
            });
        }
    }
    
    // show recipes in recyclerview
    private void displayRecipes(List<Recipe> recipes) {
        tvNoResults.setVisibility(View.GONE);
        recyclerViewRecipes.setVisibility(View.VISIBLE);
        recipeAdapter.updateRecipes(recipes);
    }
    
    // show no results message
    private void showNoResults() {
        tvNoResults.setText("no recipes found. try different ingredients!");
        tvNoResults.setVisibility(View.VISIBLE);
        recyclerViewRecipes.setVisibility(View.GONE);
    }
    
    // show error message
    private void showError() {
        tvNoResults.setText("couldnt load recipes. check your internet connection!");
        tvNoResults.setVisibility(View.VISIBLE);
        recyclerViewRecipes.setVisibility(View.GONE);
        Toast.makeText(this, "network error - check internet", Toast.LENGTH_SHORT).show();
    }

    private void toggleFavorite(Recipe recipe, int position) {
        if (recipe.isFavorite()) {
            databaseHelper.removeFromFavorites(recipe.getId());
            recipe.setFavorite(false);
        } else {
            databaseHelper.addToFavorites(recipe.getId());
            recipe.setFavorite(true);
        }
        recipeAdapter.notifyItemChanged(position);
    }

    private void updateResultsTitle(TextView tvResultsTitle, String moodType, int ingredientCount) {
        String title;
        if (ingredientCount > 0) {
            title = getString(R.string.meal_ideas_with_ingredients, moodType);
        } else {
            title = getString(R.string.meal_ideas_for_mood, moodType);
        }
        tvResultsTitle.setText(title);
    }

    private void openRecipeDetails(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipe_id", recipe.getId());
        intent.putExtra("recipe_name", recipe.getName());
        intent.putExtra("recipe_description", recipe.getDescription());
        
        // convert ingredients list to string
        StringBuilder ingredientsStr = new StringBuilder();
        if (recipe.getIngredients() != null) {
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                ingredientsStr.append(recipe.getIngredients().get(i));
                if (i < recipe.getIngredients().size() - 1) {
                    ingredientsStr.append(",");
                }
            }
        }
        
        intent.putExtra("recipe_ingredients", ingredientsStr.toString());
        intent.putExtra("recipe_mood_type", recipe.getMoodType());
        intent.putExtra("recipe_cooking_time", recipe.getCookingTime());
        intent.putExtra("recipe_instructions", recipe.getInstructions());
        intent.putExtra("recipe_cooking_method", recipe.getCookingMethod());
        
        startActivity(intent);
    }
}
