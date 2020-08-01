package com.pandulapeter.beagle.appDemo.data.networking.ktor

import com.pandulapeter.beagle.appDemo.data.model.Song
import com.pandulapeter.beagle.appDemo.data.networking.Constants
import com.pandulapeter.beagle.appDemo.data.networking.RemoteSongSource
import com.pandulapeter.beagle.logKtor.BeagleKtorLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.takeFrom

class KtorRemoteSongSource : RemoteSongSource {

    private val client = HttpClient(Android) {
        install(ForcedJsonFeature) {
            serializer = GsonSerializer()
        }
        (BeagleKtorLogger.logger as? HttpClientFeature<*, *>?)?.let { install(it) }
    }

    override suspend fun getSong(id: String): Song? = try {
        client.get<Song> {
            apiUrl(Constants.SONGS_ENDPOINT)
            parameter(Constants.PARAMETER_SONG_ID, id)
        }
    } catch (_: Exception) {
        null
    }

    private fun HttpRequestBuilder.apiUrl(path: String) {
        url {
            takeFrom(Constants.BASE_URL)
            encodedPath = path
        }
    }
}