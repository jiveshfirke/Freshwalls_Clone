package com.dedsec.freshwalls.presentation.ui.components

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.dedsec.freshwalls.domain.model.wallpaper.WallpaperOperations
import com.dedsec.freshwalls.presentation.common.viewmodel.WallpaperDetailsSharedViewModel
import com.dedsec.freshwalls.presentation.ui.theme.black
import com.dedsec.freshwalls.presentation.ui.theme.grey
import com.dedsec.freshwalls.presentation.ui.theme.lightBlackColor
import com.dedsec.freshwalls.presentation.ui.theme.lightOrangeColor
import com.dedsec.freshwalls.presentation.ui.theme.lightRed
import com.dedsec.freshwalls.presentation.ui.theme.white
import com.dedsec.freshwalls.utils.DisplayMetrics
import com.dedsec.freshwalls.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WallpaperDetailScreen(wallpaperDetailsSharedViewModel: WallpaperDetailsSharedViewModel) {
    val context: Context = LocalContext.current
    val isLoading = remember { mutableStateOf(true) }
    val wallpaperId = wallpaperDetailsSharedViewModel.wallpaperDetails?.wallpaperId ?: -1
    val currentWallpaperOperation = remember { mutableStateOf(WallpaperOperations.Default) }
    val hueProgressMap = remember { mutableStateMapOf<Int, Float>() }
    val blurProgressMap = remember { mutableStateMapOf<Int, Float>() }
    val wallpapers = wallpaperDetailsSharedViewModel.wallpaperDetails?.wallpapers ?: emptyList()
    val pagerState = rememberPagerState(
        pageCount = { wallpapers.size },
        initialPage = wallpapers.indexOf(wallpapers.find {
            it.identifier == wallpaperId
        })
    )
    val likeIconScaleX = remember { mutableFloatStateOf(0f) }
    val likeIconScaleY = remember { mutableFloatStateOf(0f) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                content = {
                    Column(
                        content = {
                            Text(
                                text = wallpapers[pagerState.currentPage].wallpaperName
                                    ?: "Wallpaper",
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = black
                                )
                            )
                            Spacer(modifier = Modifier.height(height = 10.dp))
                            Text(
                                text = "${wallpapers[pagerState.currentPage].totalViews} views",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = grey
                                )
                            )
                        }
                    )
                    Icon(
                        painter = painterResource(
                            id = if (wallpapers[pagerState.currentPage].likedByUser == null ||
                                wallpapers[pagerState.currentPage].likedByUser == false
                            ) {
                                R.drawable.ic_heart
                            } else {
                                R.drawable.ic_heart_filled
                            }
                        ),
                        contentDescription = "Like wallpaper icon",
                        tint = black,
                        modifier = Modifier.size(width = 35.dp, height = 30.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(height = 25.dp))
            HorizontalPager(
                pageSize = PageSize.Fill,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 50.dp, vertical = 20.dp),
                pageContent = { index ->
                    val pageOffset =
                        (pagerState.currentPage - index + pagerState.currentPageOffsetFraction).absoluteValue
                    val hueProgress = hueProgressMap.getOrPut(index) { 0.00f }
                    val blurProgress = blurProgressMap.getOrPut(index) { 0f }
                    Box(
                        contentAlignment = Alignment.Center,
                        content = {
                            AsyncImage(
                                model = wallpapers[index].imageURL,
                                contentDescription = "${wallpapers[index].wallpaperName} Wallpaper",
                                colorFilter = ColorFilter.colorMatrix(colorMatrix = colormatrix(value = hueProgress)),
                                modifier = Modifier
                                    .graphicsLayer {
                                        lerp(
                                            start = 0.85f,
                                            stop = 1f,
                                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                        ).also { scale ->
                                            scaleX = scale
                                            scaleY = scale
                                        }
                                        alpha = lerp(
                                            start = 0.5f,
                                            stop = 1f,
                                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                        )
                                    }
                                    .combinedClickable(
                                        onDoubleClick = {
                                            likeAnimation(
                                                likeIconScaleX = likeIconScaleX,
                                                likeIconScaleY = likeIconScaleY
                                            )
                                        },
                                        onClick = {

                                        }
                                    )
                                    .clip(RoundedCornerShape(size = 20.dp))
                                    .blur(radius = blurProgress.dp)
                                    .background(
                                        shimmeringEffect(
                                            targetValue = 1300f,
                                            showShimmer = isLoading.value
                                        )
                                    )
                                    .fillMaxWidth()
                                    .height(DisplayMetrics.getHeight(LocalContext.current).value.dp / 4.2f),
                                contentScale = ContentScale.Crop,
                                onSuccess = { isLoading.value = false }
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_heart_filled),
                                contentDescription = "Wallpaper liked animation",
                                tint = white,
                                modifier = Modifier
                                    .size(size = 92.dp)
                                    .graphicsLayer {
                                        scaleX = likeIconScaleX.floatValue
                                        scaleY = likeIconScaleY.floatValue
                                    }

                            )
                        }
                    )
                }
            )
            Spacer(modifier = Modifier.height(height = 25.dp))
            when (currentWallpaperOperation.value) {
                WallpaperOperations.Default -> {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 70.dp),
                        content = {
                            Button(
                                colors = ButtonDefaults.buttonColors(lightOrangeColor),
                                onClick = {
                                    wallpaperDetailsSharedViewModel.viewModelScope.launch {
                                        println("Printing blur value using model here ${wallpapers[pagerState.currentPage].blurProgress}")
                                        println("Printing blur value using map here ${blurProgressMap[pagerState.currentPage]}")
                                        saveImageToGallery(
                                            bitmap = generateFinalBitmap(
                                                blur = blurProgressMap[pagerState.currentPage] ?: 0f,
                                                hue = hueProgressMap[pagerState.currentPage] ?: 0f,
                                                context = context,
                                                src = loadImage(url = wallpapers[pagerState.currentPage].imageURL) ?: return@launch
                                            ),
                                            context = context,
                                            wallpaperId = wallpapers[pagerState.currentPage].identifier,
                                            wallpaperName = wallpapers[pagerState.currentPage].wallpaperName ?: "Wallpaper",
                                            extension = wallpapers[pagerState.currentPage].extension
                                        )
                                    }
                                },
                                shape = CircleShape,
                                modifier = Modifier.size(70.dp),
                                content = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_download),
                                        contentDescription = "Download icon",
                                    )
                                }
                            )
                            Button(
                                colors = ButtonDefaults.buttonColors(lightBlackColor),
                                onClick = {
                                    currentWallpaperOperation.value = WallpaperOperations.Edit
                                },
                                shape = CircleShape,
                                modifier = Modifier.size(70.dp),
                                content = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_edit),
                                        contentDescription = "Download icon",
                                    )
                                }
                            )
                            Button(
                                colors = ButtonDefaults.buttonColors(lightBlackColor),
                                onClick = { /*TODO*/ },
                                shape = CircleShape,
                                modifier = Modifier.size(70.dp),
                                content = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_apply),
                                        contentDescription = "Download icon",
                                        tint = white
                                    )
                                }
                            )
                        }
                    )
                }
                WallpaperOperations.Edit -> {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 70.dp),
                        content = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable(
                                    onClick = {
                                        currentWallpaperOperation.value = WallpaperOperations.Color
                                    }
                                ),
                                content = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_hue),
                                        tint = lightBlackColor,
                                        contentDescription = "Color icon",
                                    )
                                    Spacer(modifier = Modifier.height(height = 10.dp))
                                    Text(
                                        text = "Color",
                                        style = TextStyle(
                                            color = lightBlackColor,
                                            fontSize = 14.sp
                                        )
                                    )
                                }
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable(
                                    onClick = {
                                        currentWallpaperOperation.value = WallpaperOperations.Blur
                                    }
                                ),
                                content = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_blur),
                                        contentDescription = "Blur icon",
                                        tint = lightBlackColor,
                                    )
                                    Spacer(modifier = Modifier.height(height = 10.dp))
                                    Text(
                                        text = "Blur",
                                        style = TextStyle(
                                            color = lightBlackColor,
                                            fontSize = 14.sp
                                        )
                                    )
                                }
                            )
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(shape = RoundedCornerShape(size = 10.dp))
                                    .background(color = lightRed)
                                    .clickable {
                                        currentWallpaperOperation.value =
                                            WallpaperOperations.Default
                                    },
                                contentAlignment = Alignment.Center,
                                content = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_check),
                                        contentDescription = "Check icon",
                                        tint = white,
                                        modifier = Modifier
                                            .size(width = 24.dp, height = 24.dp)
                                    )
                                }
                            )
                        }
                    )
                }
                WallpaperOperations.Color -> {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 70.dp)
                            .fillMaxWidth(),
                        content = {
                            Slider(
                                value = hueProgressMap[pagerState.currentPage] ?: 0f,
                                onValueChange = { newValue ->
                                    hueProgressMap[pagerState.currentPage] = newValue
                                },
                                valueRange = -100.00f..100.00f,
                                colors = SliderDefaults.colors(
                                    inactiveTrackColor = lightBlackColor, thumbColor = lightBlackColor,
                                    activeTrackColor = lightBlackColor
                                ),
                                modifier = Modifier.fillMaxWidth(0.8f)
                            )
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(shape = RoundedCornerShape(size = 10.dp))
                                    .background(color = lightRed)
                                    .clickable {
                                        currentWallpaperOperation.value =
                                            WallpaperOperations.Edit
                                    },
                                contentAlignment = Alignment.Center,
                                content = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_check),
                                        contentDescription = "Check icon",
                                        tint = white,
                                        modifier = Modifier
                                            .size(width = 24.dp, height = 24.dp)
                                    )
                                }
                            )
                        }
                    )
                }
                WallpaperOperations.Blur -> {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 70.dp)
                            .fillMaxWidth(),
                        content = {
                            Slider(
                                value = blurProgressMap[pagerState.currentPage] ?: 0f,
                                onValueChange = { newValue ->
                                    blurProgressMap[pagerState.currentPage] = newValue
                                },
                                valueRange = 0f..25f,
                                colors = SliderDefaults.colors(
                                    inactiveTrackColor = lightBlackColor, thumbColor = lightBlackColor,
                                    activeTrackColor = lightBlackColor
                                ),
                                modifier = Modifier.fillMaxWidth(0.8f)
                            )
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(shape = RoundedCornerShape(size = 10.dp))
                                    .background(color = lightRed)
                                    .clickable {
                                        currentWallpaperOperation.value =
                                            WallpaperOperations.Edit
                                    },
                                contentAlignment = Alignment.Center,
                                content = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_check),
                                        contentDescription = "Check icon",
                                        tint = white,
                                        modifier = Modifier
                                            .size(width = 24.dp, height = 24.dp)
                                    )
                                }
                            )
                        }
                    )
                }
            }
        }
    )
}

