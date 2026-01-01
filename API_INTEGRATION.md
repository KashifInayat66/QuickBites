# api integration for quickbites

## what we changed

instead of using a local sql database with 21 recipes, we switched to using themealdb api. this means the app now gets recipes from the internet with real images.

## why we did this

- get way more recipes (thousands instead of 21)
- real images for every recipe
- dont have to manually add recipes to database
- recipes are always up to date
- looks way more professional

## what api we use

**themealdb api** - its completely free and has tons of recipes with images

- no api key needed
- unlimited requests
- has good recipe data
- searchable by ingredient

## what we added

### 1. internet permission
added to androidmanifest so app can connect to internet

### 2. new libraries
- **retrofit** - makes api calls easy
- **okhttp** - handles network stuff
- **glide** - loads images from internet
- **gson** - already had it, converts json to objects

### 3. new files created

**mealapi response.java** - model for what api sends back
- has all the fields from api response
- ingredients, measures, instructions, image url, etc

**mealapiservice.java** - defines api endpoints
- search by ingredient
- get meal by id
- search by name
- get random meal

**retrofitclient.java** - sets up retrofit
- creates retrofit instance
- adds logging so we can see whats happening
- sets timeouts

**apihelper.java** - converts api data to our recipe model
- takes mealitem from api
- converts to recipe object
- guesses cooking time based on category
- figures out mood type
- determines cooking method from instructions

### 4. updated files

**recipe adapter** - now loads images with glide
- added imageview reference
- uses glide to load recipe images from urls
- shows placeholder while loading
- handles errors if image doesnt load

**item_recipe.xml** - added imageview at top
- 180dp height for recipe image
- shows above recipe info
- looks way better

**build.gradle.kts** - added new dependencies
- retrofit 2.9.0
- gson converter
- okhttp with logging
- glide 4.16.0

**androidmanifest.xml** - added permissions
- internet permission
- network state permission

## how it works now

1. user types ingredients like "chicken"
2. app calls themealdb api with that ingredient
3. api returns json with matching recipes
4. we convert json to our recipe objects
5. glide loads images from urls
6. shows recipes with images to user

## example api call

when user searches for chicken:
```
https://www.themealdb.com/api/json/v1/1/filter.php?i=chicken
```

api sends back:
- meal id
- meal name
- meal image url

then we call another endpoint to get full details:
```
https://www.themealdb.com/api/json/v1/1/lookup.php?i=52772
```

this gives us everything:
- instructions
- all ingredients with measures
- category
- area/cuisine
- image

## what still needs to be done

- update mainactivity to use api instead of database
- add loading indicator when fetching recipes
- handle errors if internet is down
- maybe keep database for favorites only
- test with different ingredients

## benefits

- **more recipes** - thousands vs 21
- **real images** - looks professional
- **always fresh** - api data is maintained
- **no storage** - dont need to store recipes locally
- **easier** - dont have to add recipes manually

## downsides

- **needs internet** - wont work offline
- **slower** - has to wait for api response
- **depends on api** - if api goes down, app breaks

## next steps

1. sync gradle to download new libraries
2. update mainactivity to call api
3. add progress bar for loading
4. handle network errors gracefully
5. test with different searches
6. maybe cache results for offline use

thats basically it! the app now gets recipes from the internet with images instead of using a local database.
