package com.pandulapeter.beagle.appDemo.data

import com.pandulapeter.beagle.appDemo.data.networking.retrofit.SongRemoteSource

class SongRepository(
    private val songRemoteSource: SongRemoteSource,
) {
    suspend fun getSong(id: String) = songRemoteSource.getSong(id)

    suspend fun getLibrary() = songRemoteSource.getLibrary()
}