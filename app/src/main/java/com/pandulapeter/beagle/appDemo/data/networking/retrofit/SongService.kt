package com.pandulapeter.beagle.appDemo.data.networking.retrofit

import com.pandulapeter.beagle.appDemo.data.model.Song
import com.pandulapeter.beagle.appDemo.data.networking.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface SongService {

    @GET(Constants.SONGS_ENDPOINT)
    suspend fun getSongAsync(@Query(Constants.PARAMETER_SONG_ID) songId: String): Song
}