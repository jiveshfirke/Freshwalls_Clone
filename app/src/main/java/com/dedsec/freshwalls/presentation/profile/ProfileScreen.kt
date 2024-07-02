package com.dedsec.freshwalls.presentation.profile

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dedsec.freshwalls.navigation.Screen
import com.dedsec.freshwalls.presentation.ui.theme.Images
import com.dedsec.freshwalls.presentation.ui.theme.authenticationButtonColor
import com.dedsec.freshwalls.presentation.ui.theme.grey
import com.dedsec.freshwalls.presentation.ui.theme.white

@Composable
fun ProfileScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Image(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Name",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            ButtonLayout(
                icon = Images.ic_nav_trending,
                title = "Liked Wallpapers",
                onClick = {
                    navHostController.navigate(Screen.LikedWallpaperScreen.route)
                }
            )
            ButtonLayout(
                icon = Images.ic_settings,
                title = "App Settings",
                onClick = {
                    navHostController.navigate(Screen.AppSettingsScreen.route)
                }
            )
            ButtonLayout(
                icon = Images.ic_rating,
                title = "Rate app",
                onClick = { goForAppRate(context) }
            )
            ButtonLayout(
                icon = Images.ic_log_out,
                title = "Log out",
                onClick = {}
            )
            ButtonLayout(
                icon = Images.ic_delete_forever,
                title = "Delete Account",
                onClick = {}
            )
        }
    )
}

@Composable
fun ButtonLayout(icon: Int, title: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp)
            .padding(vertical = 5.dp)
            .height(55.dp),
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        colors = ButtonColors(
            contentColor = Color.Unspecified,
            containerColor = authenticationButtonColor,
            disabledContainerColor = authenticationButtonColor,
            disabledContentColor = authenticationButtonColor
        ),
        content = {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxSize(),
                content = {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .size(width = 24.dp, height = 24.dp),
                        tint = grey
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = title,
                        style = TextStyle(
                            color = white,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                    )
                }
            )
        },
        onClick = {
            onClick()
        }
    )
}

private fun goForAppRate(context: Context) {
    try {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.techburner.freshwalls"))
        )
    } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.techburner.freshwalls"))
        )
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navHostController = rememberNavController())
}