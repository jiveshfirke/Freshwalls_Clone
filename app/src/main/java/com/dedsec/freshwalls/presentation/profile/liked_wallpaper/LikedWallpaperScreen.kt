package com.dedsec.freshwalls.presentation.profile.liked_wallpaper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dedsec.freshwalls.presentation.ui.components.WallpaperItem

@Composable
fun LikedWallpaperScreen(navHostController: NavHostController) {
    val showShimmer = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .padding(horizontal = 10.dp),
        content = {
            Text(
                text = "Liked",
                style = TextStyle(
                    fontSize = 35.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
                verticalArrangement = Arrangement.spacedBy(space = 12.dp),
                content = {
                    items(10) {
                        val imageUrl = "https://cdn.pixabay.com/photo/2017/01/29/14/19/kermit-2018085_1280.jpg"
                        WallpaperItem(imageUrl = imageUrl, isLoading = showShimmer)
                    }
                }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LikedWallpaperScreenPreview() {
    LikedWallpaperScreen(navHostController = rememberNavController())
}