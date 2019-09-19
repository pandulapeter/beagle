package com.pandulapeter.debugMenuExample.networking

import retrofit2.Call
import retrofit2.http.GET

interface MusicGenreGeneratorService {

    @GET("/wp-json/genrenator/v1/genre")
    fun generateMusicGenre(): Call<String>
}