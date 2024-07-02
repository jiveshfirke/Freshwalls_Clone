package com.dedsec.freshwalls.navigation

import com.dedsec.freshwalls.presentation.ui.theme.Images

sealed class TopLevelDestination (
    val route: String,
    val selectedIcon: Int
){
    data object Explore: TopLevelDestination(
        route = Screen.ExploreScreen.route,
        selectedIcon = Images.ic_nav_explore
    )

    data object Variety: TopLevelDestination(
        route = Screen.VarietyScreen.route,
        selectedIcon = Images.ic_nav_category
    )

    data object Trending: TopLevelDestination(
        route = Screen.TrendingScreen.route,
        selectedIcon = Images.ic_nav_trending
    )
}

val bottomNavItems = listOf(TopLevelDestination.Explore, TopLevelDestination.Variety, TopLevelDestination.Trending)