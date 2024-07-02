package com.dedsec.freshwalls.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dedsec.freshwalls.utils.DisplayMetrics

@Composable
fun WallpaperItem(imageUrl: String, isLoading: MutableState<Boolean>) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Wallpaper",
        modifier = Modifier
            .clip(RoundedCornerShape(size = 10.dp))
            .background(
                shimmeringEffect(targetValue = 1300f, showShimmer = isLoading.value)
            )
            .height(DisplayMetrics.getHeight(LocalContext.current) / 8f)
            .fillMaxWidth(),
        contentScale = ContentScale.Crop,
        onSuccess = { isLoading.value = false }
    )
}