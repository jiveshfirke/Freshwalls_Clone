package com.dedsec.freshwalls.presentation.explore.latest.state

import com.dedsec.freshwalls.domain.model.wallpaper.Wallpaper

data class LatestWallpapersState(
    val isLoading: Boolean = false,
    val user: List<Wallpaper> = emptyList(),
    val error: String = ""
)