package com.dedsec.freshwalls.domain.model.wallpaper

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wallpaper(
    val extension: String,
    val identifier: Int,
    val imageURL: String,
    val likedByUser: Boolean?,
    val previewURL: String,
    val thumbnailUrl: String,
    val totalViews: Int,
    val wallpaperName: String?,
    var hueProgress: Float = 0.00f,
    var blurProgress: Float = 0f
): Parcelable