fun likeAnimation(likeIconScaleX: MutableFloatState, likeIconScaleY: MutableFloatState) {
    val animation1 = ValueAnimator
        .ofFloat(1.0F, 1.5F)
        .apply {
            interpolator = OvershootInterpolator()
            duration = 750L
            addUpdateListener {
                val v = it.animatedValue as Float
                likeIconScaleX.floatValue = v
                likeIconScaleY.floatValue = v
            }
        }
    val animation2 = ValueAnimator
        .ofFloat(1.5F, 0F)
        .apply {
            interpolator = AccelerateInterpolator()
            duration = 200L
            addUpdateListener {
                val v = it.animatedValue as Float
                likeIconScaleX.floatValue = v
                likeIconScaleY.floatValue = v
            }
            startDelay = 100L
        }
    val animSet = AnimatorSet()
    animSet.playSequentially(animation1, animation2)
    animSet.start()
}

private fun cleanValue(pVal: Float): Float {
    return min(180F, max(-180F, pVal))
}

fun colormatrix(value: Float): ColorMatrix {
    val newValue = cleanValue(value) / 180F * (Math.PI.toFloat())
    val cosVal = cos(newValue)
    val sinVal = sin(newValue)
    val lumR = 0.213F
    val lumG = 0.715F
    val lumB = 0.072F
    val mat = floatArrayOf(
        lumR + cosVal * (1 - lumR) + sinVal * (-lumR), lumG + cosVal * (-lumG) + sinVal * (-lumG), lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0f, 0f,
        lumR + cosVal * (-lumR) + sinVal * (0.143f), lumG + cosVal * (1 - lumG) + sinVal * (0.140f), lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0f, 0f,
        lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)), lumG + cosVal * (-lumG) + sinVal * (lumG), lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0f, 0f,
        0f, 0f, 0f, 1f, 0f,
        0f, 0f, 0f, 0f, 1f
    )
    return ColorMatrix(mat)
}

