package com.pandulapeter.beagleExample.networking

import retrofit2.Call
import retrofit2.http.GET

@Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
interface NetworkingService {

    @GET("/api/breeds/list/all")
    fun performNetworkRequest(): Call<String>
}