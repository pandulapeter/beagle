package com.pandulapeter.debugMenuExample

import com.pandulapeter.debugMenu.DebugMenuInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkingManager {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(DebugMenuInterceptor)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://binaryjazz.us/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()
    val musicGeneratorService: MusicGenreGeneratorService = retrofit.create(MusicGenreGeneratorService::class.java)
}