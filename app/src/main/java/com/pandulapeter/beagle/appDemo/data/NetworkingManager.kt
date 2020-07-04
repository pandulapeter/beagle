package com.pandulapeter.beagle.appDemo.data

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkingManager {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://campfire-test1.herokuapp.com/v1/")
        .client(
            OkHttpClient.Builder()
//TODO        .addInterceptor(BeagleNetworkInterceptor)
                .build()
        )
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()
    val songService: SongService = retrofit.create(
        SongService::class.java
    )
}