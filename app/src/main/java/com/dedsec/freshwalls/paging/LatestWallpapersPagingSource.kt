package com.dedsec.freshwalls.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dedsec.freshwalls.domain.model.wallpaper.Wallpaper
import com.dedsec.freshwalls.domain.use_case.latest.LatestWallpapersUseCase
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LatestWallpapersPagingSource @Inject constructor(
    private val latestWallpapersUseCase: LatestWallpapersUseCase
): PagingSource<Int, Wallpaper>() {
    override fun getRefreshKey(state: PagingState<Int, Wallpaper>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wallpaper> {
        val page = params.key ?: 1
        val response = latestWallpapersUseCase.invoke(page, params.loadSize)
        return try {
            LoadResult.Page(
                data = response.data ?: emptyList(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.data?.isEmpty() == true) null else page.plus(1)
            )
        } catch (e: IOException) {
            LoadResult.Error(
                e
            )
        } catch (e: HttpException) {
            LoadResult.Error(
                e
            )
        }
    }
}