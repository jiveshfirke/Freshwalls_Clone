package com.dedsec.freshwalls.data.repository.explore.latest

import com.dedsec.freshwalls.data.remote.FWApi
import com.dedsec.freshwalls.data.remote.dto.wallpaper.WallpaperDto
import com.dedsec.freshwalls.domain.repository.latest.LatestWallpapersRepository
import javax.inject.Inject

class LatestWallpapersRepositoryImpl @Inject constructor(
    private val fwApi: FWApi
): LatestWallpapersRepository {
    override suspend fun getLatestWallpapers(pageNumber: Int, itemCount: Int): WallpaperDto {
        return fwApi.getLatestWallpapers(pageNumber = pageNumber, itemCount = itemCount)
    }
}