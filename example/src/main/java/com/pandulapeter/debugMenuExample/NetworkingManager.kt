package com.pandulapeter.debugMenuExample

import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuCore.DebugMenuInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkingManager {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS })
        .addInterceptor(DebugMenuInterceptor(DebugMenu))
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://binaryjazz.us/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()
    val musicGeneratorService = retrofit.create(MusicGenreGeneratorService::class.java)
}