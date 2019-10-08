package com.pandulapeter.beagleExample.networking

import com.pandulapeter.beagle.BeagleNetworkInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkingManager {

    const val BASE_URL = "https://dog.ceo/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(BeagleNetworkInterceptor)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()
    val networkingService: NetworkingService = retrofit.create(
        NetworkingService::class.java
    )
}