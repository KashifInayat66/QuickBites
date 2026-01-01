package com.example.quickbites;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickbites.adapters.GroceryAdapter;
import com.example.quickbites.models.GroceryItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroceryListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewGroceryList;
    private TextView tvPlaceholder;
    private EditText etNewItem;
    private Button btnAddItem;
    private Button btnClearList;
    private Button btnBack;
    private GroceryAdapter groceryAdapter;
    private SharedPreferences prefs;
    private List<GroceryItem> groceryItems;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        prefs = getSharedPreferences("QuickBitesPrefs", MODE_PRIVATE);
        gson = new Gson();
        groceryItems = new ArrayList<>();

        initViews();
        setupRecyclerView();
        loadGroceryList();
        setupClickListeners();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadGroceryList();
    }

    private void initViews() {
        recyclerViewGroceryList = findViewById(R.id.recyclerViewGroceryList);
        tvPlaceholder = findViewById(R.id.tvGroceryPlaceholder);
        etNewItem = findViewById(R.id.etNewItem);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnClearList = findViewById(R.id.btnClearList);
        btnBack = findViewById(R.id.btnBack);
        Button btnShareList = findViewById(R.id.btnShareList);
        
        btnShareList.setOnClickListener(v -> shareGroceryList());
    }

    private void setupRecyclerView() {
        groceryAdapter = new GroceryAdapter(groceryItems, new GroceryAdapter.OnItemActionListener() {
            @Override
            public void onItemChecked(GroceryItem item, int position) {
                saveGroceryList();
                groceryAdapter.notifyItemChanged(position);
            }

            @Override
            public void onItemRemove(GroceryItem item, int position) {
                removeItem(position);
            }
        });

        recyclerViewGroceryList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGroceryList.setAdapter(groceryAdapter);
    }

    private void setupClickListeners() {
        btnAddItem.setOnClickListener(v -> addNewItem());
        btnClearList.setOnClickListener(v -> showClearConfirmation());
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadGroceryList() {
        String groceryListJson = prefs.getString("grocery_list_v2", "");
        
        if (groceryListJson.isEmpty()) {
            // try to migrate old format
            migrateOldFormat();
        } else {
            Type type = new TypeToken<List<GroceryItem>>(){}.getType();
            groceryItems = gson.fromJson(groceryListJson, type);
            if (groceryItems == null) {
                groceryItems = new ArrayList<>();
            }
        }

        updateUI();
    }

    private void migrateOldFormat() {
        String oldList = prefs.getString("grocery_list", "");
        if (!oldList.isEmpty()) {
            String[] items = oldList.split(",");
            Set<String> uniqueItems = new HashSet<>();
            for (String item : items) {
                uniqueItems.add(item.trim());
            }
            
            groceryItems.clear();
            for (String item : uniqueItems) {
                if (!item.isEmpty()) {
                    groceryItems.add(new GroceryItem(item));
                }
            }
            saveGroceryList();
            // clear old format
            prefs.edit().remove("grocery_list").apply();
        }
    }

    private void saveGroceryList() {
        String json = gson.toJson(groceryItems);
        prefs.edit().putString("grocery_list_v2", json).apply();
    }

    private void updateUI() {
        if (groceryItems.isEmpty()) {
            tvPlaceholder.setVisibility(android.view.View.VISIBLE);
            recyclerViewGroceryList.setVisibility(android.view.View.GONE);
            tvPlaceholder.setText("no items in grocery list yet!\n\nadd ingredients from recipe details or tap 'add' above");
        } else {
            tvPlaceholder.setVisibility(android.view.View.GONE);
            recyclerViewGroceryList.setVisibility(android.view.View.VISIBLE);
            groceryAdapter.updateItems(groceryItems);
        }
    }

    private void addNewItem() {
        String itemName = etNewItem.getText().toString().trim();
        
        if (itemName.isEmpty()) {
            Toast.makeText(this, "please enter an item name", Toast.LENGTH_SHORT).show();
            return;
        }

        // check for duplicates
        for (GroceryItem item : groceryItems) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                Toast.makeText(this, "item already in list", Toast.LENGTH_SHORT).show();
                etNewItem.setText("");
                return;
            }
        }

        groceryItems.add(new GroceryItem(itemName));
        saveGroceryList();
        updateUI();
        etNewItem.setText("");
        Toast.makeText(this, "item added!", Toast.LENGTH_SHORT).show();
    }

    private void removeItem(int position) {
        if (position >= 0 && position < groceryItems.size()) {
            groceryItems.remove(position);
            saveGroceryList();
            updateUI();
            Toast.makeText(this, "item removed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showClearConfirmation() {
        if (groceryItems.isEmpty()) {
            Toast.makeText(this, "list is already empty", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("clear grocery list")
                .setMessage("are you sure you want to remove all items?")
                .setPositiveButton("yes", (dialog, which) -> clearList())
                .setNegativeButton("no", null)
                .show();
    }

    private void clearList() {
        groceryItems.clear();
        saveGroceryList();
        updateUI();
        Toast.makeText(this, "list cleared", Toast.LENGTH_SHORT).show();
    }

    private void shareGroceryList() {
        if (groceryItems.isEmpty()) {
            Toast.makeText(this, "grocery list is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder shareText = new StringBuilder();
        shareText.append("my grocery list\n\n");
        
        int uncheckedCount = 0;
        int checkedCount = 0;
        
        // add unchecked items first
        for (GroceryItem item : groceryItems) {
            if (!item.isChecked()) {
                shareText.append("[ ] ").append(item.getName()).append("\n");
                uncheckedCount++;
            }
        }
        
        // add checked items
        if (checkedCount > 0 || uncheckedCount < groceryItems.size()) {
            shareText.append("\npurchased:\n");
            for (GroceryItem item : groceryItems) {
                if (item.isChecked()) {
                    shareText.append("[X] ").append(item.getName()).append("\n");
                    checkedCount++;
                }
            }
        }
        
        shareText.append("\ntotal items: ").append(groceryItems.size());
        if (checkedCount > 0) {
            shareText.append(" (").append(checkedCount).append(" purchased)");
        }
        shareText.append("\n\n---\nshared from QuickBites app");

        android.content.Intent shareIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "my grocery list");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText.toString());
        
        startActivity(android.content.Intent.createChooser(shareIntent, "share grocery list via"));
    }
}
