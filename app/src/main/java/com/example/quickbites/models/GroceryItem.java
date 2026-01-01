package com.example.quickbites.models;

public class GroceryItem {
    private String name;
    private boolean isChecked;

    public GroceryItem(String name) {
        this.name = name;
        this.isChecked = false;
    }

    public GroceryItem(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
