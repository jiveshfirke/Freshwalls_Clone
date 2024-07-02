package com.dedsec.freshwalls

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dedsec.freshwalls.navigation.FWNavigation
import com.dedsec.freshwalls.navigation.Screen
import com.dedsec.freshwalls.navigation.bottomNavItems
import com.dedsec.freshwalls.presentation.authentication.AuthenticationScreen
import com.dedsec.freshwalls.presentation.ui.theme.FreshwallsTheme
import com.dedsec.freshwalls.presentation.ui.theme.navigationBarSelectedIconColor
import com.dedsec.freshwalls.presentation.ui.theme.navigationBarUnSelectedIconColor
import com.dedsec.freshwalls.presentation.ui.theme.white
import com.dedsec.freshwalls.utils.SharedPreferenceManager

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun App() {
    FreshwallsTheme {
        val context = LocalContext.current
        val sharedPrefsManager = remember { SharedPreferenceManager(context) }
        val navController = rememberNavController()
        val currentEntry by navController.currentBackStackEntryAsState()
        if (sharedPrefsManager.isUserLoggedIn()) {
            var selected by remember {
                mutableIntStateOf(0)
            }
            Scaffold(
                bottomBar = {
                    if (currentEntry?.destination?.route != Screen.ProfileScreen.route &&
                        currentEntry?.destination?.route != Screen.WallpaperDetailScreen.route) {
                        NavigationBar(
                            containerColor = white,
                            content = {
                                bottomNavItems.forEachIndexed { index, topLevelDestination ->
                                    NavigationBarItem(
                                        colors = NavigationBarItemColors(
                                            selectedIconColor = navigationBarSelectedIconColor,
                                            unselectedIconColor = navigationBarUnSelectedIconColor,
                                            disabledIconColor = Color.Unspecified,
                                            unselectedTextColor = Color.Unspecified,
                                            disabledTextColor = Color.Unspecified,
                                            selectedTextColor = Color.Unspecified,
                                            selectedIndicatorColor = Color.Unspecified
                                        ),
                                        selected = currentEntry?.destination?.route == topLevelDestination.route,
                                        onClick = {
                                            selected = index
                                            navController.navigate(
                                                topLevelDestination.route,
                                                builder = {
                                                    popUpTo(Screen.ExploreScreen.route) {
                                                        inclusive = false
                                                    }
                                                }
                                            )
                                        },
                                        icon = {
                                            Icon(
                                                painter = painterResource(id = topLevelDestination.selectedIcon),
                                                contentDescription = null
                                            )
                                        })
                                }
                            })
                    }
                },
                content = { innerPaddings ->
                    FWNavigation(
                        modifier = Modifier.padding(innerPaddings),
                        navHostController = navController
                    )
                }
            )
        } else {
            AuthenticationScreen(navHostController = navController)
        }
    }
}