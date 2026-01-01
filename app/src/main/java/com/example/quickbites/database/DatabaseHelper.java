package com.example.quickbites.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

// simplified database helper - only stores favorite recipe ids now
// recipes come from themealdb api instead of local storage
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quickbites.db";
    private static final int DATABASE_VERSION = 3;
    
    // favorites table
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_RECIPE_ID = "recipe_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create favorites table - just stores recipe ids
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_RECIPE_ID + " INTEGER UNIQUE" + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    // add recipe to favorites
    public void addToFavorites(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_ID, recipeId);
        db.insertWithOnConflict(TABLE_FAVORITES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // remove recipe from favorites
    public void removeFromFavorites(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COLUMN_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});
    }

    // check if recipe is favorited
    public boolean isFavorite(int recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, null, COLUMN_RECIPE_ID + " = ?", 
                               new String[]{String.valueOf(recipeId)}, null, null, null);
        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        return isFavorite;
    }

    // get all favorite recipe ids
    public List<Integer> getFavoriteRecipeIds() {
        List<Integer> favoriteIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_FAVORITES, new String[]{COLUMN_RECIPE_ID}, 
                               null, null, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID));
                favoriteIds.add(recipeId);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favoriteIds;
    }
    
    // clear all favorites
    public void clearAllFavorites() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, null, null);
    }
}
