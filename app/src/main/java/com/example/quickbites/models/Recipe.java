package com.example.quickbites.models;

import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private String description;
    private List<String> ingredients;
    private String moodType; // "fast", "healthy", "comfort", "mix"
    private int cookingTime; // in minutes
    private String instructions;
    private String cookingMethod; // "microwave", "oven", "airfryer", "stovetop"
    private boolean isFavorite;
    private String imageUrl;

    public Recipe() {}

    public Recipe(int id, String name, String description, List<String> ingredients, 
                  String moodType, int cookingTime, String instructions, String cookingMethod) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.moodType = moodType;
        this.cookingTime = cookingTime;
        this.instructions = instructions;
        this.cookingMethod = cookingMethod;
        this.isFavorite = false;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }

    public String getMoodType() { return moodType; }
    public void setMoodType(String moodType) { this.moodType = moodType; }

    public int getCookingTime() { return cookingTime; }
    public void setCookingTime(int cookingTime) { this.cookingTime = cookingTime; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getCookingMethod() { return cookingMethod; }
    public void setCookingMethod(String cookingMethod) { this.cookingMethod = cookingMethod; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
