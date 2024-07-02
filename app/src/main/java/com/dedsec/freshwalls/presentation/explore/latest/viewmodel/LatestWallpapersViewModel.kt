package com.dedsec.freshwalls.presentation.explore.latest.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dedsec.freshwalls.domain.model.wallpaper.Wallpaper
import com.dedsec.freshwalls.paging.LatestWallpapersPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class LatestWallpapersViewModel @Inject constructor(
    private val latestWallpapersPagingSource: LatestWallpapersPagingSource
) : ViewModel() {
    private val _wallpapers: MutableStateFlow<PagingData<Wallpaper>> =
        MutableStateFlow(PagingData.empty())
    val wallpapers: StateFlow<PagingData<Wallpaper>> = _wallpapers.asStateFlow()

    init {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = 10, enablePlaceholders = true
                ),
                pagingSourceFactory = {
                    latestWallpapersPagingSource
                }
            ).flow.cachedIn(viewModelScope).collect {
                _wallpapers.value = it
            }
        }
    }
}