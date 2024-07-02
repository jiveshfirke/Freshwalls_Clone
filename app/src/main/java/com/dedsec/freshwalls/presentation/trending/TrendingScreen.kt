package com.dedsec.freshwalls.presentation.trending

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.dedsec.freshwalls.common.Constants
import com.dedsec.freshwalls.presentation.ui.components.shimmeringEffect
import com.dedsec.freshwalls.presentation.ui.theme.black
import com.dedsec.freshwalls.presentation.ui.theme.white
import com.dedsec.freshwalls.utils.DisplayMetrics
import com.dedsec.freshwalls.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrendingScreen(navHostController: NavHostController) {
    val isLiked = remember {
        mutableStateOf(false)
    }
    val isLoading = remember {
        mutableStateOf(false)
    }
    val pagerState = rememberPagerState(pageCount = {5})
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = white,
                shape = CircleShape,
                onClick = { /*TODO*/ },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = "Download"
                    )
                }
            )
        },
        content = { it ->
            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxSize(),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        content = {
                            Text(
                                text = "Trending",
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = black
                                )
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_heart),
                                contentDescription = "Like wallpaper icon",
                                tint = black,
                                modifier = Modifier.size(width = 35.dp, height = 30.dp)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    VerticalPager(
                        pageSize = PageSize.Fill,
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        modifier = Modifier,
                        pageContent = { index ->
                            Box(
                                modifier = Modifier.padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center,
                                content = {
                                    AsyncImage(
                                        model = Constants.DUMMY_IMAGE_URL,
                                        contentDescription = "Wallpaper",
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(size = 20.dp))
                                            .background(
                                                shimmeringEffect(
                                                    targetValue = 1300f,
                                                    showShimmer = isLoading.value
                                                )
                                            )
                                            .fillMaxWidth()
                                            .height(DisplayMetrics.getHeight(LocalContext.current).value.dp / 3.5f),
                                        contentScale = ContentScale.Crop,
                                        onSuccess = { isLoading.value = false }
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_heart_filled),
                                        contentDescription = "Wallpaper liked animation",
                                        tint = white,
                                        modifier = Modifier
                                            .size(size = 92.dp)
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }
    )

}

@Preview(showBackground = true)
@Composable
fun TrendingScreenPreview(modifier: Modifier = Modifier) {
    TrendingScreen(navHostController = rememberNavController())
}