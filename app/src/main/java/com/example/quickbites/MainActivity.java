package com.example.quickbites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etIngredients;
    private SeekBar seekBarMood;
    private TextView tvCurrentMood;
    private Button btnSearchRecipes;
    private Button btnFavorites;
    private Button btnGroceryList;
    
    private final String[] moodTypes = {"fast", "healthy", "comfort", "mix"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupMoodSlider();
        setupClickListeners();
    }

    private void initViews() {
        etIngredients = findViewById(R.id.etIngredients);
        seekBarMood = findViewById(R.id.seekBarMood);
        tvCurrentMood = findViewById(R.id.tvCurrentMood);
        btnSearchRecipes = findViewById(R.id.btnSearchRecipes);
        btnFavorites = findViewById(R.id.btnFavorites);
        btnGroceryList = findViewById(R.id.btnGroceryList);
    }

    private void setupMoodSlider() {
        // set initial mood
        tvCurrentMood.setText(moodTypes[0]);
        
        seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateMoodDisplay(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // no action needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // no action needed
            }
        });
    }

    private void updateMoodDisplay(int progress) {
        String currentMood = moodTypes[progress];
        tvCurrentMood.setText(currentMood);
        
        // change color based on mood
        int color;
        switch (currentMood) {
            case "fast":
                color = 0xFFFF9800; // orange
                break;
            case "healthy":
                color = 0xFF4CAF50; // green
                break;
            case "comfort":
                color = 0xFF9C27B0; // purple
                break;
            case "mix":
                color = 0xFF2196F3; // blue
                break;
            default:
                color = 0xFF4CAF50; // default green
        }
        tvCurrentMood.setTextColor(color);
    }

    private void setupClickListeners() {
        btnSearchRecipes.setOnClickListener(v -> searchRecipes());
        btnFavorites.setOnClickListener(v -> openFavorites());
        btnGroceryList.setOnClickListener(v -> openGroceryList());
    }

    private void searchRecipes() {
        String ingredients = etIngredients.getText().toString().trim();
        String moodType = moodTypes[seekBarMood.getProgress()];
        
        Intent intent = new Intent(this, RecipeListActivity.class);
        intent.putExtra("ingredients", ingredients);
        intent.putExtra("moodType", moodType);
        startActivity(intent);
    }

    private void openFavorites() {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    private void openGroceryList() {
        Intent intent = new Intent(this, GroceryListActivity.class);
        startActivity(intent);
    }
}
