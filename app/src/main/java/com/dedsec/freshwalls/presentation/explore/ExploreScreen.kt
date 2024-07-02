package com.dedsec.freshwalls.presentation.explore

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dedsec.freshwalls.presentation.common.viewmodel.WallpaperDetailsSharedViewModel
import com.dedsec.freshwalls.presentation.explore.components.ExploreScreenAppBar
import com.dedsec.freshwalls.presentation.explore.latest.LatestWallpapersScreen
import com.dedsec.freshwalls.presentation.ui.components.WallpaperItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExploreScreen(
    navHostController: NavHostController,
    wallpaperDetailsSharedViewModel: WallpaperDetailsSharedViewModel
) {
    val selected = remember {
        mutableIntStateOf(0)
    }

    val pagerState = rememberPagerState(pageCount = { 2 })

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxSize()
            .padding(top = 20.dp),
        content = {
            ExploreScreenAppBar(
                selected = selected,
                coroutineScope = coroutineScope,
                pagerState = pagerState,
                navHostController = navHostController
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                content = {
                    HorizontalPager(
                        state = pagerState,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxSize(),
                        pageContent = {
                            when (it) {
                                0 -> LatestWallpapersScreen(
                                    navHostController = navHostController,
                                    wallpaperDetailsSharedViewModel = wallpaperDetailsSharedViewModel
                                )

                                1 -> ForYouPage()
                            }
                            selected.intValue = pagerState.currentPage
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun ForYouPage() {
    val showShimmer = remember { mutableStateOf(true) }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
        content = {
            items(10) {
                val imageUrl =
                    "https://cdn.pixabay.com/photo/2017/01/29/14/19/kermit-2018085_1280.jpg"
                WallpaperItem(imageUrl = imageUrl, isLoading = showShimmer)
            }
        }
    )
}