package com.dedsec.freshwalls.presentation.explore.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dedsec.freshwalls.navigation.Screen
import com.dedsec.freshwalls.presentation.ui.components.AppBarTitleItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExploreScreenAppBar(
    selected: MutableState<Int>, coroutineScope: CoroutineScope,
    pagerState: PagerState, navHostController: NavHostController
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        content = {
            Row {
                AppBarTitleItem(
                    title = "Latest",
                    isSelected = selected.value == 0,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                            selected.value = 0
                        }
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                AppBarTitleItem(
                    title = "For You",
                    isSelected = selected.value == 1,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                            selected.value = 1
                        }
                    }
                )
            }
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        navHostController.navigate(Screen.ProfileScreen.route)
                    }
            )
        }
    )
}