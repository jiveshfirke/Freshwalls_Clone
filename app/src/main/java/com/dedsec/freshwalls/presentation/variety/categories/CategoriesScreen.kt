package com.dedsec.freshwalls.presentation.variety.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.dedsec.freshwalls.common.Constants
import com.dedsec.freshwalls.navigation.Screen
import com.dedsec.freshwalls.presentation.ui.theme.white

@Composable
fun CategoriesScreen(navHostController: NavHostController) {
    val categoriesList = listOf("Abstract", "Amoled", "Animal", "Anime", "Exclusive", "Games", "Gradient", "Minimal", "Nature", "Shapes", "Shows", "Space","Sports", "Stock", "Superheroes")
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        content = {
            items(categoriesList.count()) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .height(132.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            navHostController.navigate("${Screen.SpecificCategoryScreen.route}/${categoriesList[it]}")
                        },
                    content = {
                        AsyncImage(
                            model = Constants.DUMMY_IMAGE_URL,
                            contentDescription = "Background Image"
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            content = {
                                Text(
                                    text = categoriesList[it],
                                    style = TextStyle(
                                        color = white,
                                        fontSize = 34.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        )
                    }
                )
            }
        }
    )
}