package com.dedsec.freshwalls.presentation.variety

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dedsec.freshwalls.presentation.ui.components.AppBarTitleItem
import com.dedsec.freshwalls.presentation.variety.categories.CategoriesScreen
import com.dedsec.freshwalls.presentation.variety.colors.ColorsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VarietyScreen(navHostController: NavHostController) {
    var selected by remember {
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
            Row {
                AppBarTitleItem(
                    title = "Categories",
                    isSelected = selected == 0,
                    onClick = {
                        selected = 0
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                AppBarTitleItem(
                    title = "Colors",
                    isSelected = selected == 1,
                    onClick = {
                        selected = 1
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                content = {
                    HorizontalPager(
                        state = pagerState,
                        verticalAlignment = Alignment.Top
                    ) {
                        selected = pagerState.currentPage
                        when (it) {
                            0 -> CategoriesScreen(navHostController = navHostController)
                            1 -> ColorsScreen(navHostController = navHostController)
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun ColorPage() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        content = {
            items(10) {
                Text(text = "Color Page")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun VarietyScreenPreview() {
    VarietyScreen(navHostController = rememberNavController())
}
