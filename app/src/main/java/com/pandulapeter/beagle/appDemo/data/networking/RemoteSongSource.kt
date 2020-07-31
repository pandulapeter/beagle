package com.pandulapeter.beagle.appDemo.data.networking

import com.pandulapeter.beagle.appDemo.data.model.Song

interface RemoteSongSource {

    suspend fun getSong(id: String): Song?
}