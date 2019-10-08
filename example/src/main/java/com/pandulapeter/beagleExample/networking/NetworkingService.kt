package com.pandulapeter.beagleExample.networking

import retrofit2.Call
import retrofit2.http.GET

interface NetworkingService {

    @GET("/api/breeds/list/all")
    fun performNetworkRequest(): Call<String>
}