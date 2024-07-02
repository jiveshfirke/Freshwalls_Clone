package com.dedsec.freshwalls.data.remote

import com.dedsec.freshwalls.data.remote.dto.authentication.LoginWithProviderDto
import com.dedsec.freshwalls.data.remote.dto.wallpaper.WallpaperDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FWApi {
    @POST(value = "") //Add Header TODO
    suspend fun loginWithProvider(
        @Query(value = "name") name: String,
        @Query(value = "provider") provider: String = "google",
        @Query(value = "provider_id") providerId: Int = 1,
        @Query(value = "email") email: String
    ): LoginWithProviderDto

    @GET(value = "") //Add header //TODO
    suspend fun getLatestWallpapers(
        @Query(value = "page") pageNumber: Int,
        @Query(value = "item_count") itemCount: Int
    ): WallpaperDto
}