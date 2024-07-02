package com.dedsec.freshwalls.navigation

sealed class Screen(val route: String) {
    data object SplashScreen: Screen("splash_screen")
    data object AuthenticationScreen: Screen("authentication_screen")
    data object ExploreScreen: Screen("explore_screen")
    data object VarietyScreen: Screen("variety_screen")
    data object TrendingScreen: Screen("trending_screen")
    data object ProfileScreen: Screen("profile_screen")
    data object LikedWallpaperScreen: Screen("liked_wallpaper_screen")
    data object AppSettingsScreen: Screen("app_settings_screen")
    data object WallpaperDetailScreen: Screen("wallpaper_detail_screen")
    data object SpecificCategoryScreen: Screen("specific_category_screen")
}