fun saveImageToGallery(bitmap: Bitmap, context: Context, extension: String, wallpaperName: String, wallpaperId: Int) {
    println("It is in first line")
    val folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Freshwalls"
    val fileName = "${wallpaperName}_${wallpaperId}.$extension"
    val file = File(folderPath, fileName)

    // Check if folder exists, create it if not
    if (!file.parentFile?.exists()!!) {
        file.parentFile?.mkdirs()
    }

    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    // MediaScanner to notify gallery about the new image
    val mediaScannerIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    val contentUri = Uri.fromFile(file)
    mediaScannerIntent.data = contentUri
    context.applicationContext.sendBroadcast(mediaScannerIntent)
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(context, "Image saved to gallery!", Toast.LENGTH_SHORT).show()
    }
}

fun generateFinalBitmap(context: Context, src: Bitmap, hue: Float, blur: Float): Bitmap {
    // apply hue
    val tmpBitmap = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(tmpBitmap)
    val paint = Paint()
    paint.colorFilter = getHueColorMatrix(hue)
    canvas.drawBitmap(src, 0f, 0f, paint)
    println("Printing it is done blur value $blur")
    return generateBlur(context = context, src = tmpBitmap, radius = blur, scale = 1F)
}

private fun adjustHue(cm: android.graphics.ColorMatrix, value: Float) {
    val newValue = cleanValue(value) / 180F * (Math.PI.toFloat())
    if (newValue == 0f) {
        return
    }
    val cosVal = cos(newValue)
    val sinVal = sin(newValue)
    val lumR = 0.213F
    val lumG = 0.715F
    val lumB = 0.072F
    val mat = floatArrayOf(
        lumR + cosVal * (1 - lumR) + sinVal * (-lumR), lumG + cosVal * (-lumG) + sinVal * (-lumG), lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0f, 0f,
        lumR + cosVal * (-lumR) + sinVal * (0.143f), lumG + cosVal * (1 - lumG) + sinVal * (0.140f), lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0f, 0f,
        lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)), lumG + cosVal * (-lumG) + sinVal * (lumG), lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0f, 0f,
        0f, 0f, 0f, 1f, 0f,
        0f, 0f, 0f, 0f, 1f
    )
    cm.postConcat(android.graphics.ColorMatrix(mat))
}


