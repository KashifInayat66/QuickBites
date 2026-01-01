# QuickBites 🍽️

<div align="center">
  
![QuickBites Logo](app_icon.png)

**A smart meal planning Android app that turns your ingredients into delicious recipes**

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![API](https://img.shields.io/badge/Min%20API-21-blue.svg)](https://developer.android.com/about/versions/lollipop)
[![License](https://img.shields.io/badge/License-MIT-orange.svg)](LICENSE)

[Features](#features) • [Screenshots](#screenshots) • [Installation](#installation) • [Tech Stack](#tech-stack) • [Architecture](#architecture)

</div>

---

## 📖 Overview

QuickBites solves a universal problem: **"What should I cook?"** 

Instead of staring at your fridge wondering what to make, simply input your available ingredients and QuickBites instantly suggests recipes you can prepare right now. With a smart mood-based filter, real recipe images, and thousands of options powered by TheMealDB API, meal planning has never been easier.

**Perfect for:**
- Busy professionals with limited time
- College students learning to cook
- Anyone looking to reduce food waste
- Home cooks seeking inspiration

---

## ✨ Features

### Core Functionality
- 🔍 **Intelligent Ingredient Search** - Type what you have, discover what you can make
- 🎭 **Mood-Based Filtering** - Choose between Fast, Healthy, Comfort, or Mix recipes
- 📸 **Real Recipe Images** - Professional food photography for every dish
- ⭐ **Smart Favorites System** - Save and quickly access your preferred recipes
- 🔎 **Advanced Search** - Filter favorites by name, ingredients, or category
- 📝 **Integrated Grocery List** - Auto-generate shopping lists from recipes
- 🔗 **Social Sharing** - Share recipes and lists via any messaging app

### Technical Highlights
- 🌐 **Live API Integration** - Access to 1000+ recipes from TheMealDB
- 📱 **Offline Capability** - Favorites stored locally for offline access
- 🎨 **Modern Material Design** - Clean, intuitive user interface
- ⚡ **Optimized Performance** - Fast loading with image caching
- 🔄 **Real-time Updates** - Dynamic content loading with progress indicators

---

## 📱 Screenshots

<div align="center">
  
_Screenshots coming soon - App in development_

| Home Screen | Recipe Results | Recipe Details |
|-------------|----------------|----------------|
| TBD | TBD | TBD |

</div>

---

## 🚀 Installation

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 11 or higher
- Android SDK 36
- Minimum Android device: API 21 (Lollipop 5.0)

### Clone and Run

```bash
# Clone the repository
git clone https://github.com/divineib/QuickBites.git

# Navigate to project directory
cd QuickBites

# Open in Android Studio
# File > Open > Select QuickBites folder

# Sync Gradle (Android Studio will prompt)
# Wait for dependencies to download

# Run the app
# Click Run button or press Shift+F10
# Select your device/emulator
```

### Building APK

```bash
# In Android Studio
Build > Build Bundle(s) / APK(s) > Build APK(s)

# APK will be generated in:
app/build/outputs/apk/debug/app-debug.apk
```

---

## 🛠️ Tech Stack

### Languages & Frameworks
- **Java** - Primary development language
- **Android SDK** - Native Android development
- **Material Design** - UI/UX framework

### Architecture & Patterns
- **MVP Architecture** - Clean separation of concerns
- **Repository Pattern** - Data access abstraction
- **Observer Pattern** - Reactive UI updates

### Libraries & APIs

#### Networking
```gradle
Retrofit 2.9.0          // REST API client
OkHttp 4.12.0          // HTTP client
Gson 2.10.1            // JSON parsing
```

#### UI & Image Loading
```gradle
Glide 4.16.0           // Image loading and caching
Material Components    // Modern UI components
RecyclerView           // Efficient list rendering
CardView               // Material card design
```

#### Data Persistence
```gradle
SQLite                 // Local database for favorites
SharedPreferences      // App settings storage
```

#### External APIs
- **TheMealDB API** - Recipe data and images (Free tier)
  - 1000+ recipes with detailed instructions
  - High-quality food photography
  - Searchable by ingredient, name, category
  - No API key required

---

## 🏗️ Architecture

### Project Structure

```
QuickBites/
├── app/src/main/
│   ├── java/com/example/quickbites/
│   │   ├── MainActivity.java              # Home screen with search
│   │   ├── RecipeListActivity.java       # Display search results
│   │   ├── RecipeDetailsActivity.java    # Full recipe view
│   │   ├── FavoritesActivity.java        # Saved recipes
│   │   ├── GroceryListActivity.java      # Shopping list
│   │   │
│   │   ├── adapters/
│   │   │   ├── RecipeAdapter.java        # RecyclerView adapter for recipes
│   │   │   └── GroceryAdapter.java       # RecyclerView adapter for list items
│   │   │
│   │   ├── models/
│   │   │   ├── Recipe.java               # Recipe data model
│   │   │   └── GroceryItem.java          # Grocery item model
│   │   │
│   │   ├── api/
│   │   │   ├── MealApiService.java       # Retrofit API interface
│   │   │   ├── MealApiResponse.java      # API response models
│   │   │   ├── RetrofitClient.java       # Singleton Retrofit instance
│   │   │   └── ApiHelper.java            # API data transformation
│   │   │
│   │   └── database/
│   │       └── DatabaseHelper.java       # SQLite database manager
│   │
│   └── res/
│       ├── layout/                        # XML layouts
│       ├── drawable/                      # Icons and graphics
│       ├── values/                        # Strings, colors, themes
│       └── mipmap/                        # App icons
│
├── gradle/                                # Gradle configuration
├── README.md                              # This file
└── .gitignore                            # Git ignore rules
```

### Data Flow

```
User Input → MainActivity
    ↓
Search Query → RetrofitClient → TheMealDB API
    ↓
MealApiResponse → ApiHelper (Data Transformation)
    ↓
Recipe Objects → RecipeAdapter → RecyclerView
    ↓
User Selection → RecipeDetailsActivity
    ↓
Save to Favorites → DatabaseHelper → SQLite
```

---

## 🔑 Key Algorithms

### Ingredient Matching
- Searches TheMealDB API by primary ingredient
- Filters results based on mood preference
- Caches images for offline viewing

### Favorites Management
- Stores recipe IDs locally in SQLite
- Fetches full recipe data from API on demand
- Enables offline access to saved recipes

### Mood Classification
- Analyzes recipe categories and cooking times
- Auto-categorizes as Fast, Healthy, Comfort, or Mix
- Provides personalized meal suggestions

---

## 🎯 Future Enhancements

### Phase 2 (In Development)
- [ ] Meal planning calendar with weekly view
- [ ] Nutritional information and calorie tracking
- [ ] User-generated recipe uploads
- [ ] Recipe ratings and reviews system
- [ ] Advanced filters (cuisine, dietary restrictions)

### Phase 3 (Planned)
- [ ] Voice-activated ingredient input
- [ ] Barcode scanner for pantry management
- [ ] AI-powered recipe recommendations
- [ ] Social features (follow friends, share meal plans)
- [ ] Integration with grocery delivery services
- [ ] Dark mode support

### Monetization Strategy
- Premium tier with unlimited custom recipes
- Partnership with grocery retailers
- Affiliate marketing for cooking equipment
- Ad-free subscription option

---

## 🧪 Testing

### Manual Testing Checklist
- [x] Search recipes by ingredient
- [x] Filter by mood (Fast, Healthy, Comfort, Mix)
- [x] Add/remove favorites
- [x] Search within favorites
- [x] Add items to grocery list
- [x] Share recipes and lists
- [x] Handle network errors gracefully
- [x] Load images efficiently

### Test Cases
```bash
# Test ingredient search
Input: "chicken"
Expected: Display 10+ chicken recipes with images

# Test mood filter
Input: "healthy" mood selected
Expected: Show only healthy-categorized recipes

# Test favorites
Action: Toggle heart icon
Expected: Recipe persists in Favorites screen

# Test offline mode
Action: Disable internet, open Favorites
Expected: Previously favorited recipes still accessible
```

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. **Fork the repository**
2. **Create a feature branch** (`git checkout -b feature/AmazingFeature`)
3. **Commit your changes** (`git commit -m 'Add some AmazingFeature'`)
4. **Push to the branch** (`git push origin feature/AmazingFeature`)
5. **Open a Pull Request**

### Contribution Guidelines
- Follow existing code style and conventions
- Add comments for complex logic
- Test thoroughly before submitting PR
- Update README if adding new features

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Developer

**Divine Ibeawuchi**

Software Engineering Student | Android Developer

[![GitHub](https://img.shields.io/badge/GitHub-divineib-181717?style=flat&logo=github)](https://github.com/divineib)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-0077B5?style=flat&logo=linkedin)](https://linkedin.com/in/divine-ibeawuchi)

---

## 🙏 Acknowledgments

- **TheMealDB** - For providing the comprehensive recipe API
- **Material Design** - For UI/UX design guidelines
- **Android Community** - For extensive documentation and support
- **Glide Team** - For the efficient image loading library

---

## 📞 Support

If you encounter any issues or have questions:

- **Open an Issue**: [GitHub Issues](https://github.com/divineib/QuickBites/issues)
- **Email**: divine.ibeawuchi@example.com
- **Discussions**: [GitHub Discussions](https://github.com/divineib/QuickBites/discussions)

---

## ⭐ Show Your Support

If you find QuickBites helpful, please consider giving it a star ⭐ on GitHub!

---

<div align="center">

**Made with ❤️ for people who love good food but hate meal planning**

[⬆ Back to Top](#quickbites-)

</div>
