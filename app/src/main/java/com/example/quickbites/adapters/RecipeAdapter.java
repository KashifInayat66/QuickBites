package com.example.quickbites.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.quickbites.R;
import com.example.quickbites.models.Recipe;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    private final OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
        void onFavoriteClick(Recipe recipe, int position);
    }

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe, listener);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void updateRecipes(List<Recipe> newRecipes) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new RecipeDiffCallback(this.recipes, newRecipes));
        this.recipes = newRecipes;
        diffResult.dispatchUpdatesTo(this);
    }

    private static class RecipeDiffCallback extends DiffUtil.Callback {
        private final List<Recipe> oldList;
        private final List<Recipe> newList;

        public RecipeDiffCallback(List<Recipe> oldList, List<Recipe> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Recipe oldRecipe = oldList.get(oldItemPosition);
            Recipe newRecipe = newList.get(newItemPosition);
            return oldRecipe.getId() == newRecipe.getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Recipe oldRecipe = oldList.get(oldItemPosition);
            Recipe newRecipe = newList.get(newItemPosition);
            return oldRecipe.equals(newRecipe);
        }
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivRecipeImage;
        private final TextView tvRecipeName;
        private final TextView tvRecipeDescription;
        private final TextView tvCookingTime;
        private final TextView tvMoodType;
        private final TextView tvIngredients;
        private final ImageView ivFavorite;
        private final Button btnCookingMethod;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRecipeImage = itemView.findViewById(R.id.ivRecipeImage);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvRecipeDescription = itemView.findViewById(R.id.tvRecipeDescription);
            tvCookingTime = itemView.findViewById(R.id.tvCookingTime);
            tvMoodType = itemView.findViewById(R.id.tvMoodType);
            tvIngredients = itemView.findViewById(R.id.tvIngredients);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            btnCookingMethod = itemView.findViewById(R.id.btnCookingMethod);
        }

        public void bind(Recipe recipe, OnRecipeClickListener listener) {
            tvRecipeName.setText(recipe.getName());
            tvRecipeDescription.setText(recipe.getDescription());
            tvCookingTime.setText(itemView.getContext().getString(R.string.cooking_time_format, recipe.getCookingTime()));
            tvMoodType.setText(recipe.getMoodType());
            
            // load recipe image with glide
            if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(recipe.getImageUrl())
                        .placeholder(R.drawable.ic_launcher_background) // placeholder while loading
                        .error(R.drawable.ic_launcher_background) // if image fails to load
                        .centerCrop()
                        .into(ivRecipeImage);
            } else {
                // no image so just show placeholder
                ivRecipeImage.setImageResource(R.drawable.ic_launcher_background);
            }
            
            // show first few ingredients
            if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
                StringBuilder ingredientsText = new StringBuilder();
                int maxIngredients = Math.min(3, recipe.getIngredients().size());
                for (int i = 0; i < maxIngredients; i++) {
                    if (i > 0) ingredientsText.append(", ");
                    ingredientsText.append(recipe.getIngredients().get(i));
                }
                if (recipe.getIngredients().size() > 3) {
                    ingredientsText.append("...");
                }
                tvIngredients.setText(ingredientsText.toString());
            }

            // set favorite icon
            if (recipe.isFavorite()) {
                ivFavorite.setImageResource(R.drawable.ic_favorite_filled);
            } else {
                ivFavorite.setImageResource(R.drawable.ic_favorite_outline);
            }

            // set mood type color
            int moodColor = getMoodColor(recipe.getMoodType());
            tvMoodType.setTextColor(moodColor);

            // set cooking method button text
            String cookingMethod = recipe.getCookingMethod();
            if (cookingMethod != null && !cookingMethod.isEmpty()) {
                btnCookingMethod.setText("cook with: " + cookingMethod);
            } else {
                btnCookingMethod.setText("cooking method");
            }

            // click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRecipeClick(recipe);
                }
            });

            ivFavorite.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onFavoriteClick(recipe, position);
                    }
                }
            });

            // cooking method button click
            btnCookingMethod.setOnClickListener(v -> {
                new androidx.appcompat.app.AlertDialog.Builder(itemView.getContext())
                        .setTitle("how to cook:")
                        .setMessage(recipe.getInstructions())
                        .setPositiveButton("got it", null)
                        .show();
            });
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
}
