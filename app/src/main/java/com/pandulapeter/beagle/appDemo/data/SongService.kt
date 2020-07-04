package com.pandulapeter.beagle.appDemo.data

import com.pandulapeter.beagle.appDemo.data.model.Song
import retrofit2.http.GET
import retrofit2.http.Query

interface SongService {

    @GET("song")
    suspend fun getSongAsync(@Query("id") songId: String): Song
}