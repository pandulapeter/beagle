package com.pandulapeter.beagle.appDemo.data

import androidx.annotation.StringRes
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.networking.ktor.KtorRemoteSongSource
import com.pandulapeter.beagle.appDemo.data.networking.retrofit.OkHttpRemoteSongSource

class SongRepository(
    private val okHttpRemoteSongSource: OkHttpRemoteSongSource,
    private val ktorRemoteSongSource: KtorRemoteSongSource
) {

    suspend fun getSong(id: String, engine: Engine) = when (engine) {
        Engine.OK_HTTP -> getSongUsingOkHttp(id)
        Engine.KTOR -> getSongUsingKtor(id)
    }

    suspend fun getLibrary(engine: Engine) = when (engine) {
        Engine.OK_HTTP -> getLibraryUsingOkHttp()
        Engine.KTOR -> getLibraryUsingKtor()
    }

    private suspend fun getSongUsingOkHttp(id: String) = okHttpRemoteSongSource.getSong(id)

    private suspend fun getSongUsingKtor(id: String) = ktorRemoteSongSource.getSong(id)

    private suspend fun getLibraryUsingOkHttp() = okHttpRemoteSongSource.getLibrary()

    private suspend fun getLibraryUsingKtor() = ktorRemoteSongSource.getLibrary()

    enum class Engine(@StringRes val nameResourceId: Int) {
        OK_HTTP(R.string.case_study_network_request_interceptor_engine_ok_http),
        KTOR(R.string.case_study_network_request_interceptor_engine_ktor);

        companion object {
            fun fromResourceId(@StringRes titleResourceId: Int?) = values().firstOrNull { it.nameResourceId == titleResourceId }
        }
    }
}