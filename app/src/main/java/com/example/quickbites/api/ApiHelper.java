package com.example.quickbites.api;

import com.example.quickbites.models.Recipe;
import java.util.ArrayList;
import java.util.List;

// helper class to convert api data to our recipe model
public class ApiHelper {
    
    // convert mealitem from api to our recipe model
    public static Recipe convertMealToRecipe(MealApiResponse.MealItem mealItem) {
        Recipe recipe = new Recipe();
        
        // basic info
        recipe.setId(Integer.parseInt(mealItem.getIdMeal()));
        recipe.setName(mealItem.getStrMeal());
        recipe.setDescription(mealItem.getStrCategory() + " - " + mealItem.getStrArea());
        recipe.setInstructions(mealItem.getStrInstructions());
        recipe.setImageUrl(mealItem.getStrMealThumb());
        
        // get ingredients list
        List<String> ingredients = extractIngredients(mealItem);
        recipe.setIngredients(ingredients);
        
        // estimate cooking time based on category (just guessing since api doesnt have it)
        recipe.setCookingTime(estimateCookingTime(mealItem.getStrCategory()));
        
        // set mood type based on category
        recipe.setMoodType(determineMoodType(mealItem.getStrCategory()));
        
        // set cooking method based on instructions keywords
        recipe.setCookingMethod(determineCookingMethod(mealItem.getStrInstructions()));
        
        return recipe;
    }
    
    // extract ingredients from the meal item
    private static List<String> extractIngredients(MealApiResponse.MealItem meal) {
        List<String> ingredients = new ArrayList<>();
        
        // check each ingredient field and add if not empty
        addIngredientIfNotEmpty(ingredients, meal.getStrIngredient1(), meal.getStrMeasure1());
        addIngredientIfNotEmpty(ingredients, meal.getStrIngredient2(), meal.getStrMeasure2());
        addIngredientIfNotEmpty(ingredients, meal.getStrIngredient3(), meal.getStrMeasure3());
        addIngredientIfNotEmpty(ingredients, meal.getStrIngredient4(), meal.getStrMeasure4());
        addIngredientIfNotEmpty(ingredients, meal.getStrIngredient5(), meal.getStrMeasure5());
        addIngredientIfNotEmpty(ingredients, meal.getStrIngredient6(), meal.getStrMeasure6());
        addIngredientIfNotEmpty(ingredients, meal.getStrIngredient7(), meal.getStrMeasure7());
        addIngredientIfNotEmpty(ingredients, meal.getStrIngredient8(), meal.getStrMeasure8());
        addIngredientIfNotEmpty(ingredients, meal.getStrIngredient9(), meal.getStrMeasure9());
        addIngredientIfNotEmpty(ingredients, meal.getStrIngredient10(), meal.getStrMeasure10());
        
        return ingredients;
    }
    
    // add ingredient with measure if not empty
    private static void addIngredientIfNotEmpty(List<String> list, String ingredient, String measure) {
        if (ingredient != null && !ingredient.trim().isEmpty()) {
            String formatted = ingredient.trim();
            if (measure != null && !measure.trim().isEmpty()) {
                formatted = measure.trim() + " " + formatted;
            }
            list.add(formatted);
        }
    }
    
    // guess cooking time based on category
    private static int estimateCookingTime(String category) {
        if (category == null) return 30;
        
        category = category.toLowerCase();
        if (category.contains("dessert") || category.contains("breakfast")) {
            return 20; // fast stuff
        } else if (category.contains("beef") || category.contains("lamb")) {
            return 60; // takes longer
        } else if (category.contains("seafood") || category.contains("chicken")) {
            return 35; // medium time
        }
        return 30; // default
    }
    
    // determine mood type from category
    private static String determineMoodType(String category) {
        if (category == null) return "mix";
        
        category = category.toLowerCase();
        if (category.contains("dessert") || category.contains("breakfast")) {
            return "fast";
        } else if (category.contains("vegetarian") || category.contains("seafood")) {
            return "healthy";
        } else if (category.contains("pasta") || category.contains("beef")) {
            return "comfort";
        }
        return "mix";
    }
    
    // figure out cooking method from instructions
    private static String determineCookingMethod(String instructions) {
        if (instructions == null) return "stovetop";
        
        String lower = instructions.toLowerCase();
        if (lower.contains("microwave")) {
            return "microwave";
        } else if (lower.contains("oven") || lower.contains("bake")) {
            return "oven";
        } else if (lower.contains("air fryer") || lower.contains("airfryer")) {
            return "airfryer";
        }
        return "stovetop"; // default
    }
}
