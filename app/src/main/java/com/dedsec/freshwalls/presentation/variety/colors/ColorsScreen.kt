package com.dedsec.freshwalls.presentation.variety.colors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dedsec.freshwalls.navigation.Screen
import com.dedsec.freshwalls.presentation.ui.theme.black
import com.dedsec.freshwalls.presentation.ui.theme.blue
import com.dedsec.freshwalls.presentation.ui.theme.brown
import com.dedsec.freshwalls.presentation.ui.theme.cyan
import com.dedsec.freshwalls.presentation.ui.theme.darkGrey
import com.dedsec.freshwalls.presentation.ui.theme.darkYellow
import com.dedsec.freshwalls.presentation.ui.theme.green
import com.dedsec.freshwalls.presentation.ui.theme.lightGrey
import com.dedsec.freshwalls.presentation.ui.theme.lime
import com.dedsec.freshwalls.presentation.ui.theme.orange
import com.dedsec.freshwalls.presentation.ui.theme.pink
import com.dedsec.freshwalls.presentation.ui.theme.red
import com.dedsec.freshwalls.presentation.ui.theme.violet
import com.dedsec.freshwalls.presentation.ui.theme.white
import com.dedsec.freshwalls.presentation.ui.theme.yellow
import java.util.Locale

@Composable
fun ColorsScreen(navHostController: NavHostController) {
    val colorsList = listOf(black, blue, brown, cyan, darkYellow, green, darkGrey, lime, orange, pink, violet, red, lightGrey, white, yellow)
    val colorsNameList = listOf("black", "blue", "brown", "cyan", "darkYellow", "green", "darkGrey", "lime", "orange", "pink", "violet", "red", "lightGrey", "white", "yellow")
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        content = {
            items(colorsList.count()) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(colorsList[index])
                        .clickable {
                            navHostController.navigate("${Screen.SpecificCategoryScreen.route}/${colorsNameList[index].replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            }}")
                        }
                )
            }
        }
    )
}