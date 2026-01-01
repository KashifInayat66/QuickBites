package com.example.quickbites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quickbites.database.DatabaseHelper;
import com.example.quickbites.models.GroceryItem;
import com.example.quickbites.models.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {
    private Recipe recipe;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        databaseHelper = new DatabaseHelper(this);

        // get recipe data from intent
        Intent intent = getIntent();
        int recipeId = intent.getIntExtra("recipe_id", -1);
        String recipeName = intent.getStringExtra("recipe_name");
        String recipeDescription = intent.getStringExtra("recipe_description");
        String ingredientsStr = intent.getStringExtra("recipe_ingredients");
        String moodType = intent.getStringExtra("recipe_mood_type");
        int cookingTime = intent.getIntExtra("recipe_cooking_time", 0);
        String instructions = intent.getStringExtra("recipe_instructions");
        String cookingMethod = intent.getStringExtra("recipe_cooking_method");

        // create recipe object
        recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setName(recipeName);
        recipe.setDescription(recipeDescription);
        recipe.setMoodType(moodType);
        recipe.setCookingTime(cookingTime);
        recipe.setInstructions(instructions);
        recipe.setCookingMethod(cookingMethod);

        // initialize views
        TextView tvRecipeName = findViewById(R.id.tvRecipeName);
        TextView tvRecipeDescription = findViewById(R.id.tvRecipeDescription);
        TextView tvCookingTime = findViewById(R.id.tvCookingTime);
        TextView tvMoodType = findViewById(R.id.tvMoodType);
        TextView tvCookingMethod = findViewById(R.id.tvCookingMethod);
        TextView tvIngredients = findViewById(R.id.tvIngredients);
        TextView tvInstructions = findViewById(R.id.tvInstructions);
        Button btnAddToGroceryList = findViewById(R.id.btnAddToGroceryList);
        Button btnClose = findViewById(R.id.btnClose);
        Button btnShare = findViewById(R.id.btnShare);

        // set data
        tvRecipeName.setText(recipeName);
        tvRecipeDescription.setText(recipeDescription);
        tvCookingTime.setText(cookingTime + " mins");
        tvMoodType.setText(moodType);
        tvCookingMethod.setText(cookingMethod);
        
        // format ingredients with line breaks
        if (ingredientsStr != null && !ingredientsStr.isEmpty()) {
            String[] ingredients = ingredientsStr.split(",");
            StringBuilder formattedIngredients = new StringBuilder();
            for (int i = 0; i < ingredients.length; i++) {
                formattedIngredients.append("• ").append(ingredients[i].trim());
                if (i < ingredients.length - 1) {
                    formattedIngredients.append("\n");
                }
            }
            tvIngredients.setText(formattedIngredients.toString());
        }
        
        tvInstructions.setText(instructions);

        // set mood color
        int moodColor = getMoodColor(moodType);
        tvMoodType.setTextColor(moodColor);

        // button listeners
        btnAddToGroceryList.setOnClickListener(v -> addToGroceryList());

        btnShare.setOnClickListener(v -> shareRecipe());

        btnClose.setOnClickListener(v -> finish());
    }

    private void shareRecipe() {
        Intent intent = getIntent();
        String recipeName = intent.getStringExtra("recipe_name");
        String ingredientsStr = intent.getStringExtra("recipe_ingredients");
        String instructions = intent.getStringExtra("recipe_instructions");
        int cookingTime = intent.getIntExtra("recipe_cooking_time", 0);
        String cookingMethod = intent.getStringExtra("recipe_cooking_method");

        StringBuilder shareText = new StringBuilder();
        shareText.append(recipeName).append("\n\n");
        shareText.append("cooking time: ").append(cookingTime).append(" mins\n");
        shareText.append("method: ").append(cookingMethod).append("\n\n");
        shareText.append("ingredients:\n");
        
        if (ingredientsStr != null && !ingredientsStr.isEmpty()) {
            String[] ingredients = ingredientsStr.split(",");
            for (String ingredient : ingredients) {
                shareText.append("- ").append(ingredient.trim()).append("\n");
            }
        }
        
        shareText.append("\ninstructions:\n").append(instructions);
        shareText.append("\n\n---\nshared from QuickBites app");

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "recipe: " + recipeName);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
        
        startActivity(Intent.createChooser(shareIntent, "share recipe via"));
    }

    private void addToGroceryList() {
        Intent intent = getIntent();
        String ingredientsStr = intent.getStringExtra("recipe_ingredients");
        
        if (ingredientsStr != null && !ingredientsStr.isEmpty()) {
            SharedPreferences prefs = getSharedPreferences("QuickBitesPrefs", MODE_PRIVATE);
            Gson gson = new Gson();
            
            // load current grocery list
            String groceryListJson = prefs.getString("grocery_list_v2", "");
            List<GroceryItem> groceryItems;
            
            if (groceryListJson.isEmpty()) {
                groceryItems = new ArrayList<>();
            } else {
                Type type = new TypeToken<List<GroceryItem>>(){}.getType();
                groceryItems = gson.fromJson(groceryListJson, type);
                if (groceryItems == null) {
                    groceryItems = new ArrayList<>();
                }
            }
            
            // add new ingredients
            String[] ingredients = ingredientsStr.split(",");
            int addedCount = 0;
            
            for (String ingredient : ingredients) {
                String trimmedIngredient = ingredient.trim();
                if (!trimmedIngredient.isEmpty()) {
                    // check for duplicates
                    boolean exists = false;
                    for (GroceryItem item : groceryItems) {
                        if (item.getName().equalsIgnoreCase(trimmedIngredient)) {
                            exists = true;
                            break;
                        }
                    }
                    
                    if (!exists) {
                        groceryItems.add(new GroceryItem(trimmedIngredient));
                        addedCount++;
                    }
                }
            }
            
            // save updated list
            String json = gson.toJson(groceryItems);
            prefs.edit().putString("grocery_list_v2", json).apply();
            
            if (addedCount > 0) {
                Toast.makeText(this, addedCount + " ingredients added to grocery list!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "all ingredients already in list", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int getMoodColor(String moodType) {
        switch (moodType.toLowerCase()) {
            case "fast":
                return 0xFFFF9800; // orange
            case "healthy":
                return 0xFF4CAF50; // green
            case "comfort":
                return 0xFF9C27B0; // purple
            case "mix":
                return 0xFF2196F3; // blue
            default:
                return 0xFF666666; // gray
        }
    }
}
