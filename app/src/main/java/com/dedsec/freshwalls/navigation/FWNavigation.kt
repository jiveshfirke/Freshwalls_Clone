package com.dedsec.freshwalls.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dedsec.freshwalls.presentation.authentication.AuthenticationScreen
import com.dedsec.freshwalls.presentation.common.viewmodel.WallpaperDetailsSharedViewModel
import com.dedsec.freshwalls.presentation.explore.ExploreScreen
import com.dedsec.freshwalls.presentation.profile.ProfileScreen
import com.dedsec.freshwalls.presentation.profile.app_settings.AppSettingsScreen
import com.dedsec.freshwalls.presentation.profile.liked_wallpaper.LikedWallpaperScreen
import com.dedsec.freshwalls.presentation.trending.TrendingScreen
import com.dedsec.freshwalls.presentation.ui.components.WallpaperDetailScreen
import com.dedsec.freshwalls.presentation.variety.SpecificCategoryScreen
import com.dedsec.freshwalls.presentation.variety.VarietyScreen
import com.dedsec.freshwalls.utils.SharedPreferenceManager

@Composable
fun FWNavigation(modifier: Modifier, navHostController: NavHostController) {
    val context = LocalContext.current
    val sharedPrefsManager = remember { SharedPreferenceManager(context) }
    val wallpaperDetailsSharedViewModel = viewModel<WallpaperDetailsSharedViewModel>()
    NavHost(
        navController = navHostController,
        startDestination = Screen.ExploreScreen.route,
        modifier = modifier
    ) {
//        composable(SplashScreen) {
//            SplashScreen(
//                navHostController
//            )
//        }
        composable(Screen.AuthenticationScreen.route) {
            AuthenticationScreen(navHostController = navHostController)
        }
        composable(Screen.ExploreScreen.route) {
            ExploreScreen(
                navHostController = navHostController,
                wallpaperDetailsSharedViewModel = wallpaperDetailsSharedViewModel
            )
        }
        composable(
            route = Screen.WallpaperDetailScreen.route
        ) {
            WallpaperDetailScreen(wallpaperDetailsSharedViewModel = wallpaperDetailsSharedViewModel)
        }
        composable(Screen.VarietyScreen.route) {
            VarietyScreen(navHostController = navHostController)
        }
        composable(Screen.TrendingScreen.route) {
            TrendingScreen(navHostController = navHostController)
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(navHostController = navHostController)
        }
        composable(Screen.LikedWallpaperScreen.route) {
            LikedWallpaperScreen(navHostController = navHostController)
        }
        composable(Screen.AppSettingsScreen.route) {
            AppSettingsScreen(navHostController = navHostController)
        }
        composable(
            route = "${Screen.SpecificCategoryScreen.route}/{title}",
            enterTransition = { slideInVertically(animationSpec = tween(200), initialOffsetY = {it/20}) + fadeIn(animationSpec = tween(200)) },
            exitTransition = { slideOutVertically(animationSpec = tween(200), targetOffsetY = {it/20}) + fadeOut(animationSpec = tween(200)) }
        ) {
            val title = it.arguments?.getString("title") ?: ""
            SpecificCategoryScreen(title = title)
        }
    }
}