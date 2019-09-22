package com.pandulapeter.debugMenuExample.networking

import com.pandulapeter.debugMenu.DebugMenuNetworkInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkingManager {

    const val BASE_URL = "https://binaryjazz.us/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(DebugMenuNetworkInterceptor)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()
    val musicGeneratorService: MusicGenreGeneratorService = retrofit.create(
        MusicGenreGeneratorService::class.java
    )
}