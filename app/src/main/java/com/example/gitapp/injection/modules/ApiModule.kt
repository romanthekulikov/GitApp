package com.example.gitapp.injection.modules

import com.example.data.data.api.GitApiService
import com.example.data.data.converters.LocalDateConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://api.github.com/"

@Module
object ApiModule {
    @Provides
    @Singleton
    fun provideGitApiService(): GitApiService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(LocalDateConverter())
            .build()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL).build()
        return retrofit.create(GitApiService::class.java)
    }
}