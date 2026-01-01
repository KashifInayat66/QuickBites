package com.example.quickbites;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFavorites;
    private RecipeAdapter recipeAdapter;
    private DatabaseHelper databaseHelper;
    private TextView tvNoFavorites;
    private EditText etSearch;
    private List<Recipe> allFavorites;
    private List<Recipe> filteredFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        allFavorites = new ArrayList<>();
        filteredFavorites = new ArrayList<>();

        initViews();
        setupRecyclerView();
        setupSearch();
        loadFavorites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // refresh favorites when returning to this activity
        loadFavorites();
    }

    private void initViews() {
        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites);
        tvNoFavorites = findViewById(R.id.tvNoFavorites);
        etSearch = findViewById(R.id.etSearch);
        Button btnBack = findViewById(R.id.btnBack);
        databaseHelper = new DatabaseHelper(this);
        
        // set up back button click listener
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFavorites(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filterFavorites(String query) {
        if (query.trim().isEmpty()) {
            filteredFavorites = new ArrayList<>(allFavorites);
        } else {
            filteredFavorites = new ArrayList<>();
            String lowerQuery = query.toLowerCase();
            
            for (Recipe recipe : allFavorites) {
                // search in name, description, or ingredients
                if (recipe.getName().toLowerCase().contains(lowerQuery) ||
                    recipe.getDescription().toLowerCase().contains(lowerQuery) ||
                    recipe.getMoodType().toLowerCase().contains(lowerQuery)) {
                    filteredFavorites.add(recipe);
                } else if (recipe.getIngredients() != null) {
                    for (String ingredient : recipe.getIngredients()) {
                        if (ingredient.toLowerCase().contains(lowerQuery)) {
                            filteredFavorites.add(recipe);
                            break;
                        }
                    }
                }
            }
        }
        updateUI();
    }

    private void setupRecyclerView() {
        recipeAdapter = new RecipeAdapter(new ArrayList<>(), new RecipeAdapter.OnRecipeClickListener() {
            @Override
            public void onRecipeClick(Recipe recipe) {
                openRecipeDetails(recipe);
            }

            @Override
            public void onFavoriteClick(Recipe recipe, int position) {
                toggleFavorite(recipe);
            }
        });
        
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFavorites.setAdapter(recipeAdapter);
    }

    private void loadFavorites() {
        // show loading message
        tvNoFavorites.setText("loading favorites...");
        tvNoFavorites.setVisibility(View.VISIBLE);
        recyclerViewFavorites.setVisibility(View.GONE);
        
        // get favorite recipe ids from database
        List<Integer> favoriteIds = databaseHelper.getFavoriteRecipeIds();
        
        if (favoriteIds.isEmpty()) {
            allFavorites.clear();
            filteredFavorites.clear();
            etSearch.setText("");
            updateUI();
            return;
        }
        
        // fetch each favorite recipe from api
        allFavorites.clear();
        for (Integer recipeId : favoriteIds) {
            fetchRecipeById(String.valueOf(recipeId));
        }
    }
    
    // fetch recipe details from api by id
    private void fetchRecipeById(String recipeId) {
        RetrofitClient.getApiService().getMealById(recipeId).enqueue(new Callback<MealApiResponse>() {
            @Override
            public void onResponse(Call<MealApiResponse> call, Response<MealApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MealApiResponse.MealItem> meals = response.body().getMeals();
                    if (meals != null && !meals.isEmpty()) {
                        Recipe recipe = ApiHelper.convertMealToRecipe(meals.get(0));
                        recipe.setFavorite(true); // mark as favorite
                        allFavorites.add(recipe);
                        filteredFavorites = new ArrayList<>(allFavorites);
                        etSearch.setText("");
                        updateUI();
                    }
                }
            }

            @Override
            public void onFailure(Call<MealApiResponse> call, Throwable t) {
                Toast.makeText(FavoritesActivity.this, "error loading some favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        if (filteredFavorites.isEmpty()) {
            tvNoFavorites.setVisibility(android.view.View.VISIBLE);
            recyclerViewFavorites.setVisibility(android.view.View.GONE);
            
            if (allFavorites.isEmpty()) {
                tvNoFavorites.setText("no favorite recipes yet! start searching for meals and tap the heart to save them here.");
            } else {
                tvNoFavorites.setText("no recipes match your search");
            }
        } else {
            tvNoFavorites.setVisibility(android.view.View.GONE);
            recyclerViewFavorites.setVisibility(android.view.View.VISIBLE);
            recipeAdapter.updateRecipes(filteredFavorites);
        }
    }

    private void toggleFavorite(Recipe recipe) {
        // remove from favorites since this is the favorites screen
        databaseHelper.removeFromFavorites(recipe.getId());
        recipe.setFavorite(false);
        
        // refresh the list
        loadFavorites();
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
