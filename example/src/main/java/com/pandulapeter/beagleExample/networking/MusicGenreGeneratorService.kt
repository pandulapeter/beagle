package com.pandulapeter.beagleExample.networking

import retrofit2.Call
import retrofit2.http.GET

interface MusicGenreGeneratorService {

    @GET("/api/breeds/list/all")
    fun generateMusicGenre(): Call<String>
}