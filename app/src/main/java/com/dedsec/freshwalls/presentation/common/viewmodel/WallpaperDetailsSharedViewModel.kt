package com.dedsec.freshwalls.presentation.common.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dedsec.freshwalls.domain.model.wallpaper.WallpaperDetails

class WallpaperDetailsSharedViewModel: ViewModel() {
    var wallpaperDetails by mutableStateOf<WallpaperDetails?>(null)
}