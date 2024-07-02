package com.dedsec.freshwalls.data.remote.dto.wallpaper

import com.dedsec.freshwalls.domain.model.wallpaper.Wallpaper

data class Wallpaper1(
    val createdAt: String,
    val extension: String,
    val identifier: Int,
    val imageURL: String,
    val likedByUser: Boolean?,
    val previewURL: String,
    val status: String,
    val thumbnailUrl: String,
    val totalDownloads: Int,
    val totalLikes: Int,
    val totalViews: Int,
    val totalWallpaperAppliedByUsers: Int,
    val wallpaperAuthorName: String,
    val wallpaperDescription: Any,
    val wallpaperName: String?
)

fun Wallpaper1.toWallpaper(): Wallpaper {
    return Wallpaper(
        identifier = identifier,
        wallpaperName = wallpaperName,
        imageURL = imageURL,
        extension = extension,
        thumbnailUrl = thumbnailUrl,
        previewURL = previewURL,
        totalViews = totalViews,
        likedByUser = likedByUser
    )
}