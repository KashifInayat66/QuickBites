# QuickBites - Meal Ideas Fast 🍕

an android app that helps you figure out what to cook when you dont know what to make. just type in ingredients you have at home and get meal ideas instantly!

## What It Does

quickbites solves the "whats for dinner?" problem. you know when you stand in front of your fridge with food but no clue what to make? this app fixes that. type ingredients like "chicken, rice" and boom - recipe ideas with pictures and instructions.

## Features

✅ **ingredient search** - type what you got, see what you can make  
✅ **mood slider** - pick fast, healthy, comfort, or mix recipes  
✅ **real recipe images** - looks professional with themealdb api  
✅ **favorites** - save recipes you like with the heart button  
✅ **search favorites** - find saved recipes quickly  
✅ **grocery list** - add ingredients manually or from recipes  
✅ **share** - send recipes and lists to friends/roommates  
✅ **thousands of recipes** - powered by themealdb api  

## Screenshots

_coming soon - add screenshots here when ready_

## Tech Stack

- **language:** java
- **platform:** android (min sdk 21, target sdk 36)
- **architecture:** 5 activities, mvp pattern
- **api:** themealdb (free recipe api)
- **networking:** retrofit + okhttp
- **image loading:** glide
- **database:** sqlite (for favorites only)
- **data parsing:** gson

## How To Install & Run

### option 1: clone and run in android studio

1. **clone the repo**
```bash
git clone https://github.com/yourusername/QuickBites.git
cd QuickBites
```

2. **open in android studio**
   - open android studio
   - click "open an existing project"
   - select the QuickBites folder

3. **sync gradle**
   - android studio will auto-sync
   - wait for dependencies to download

4. **run the app**
   - connect android device or start emulator
   - click the green play button
   - app will install and launch

### option 2: download apk (coming soon)

_apk releases will be available in the releases section_

## Project Structure

```
app/src/main/java/com/example/quickbites/
├── MainActivity.java              # home screen with search
├── RecipeListActivity.java       # shows search results
├── RecipeDetailsActivity.java    # full recipe with instructions
├── FavoritesActivity.java        # saved recipes
├── GroceryListActivity.java      # shopping list
├── adapters/
│   ├── RecipeAdapter.java        # displays recipe cards
│   └── GroceryAdapter.java       # displays list items
├── models/
│   ├── Recipe.java               # recipe data model
│   └── GroceryItem.java          # grocery item model
├── api/
│   ├── MealApiResponse.java      # api response models
│   ├── MealApiService.java       # retrofit interface
│   ├── RetrofitClient.java       # retrofit setup
│   └── ApiHelper.java            # converts api data
└── database/
    └── DatabaseHelper.java       # stores favorite ids
```

## How It Works

1. **user searches** - types ingredients like "chicken"
2. **api call** - app calls themealdb api
3. **results** - api returns recipes with images
4. **filter** - app filters by mood type
5. **display** - shows recipes with glide-loaded images
6. **favorite** - user can save to local database
7. **details** - tap recipe for full instructions

## API Integration

uses [themealdb api](https://www.themealdb.com/api.php) - completely free with no api key required.

**endpoints used:**
- `search.php?s={query}` - search by name
- `filter.php?i={ingredient}` - search by ingredient
- `lookup.php?i={id}` - get full recipe details
- `random.php` - get random meal

**why this api?**
- free with unlimited requests
- real recipe images
- searchable by ingredient (perfect for our use case)
- detailed instructions and ingredients
- no rate limits

## Dependencies

```gradle
// networking
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:okhttp:4.12.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'

// image loading
implementation 'com.github.bumptech.glide:glide:4.16.0'

// json parsing
implementation 'com.google.code.gson:gson:2.10.1'
```

## Future Enhancements

### phase 2 (next version)
- [ ] add recipe images to details page
- [ ] meal planning calendar
- [ ] nutritional information
- [ ] user-generated recipes
- [ ] recipe ratings

### phase 3 (future)
- [ ] voice input for hands-free searching
- [ ] barcode scanner for pantry management
- [ ] ai-powered recommendations
- [ ] social features (follow friends)
- [ ] integration with grocery delivery

### monetization ideas
- premium subscription for custom recipes
- grocery store partnerships
- cooking tool affiliate links
- ad-free tier

## For Developers

### building from source

```bash
# clone repo
git clone https://github.com/yourusername/QuickBites.git

# open in android studio
# sync gradle
# run on device/emulator
```

### requirements
- android studio hedgehog or newer
- jdk 11 or higher
- android sdk 36
- internet connection for api calls

### testing
1. search with ingredient: "chicken"
2. try different mood types
3. favorite some recipes
4. check favorites screen
5. add items to grocery list
6. test sharing functionality

## Known Issues

- favorites may take a moment to load (fetching from api)
- no offline mode yet (api required)
- some recipes may not match mood perfectly (api categorization)

## Contributing

feel free to fork and submit pull requests! this is a school project but im open to improvements.

## License

this project is for educational purposes (cs-305 android development course).

## Credits

- **developer:** [your name]
- **course:** cs-305 android application development
- **api:** [themealdb.com](https://www.themealdb.com)
- **images:** themealdb api

## Contact

got questions? open an issue or reach out!

---

made with ❤️ for people who dont know what to cook
