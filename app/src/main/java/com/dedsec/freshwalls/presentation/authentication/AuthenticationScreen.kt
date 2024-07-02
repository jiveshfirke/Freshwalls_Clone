package com.dedsec.freshwalls.presentation.authentication

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.dedsec.freshwalls.domain.model.authentication.User
import com.dedsec.freshwalls.services.GoogleAuthenticationClient
import com.dedsec.freshwalls.presentation.ui.theme.Images
import com.dedsec.freshwalls.utils.SharedPreferenceManager
import kotlinx.coroutines.time.delay
import java.time.Duration
import com.dedsec.freshwalls.presentation.activities.MainActivity
import com.dedsec.freshwalls.presentation.authentication.viewmodel.AuthenticationViewModel
import com.dedsec.freshwalls.presentation.authentication.viewmodel.SignInWithGoogleViewModel
import com.dedsec.freshwalls.presentation.ui.theme.authenticationButtonColor
import com.dedsec.freshwalls.presentation.ui.theme.white
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthenticationScreen(
    navHostController: NavHostController,
    authenticationViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val sharedPrefsManager = remember { SharedPreferenceManager(context) }
    var quoteIndex by remember { mutableIntStateOf(0) }
    val quoteList = listOf(
        "\"Your wall,\nyour story\"",
        "\"Wallpaper makes a\nstatement without\na word\"",
        "\"Your Wallpaper \nMirrors your identity\"",
        "\"Every Wallpaper \nhas a story\"",
        "\"Your Wallpaper is\na reflection of\nwhat you are\""
    )
    var startAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(Duration.ofSeconds(2))
            quoteIndex = (quoteIndex + 1) % quoteList.size
            startAnimation = !startAnimation
        }
    }
    val signInWithGoogleViewModel = viewModel<SignInWithGoogleViewModel>()
    val signInState by signInWithGoogleViewModel.signInState.collectAsStateWithLifecycle()
    val googleAuthenticationClient by lazy {
        GoogleAuthenticationClient(
            context = context,
            signInClient = Identity.getSignInClient(context)
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                signInWithGoogleViewModel.viewModelScope.launch {
                    val signInResult = googleAuthenticationClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    signInWithGoogleViewModel.onSignInResult(signInResult)
                }
            }
        }
    )
    LaunchedEffect(key1 = signInState) {
        signInState.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }
    val authenticationState = authenticationViewModel.authenticationState.value
    LaunchedEffect(key1 = signInState.isSignInSuccessful, block = {
        if (signInState.isSignInSuccessful) {
            println("Printing user name ${signInState.userData?.profilePictureUrl}")
            sharedPrefsManager.saveUserProfilePicture(userProfilePicture = signInState.userData?.profilePictureUrl ?: "")
            authenticationViewModel.loginWithProvider(
                name = signInState.userData?.username ?: "",
                email = signInState.userData?.email ?: ""
            )
            signInWithGoogleViewModel.resetSignInState()
        }
    })
    // Animated float value
    val animatedValue by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = keyframes {
            durationMillis = 5000
            0.0f at 0 using LinearEasing
            1.0f at 1000 using LinearEasing
            0.0f at 2000 using LinearEasing
        }, label = "Text Fade In Fade out animation"
    )
    val quote = quoteList[quoteIndex]
    Box(
        modifier = Modifier.fillMaxSize(),
        content = {
            Box(
                modifier = Modifier,
                content = {
                    Image(
                        painter = painterResource(id = Images.bg_authentication),
                        contentDescription = "Background Image"
                    )
                    AnimatedContent(
                        targetState = quote,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .alpha(animatedValue),
                        content = {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .alpha(animatedValue)
                                    .align(Alignment.Center)
                                    .padding(horizontal = 60.dp),
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                            )
                        }, label = "Label of Fade In Fade out animation in UI"
                    )
                }
            )
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .padding(bottom = 90.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(
                        text = "Welcome to Freshwalls",
                        style = TextStyle(
                            color = authenticationButtonColor,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 35.dp)
                            .height(55.dp),
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        colors = ButtonColors(
                            contentColor = Color.Unspecified,
                            containerColor = authenticationButtonColor,
                            disabledContainerColor = authenticationButtonColor,
                            disabledContentColor = authenticationButtonColor
                        ),
                        content = {
                            Icon(
                                painter = painterResource(id = Images.ic_google),
                                contentDescription = "Google Icon",
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterVertically)
                                    .size(width = 24.dp, height = 24.dp)
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(
                                text = "Continue with Google",
                                style = TextStyle(
                                    color = white,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterVertically)
                            )
                        },
                        onClick = {
                            signInWithGoogleViewModel.viewModelScope.launch {
                                val signInIntentSender = googleAuthenticationClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        }
                    )
                }
            )
            if (authenticationState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            if (authenticationState.error.isNotBlank()) {
                Toast.makeText(context, authenticationState.error, Toast.LENGTH_SHORT).show()
            }
            if (authenticationState.user != null) {
                val user: User = authenticationState.user
                sharedPrefsManager.saveIsUserLoggedIn(isUserLoggedIn = true)
                println("Printing user id ${user.identifier}")
                println("Printing user token ${user.token}")
                println("Printing user name ${user.name}")
                sharedPrefsManager.saveUserId(userId = user.identifier)
                sharedPrefsManager.saveUserToken(userToken = user.token)
                sharedPrefsManager.saveUserName(userName = user.name)
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            }
        }
    )

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GradientBackground(modifier: Modifier = Modifier) {
    AuthenticationScreen(navHostController = rememberNavController())
}