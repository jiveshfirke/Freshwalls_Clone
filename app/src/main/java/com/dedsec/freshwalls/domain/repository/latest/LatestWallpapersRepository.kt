package com.dedsec.freshwalls.domain.repository.latest

import com.dedsec.freshwalls.data.remote.dto.wallpaper.WallpaperDto

interface LatestWallpapersRepository {
    suspend fun getLatestWallpapers(pageNumber: Int, itemCount: Int): WallpaperDto
}