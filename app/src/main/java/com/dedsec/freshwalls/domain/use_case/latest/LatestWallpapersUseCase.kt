package com.dedsec.freshwalls.domain.use_case.latest

import com.dedsec.freshwalls.common.Resource
import com.dedsec.freshwalls.data.remote.dto.wallpaper.toWallpaper
import com.dedsec.freshwalls.domain.model.wallpaper.Wallpaper
import com.dedsec.freshwalls.domain.repository.latest.LatestWallpapersRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LatestWallpapersUseCase @Inject constructor(
    private val latestWallpapersRepository: LatestWallpapersRepository
) {
    suspend operator fun invoke(pageNumber: Int, itemCount: Int): Resource<List<Wallpaper>> {
        try {
            val wallpapers = latestWallpapersRepository.getLatestWallpapers(
                pageNumber = pageNumber, itemCount = itemCount
            ).data.map {
                it.toWallpaper()
            }
            return Resource.Success(data = wallpapers)
        } catch (e: HttpException) {
            return Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred")
        } catch (e: IOException) {
            return Resource.Error(message = "Couldn't reach server, please check your Internet connection")
        }
    }

}