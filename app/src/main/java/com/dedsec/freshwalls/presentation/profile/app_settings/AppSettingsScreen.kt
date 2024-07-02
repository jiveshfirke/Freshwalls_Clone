package com.dedsec.freshwalls.presentation.profile.app_settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dedsec.freshwalls.presentation.ui.theme.Images
import com.dedsec.freshwalls.presentation.ui.theme.authenticationButtonColor
import com.dedsec.freshwalls.presentation.ui.theme.navigationBarSelectedIconColor
import com.dedsec.freshwalls.presentation.ui.theme.navigationBarUnSelectedIconColor
import com.dedsec.freshwalls.presentation.ui.theme.white

@Composable
fun AppSettingsScreen(navHostController: NavHostController) {
    val pushNotificationSwitch = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(3.dp, navigationBarSelectedIconColor, CircleShape),
                contentAlignment = Alignment.Center,
                content = {
                    Image(
                        painter = painterResource(id = Images.ic_cog),
                        contentDescription = "Settings Image",
                        modifier = Modifier
                            .size(80.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Settings",
                style = TextStyle(
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(authenticationButtonColor)
                    .padding(horizontal = 20.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                content = {
                    Text(
                        text = "Push Notification",
                        style = TextStyle(
                            color = white,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                    Switch(
                        checked = pushNotificationSwitch.value,
                        onCheckedChange = { pushNotificationSwitch.value = it },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = navigationBarSelectedIconColor,
                            uncheckedTrackColor = navigationBarUnSelectedIconColor
                        )
                    )

                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(authenticationButtonColor)
                    .padding(horizontal = 20.dp, vertical = 15.dp)
                    .clickable {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://techburner.in/freshwalls-privacy-policy/")
                            )
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                content = {
                    Text(
                        text = "Privacy Policy",
                        style = TextStyle(
                            color = white,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )

                }
            )

        }
    )
}

@Preview(showBackground = true)
@Composable
fun AppSettingsScreenPreview(modifier: Modifier = Modifier) {
    AppSettingsScreen(navHostController = rememberNavController())
}