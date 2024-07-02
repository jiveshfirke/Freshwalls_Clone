package com.dedsec.freshwalls.presentation.variety

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dedsec.freshwalls.presentation.ui.components.SpecificWallpaperItem
import com.dedsec.freshwalls.presentation.ui.theme.black
import com.dedsec.freshwalls.presentation.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecificCategoryScreen(title: String) {
    val showShimmer = remember { mutableStateOf(false) }

    val gridState = rememberLazyGridState()
    val firstItemVisible by remember {
        derivedStateOf {
            gridState.canScrollBackward == false
        }
    }

    val appBarHeight by animateDpAsState(targetValue = if (firstItemVisible) 70.dp else 50.dp)
    val textSize by animateFloatAsState(targetValue = if (firstItemVisible) 50f else 30f)

    Scaffold(
        content = { it ->
            Column(
                modifier = Modifier
                    .background(white)
                    .fillMaxSize()
                    .padding(20.dp),
                content = {
                    Box(
                        modifier = Modifier
                            .height(appBarHeight)
                    ){
                        Text(
                            text = title,
                            style = TextStyle(
                                color = black,
                                fontSize = textSize.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        state = gridState,
                        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
                        content = {
                            items(100) {
                                val imageUrl =
                                    "https://cdn.pixabay.com/photo/2017/01/29/14/19/kermit-2018085_1280.jpg"
                                SpecificWallpaperItem(imageUrl = imageUrl, isLoading = showShimmer)
                            }
                        }
                    )
                }
            )
        }
    )

}