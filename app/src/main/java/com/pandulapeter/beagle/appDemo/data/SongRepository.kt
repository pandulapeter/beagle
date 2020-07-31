package com.pandulapeter.beagle.appDemo.data

import com.pandulapeter.beagle.appDemo.data.networking.ktor.KtorRemoteSongSource
import com.pandulapeter.beagle.appDemo.data.networking.retrofit.RetrofitRemoteSongSource

class SongRepository(
    private val retrofitRemoteSongSource: RetrofitRemoteSongSource,
    private val ktorRemoteSongSource: KtorRemoteSongSource
) {
    private val booleans = listOf(true, false)

    suspend fun getSong(id: String) = if (booleans.random()) getSongFromRetrofit(id) else getSongFromKtor(id)

    private suspend fun getSongFromRetrofit(id: String) = retrofitRemoteSongSource.getSong(id)

    private suspend fun getSongFromKtor(id: String) = ktorRemoteSongSource.getSong(id)
}