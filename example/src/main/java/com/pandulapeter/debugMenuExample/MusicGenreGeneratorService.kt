package com.pandulapeter.debugMenuExample

import retrofit2.Call
import retrofit2.http.GET

interface MusicGenreGeneratorService {

    @GET("/wp-json/genrenator/v1/genre")
    fun generateMusicGenre(): Call<String>
}