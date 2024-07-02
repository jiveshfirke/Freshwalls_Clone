package com.dedsec.freshwalls.presentation.explore.latest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.dedsec.freshwalls.common.Constants
import com.dedsec.freshwalls.domain.model.wallpaper.WallpaperDetails
import com.dedsec.freshwalls.navigation.Screen
import com.dedsec.freshwalls.presentation.common.viewmodel.WallpaperDetailsSharedViewModel
import com.dedsec.freshwalls.presentation.explore.latest.viewmodel.LatestWallpapersViewModel
import com.dedsec.freshwalls.presentation.ui.components.LoadingIndicator
import com.dedsec.freshwalls.presentation.ui.components.WallpaperItem

@Composable
fun LatestWallpapersScreen(
    latestWallpapersViewModel: LatestWallpapersViewModel = hiltViewModel(),
    navHostController: NavHostController,
    wallpaperDetailsSharedViewModel: WallpaperDetailsSharedViewModel
) {
    val showShimmer = remember { mutableStateOf(true) }
    val wallpapers = latestWallpapersViewModel.wallpapers.collectAsLazyPagingItems()
    wallpapers.apply {
        when {
            loadState.refresh is LoadState.Loading -> {
                LoadingIndicator()
            }

            loadState.refresh is LoadState.Error || loadState.append is LoadState.Error -> {
                Text(text = "An unexpected error occurred, please check your internet connection")
            }

            loadState.refresh is LoadState.NotLoading -> {

            }
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
        content = {
            items(
                count = wallpapers.itemCount,
                itemContent = { index ->
                    Surface(
                        content = {
                            WallpaperItem(
                                imageUrl = wallpapers[index]?.thumbnailUrl
                                    ?: Constants.DUMMY_IMAGE_URL,
                                isLoading = showShimmer
                            )
                        },
                        onClick = {
                            wallpaperDetailsSharedViewModel.wallpaperDetails = WallpaperDetails(
                                wallpaperId = wallpapers[index]?.identifier ?: -1,
                                wallpapers = wallpapers.itemSnapshotList.items
                            )
                            navHostController.navigate(Screen.WallpaperDetailScreen.route)
                        }
                    )

                }
            )
            item(
                content = {
                    if (wallpapers.loadState.append is LoadState.Loading) {
                        LoadingIndicator()
                    }
                }
            )
        }
    )
}