package com.dedsec.freshwalls.di

import com.dedsec.freshwalls.common.Constants
import com.dedsec.freshwalls.data.remote.FWApi
import com.dedsec.freshwalls.data.repository.authentication.AuthenticationRepositoryImpl
import com.dedsec.freshwalls.data.repository.explore.latest.LatestWallpapersRepositoryImpl
import com.dedsec.freshwalls.domain.repository.authentication.AuthenticationRepository
import com.dedsec.freshwalls.domain.repository.latest.LatestWallpapersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFWApi(): FWApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FWApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(fwApi: FWApi): AuthenticationRepository {
        return AuthenticationRepositoryImpl(fwApi = fwApi)
    }

    @Provides
    @Singleton
    fun provideLatestWallpapersRepository(fwApi: FWApi): LatestWallpapersRepository {
        return LatestWallpapersRepositoryImpl(fwApi = fwApi)
    }
}