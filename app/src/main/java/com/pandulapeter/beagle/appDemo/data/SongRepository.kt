package com.pandulapeter.beagle.appDemo.data

import com.pandulapeter.beagle.appDemo.data.networking.ktor.KtorRemoteSongSource
import com.pandulapeter.beagle.appDemo.data.networking.retrofit.RetrofitRemoteSongSource

class SongRepository(
    private val retrofitRemoteSongSource: RetrofitRemoteSongSource,
    private val ktorRemoteSongSource: KtorRemoteSongSource
) {
    private val booleans = listOf(true, false)

    suspend fun getSong(id: String) = if (booleans.random()) getSongUsingRetrofit(id) else getSongUsingKtor(id)

    private suspend fun getSongUsingRetrofit(id: String) = retrofitRemoteSongSource.getSong(id)

    private suspend fun getSongUsingKtor(id: String) = ktorRemoteSongSource.getSong(id)
}