package com.example.quickbites.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// response model for themealdb api
public class MealApiResponse {
    @SerializedName("meals")
    private List<MealItem> meals;

    public List<MealItem> getMeals() {
        return meals;
    }

    public void setMeals(List<MealItem> meals) {
        this.meals = meals;
    }

    // inner class for each meal item from api
    public static class MealItem {
        @SerializedName("idMeal")
        private String idMeal;
        
        @SerializedName("strMeal")
        private String strMeal;
        
        @SerializedName("strCategory")
        private String strCategory;
        
        @SerializedName("strArea")
        private String strArea;
        
        @SerializedName("strInstructions")
        private String strInstructions;
        
        @SerializedName("strMealThumb")
        private String strMealThumb;
        
        // ingredients (up to 20 in the api)
        @SerializedName("strIngredient1")
        private String strIngredient1;
        @SerializedName("strIngredient2")
        private String strIngredient2;
        @SerializedName("strIngredient3")
        private String strIngredient3;
        @SerializedName("strIngredient4")
        private String strIngredient4;
        @SerializedName("strIngredient5")
        private String strIngredient5;
        @SerializedName("strIngredient6")
        private String strIngredient6;
        @SerializedName("strIngredient7")
        private String strIngredient7;
        @SerializedName("strIngredient8")
        private String strIngredient8;
        @SerializedName("strIngredient9")
        private String strIngredient9;
        @SerializedName("strIngredient10")
        private String strIngredient10;
        
        // measures
        @SerializedName("strMeasure1")
        private String strMeasure1;
        @SerializedName("strMeasure2")
        private String strMeasure2;
        @SerializedName("strMeasure3")
        private String strMeasure3;
        @SerializedName("strMeasure4")
        private String strMeasure4;
        @SerializedName("strMeasure5")
        private String strMeasure5;
        @SerializedName("strMeasure6")
        private String strMeasure6;
        @SerializedName("strMeasure7")
        private String strMeasure7;
        @SerializedName("strMeasure8")
        private String strMeasure8;
        @SerializedName("strMeasure9")
        private String strMeasure9;
        @SerializedName("strMeasure10")
        private String strMeasure10;

        // getters
        public String getIdMeal() { return idMeal; }
        public String getStrMeal() { return strMeal; }
        public String getStrCategory() { return strCategory; }
        public String getStrArea() { return strArea; }
        public String getStrInstructions() { return strInstructions; }
        public String getStrMealThumb() { return strMealThumb; }
        
        public String getStrIngredient1() { return strIngredient1; }
        public String getStrIngredient2() { return strIngredient2; }
        public String getStrIngredient3() { return strIngredient3; }
        public String getStrIngredient4() { return strIngredient4; }
        public String getStrIngredient5() { return strIngredient5; }
        public String getStrIngredient6() { return strIngredient6; }
        public String getStrIngredient7() { return strIngredient7; }
        public String getStrIngredient8() { return strIngredient8; }
        public String getStrIngredient9() { return strIngredient9; }
        public String getStrIngredient10() { return strIngredient10; }
        
        public String getStrMeasure1() { return strMeasure1; }
        public String getStrMeasure2() { return strMeasure2; }
        public String getStrMeasure3() { return strMeasure3; }
        public String getStrMeasure4() { return strMeasure4; }
        public String getStrMeasure5() { return strMeasure5; }
        public String getStrMeasure6() { return strMeasure6; }
        public String getStrMeasure7() { return strMeasure7; }
        public String getStrMeasure8() { return strMeasure8; }
        public String getStrMeasure9() { return strMeasure9; }
        public String getStrMeasure10() { return strMeasure10; }
    }
}