private fun getHueColorMatrix(value: Float): android.graphics.ColorMatrixColorFilter {
    val cm = android.graphics.ColorMatrix()
    adjustHue(cm, value)
    return android.graphics.ColorMatrixColorFilter(cm)
}
private const val SCALE = 0.123F
private fun createScaledBitmap(bp: Bitmap, scale: Float): Bitmap {
    val w = (bp.width * scale).roundToInt()
    val h = (bp.height * scale).roundToInt()
    return Bitmap.createScaledBitmap(bp, w, h, false)
}
fun boxBlur(src: Bitmap, radius: Float): Bitmap {
    val width = src.width
    val height = src.height
    val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val pixelsIn = IntArray(width * height)
    val pixelsOut = IntArray(width * height)
    src.getPixels(pixelsIn, 0, width, 0, 0, width, height)

    // Apply box blur in two passes (horizontal and vertical)
    for (y in 0 until height) {
        for (x in 0 until width) {
            var sumRed = 0
            var sumGreen = 0
            var sumBlue = 0
            var sumAlpha = 0
            var count = 0

            for (dy in -radius.toInt()..radius.toInt()) {
                val yIndex = y + dy
                if (yIndex >= 0 && yIndex < height) {
                    for (dx in -radius.toInt()..radius.toInt()) {
                        val xIndex = x + dx
                        if (xIndex >= 0 && xIndex < width) {
                            val color = pixelsIn[yIndex * width + xIndex]
                            val alpha = Color.alpha(color)
                            val red = Color.red(color)
                            val green = Color.green(color)
                            val blue = Color.blue(color)
                            sumRed += alpha * red
                            sumGreen += alpha * green
                            sumBlue += alpha * blue
                            sumAlpha += alpha
                            count++
                        }
                    }
                }
            }
            val averageColor = Color.argb(
                if (count != 0) sumAlpha / count else 0,
                if (count != 0) sumRed / count else 0,
                if (count != 0) sumGreen / count else 0,
                if (count != 0) sumBlue / count else 0
            )
            pixelsOut[y * width + x] = averageColor
        }
    }

    // Apply blur again vertically
    for (x in 0 until width) {
        for (y in 0 until height) {
            var sumRed = 0
            var sumGreen = 0
            var sumBlue = 0
            var sumAlpha = 0
            var count = 0

            for (dy in -radius.toInt()..radius.toInt()) {
                val yIndex = y + dy
                if (yIndex >= 0 && yIndex < height) {
                    val color = pixelsOut[yIndex * width + x]
                    val alpha = Color.alpha(color)
                    val red = Color.red(color)
                    val green = Color.green(color)
                    val blue = Color.blue(color)
                    sumRed += alpha * red
                    sumGreen += alpha * green
                    sumBlue += alpha * blue
                    sumAlpha += alpha
                    count++
                }
            }
            val averageColor = Color.argb(
                if (count != 0) sumAlpha / count else 0,
                if (count != 0) sumRed / count else 0,
                if (count != 0) sumGreen / count else 0,
                if (count != 0) sumBlue / count else 0
            )
            pixelsOut[y * width + x] = averageColor
        }
    }

    outputBitmap.setPixels(pixelsOut, 0, width, 0, 0, width, height)
    return outputBitmap
}
fun generateBlur(context: Context, src: Bitmap, radius: Float, scale: Float = SCALE): Bitmap {
    println(" it is in Blur")
    if (radius == 0f) {
        println("Blur is less than 1")
        return src
    }
    val scaled = createScaledBitmap(src, scale)

    val outputBitmap = Bitmap.createBitmap(scaled)
    val rs = RenderScript.create(context)
    val intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    val tmpIn = Allocation.createFromBitmap(rs, scaled)
    val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
    intrinsicBlur.setRadius(25f)
    intrinsicBlur.setInput(tmpIn)
    intrinsicBlur.forEach(tmpOut)
    tmpOut.copyTo(outputBitmap)
    println("Blur is done here")
//    return outputBitmap
    val maxDimension = 1024 // Adjust this value based on your needs
    val width = src.width.coerceAtMost(maxDimension)
    val height = src.height.coerceAtMost(maxDimension)
//    val scaledBitmap = createScaledBitmap(src, width.toFloat(), height.toFloat(), true)
    return boxBlur(src = scaled, radius = radius)
}

suspend fun loadImage(url: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            val drawable = Drawable.createFromStream(inputStream, "src") as? BitmapDrawable
            println("Got bitmap")
            drawable?.bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}