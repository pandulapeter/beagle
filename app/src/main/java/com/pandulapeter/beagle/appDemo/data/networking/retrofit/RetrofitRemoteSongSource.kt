package com.pandulapeter.beagle.appDemo.data.networking.retrofit

import com.pandulapeter.beagle.appDemo.data.model.Song
import com.pandulapeter.beagle.appDemo.data.networking.Constants
import com.pandulapeter.beagle.appDemo.data.networking.RemoteSongSource
import com.pandulapeter.beagle.logOkHttp.BeagleOkHttpLogger
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitRemoteSongSource : RemoteSongSource {

    private val songService: SongService = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(
            OkHttpClient.Builder()
                .apply { (BeagleOkHttpLogger.logger as? Interceptor?)?.let { addInterceptor(it) } }
                .build()
        )
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()
        .create(SongService::class.java)

    override suspend fun getSong(id: String): Song? = try {
        songService.getSongAsync(id)
    } catch (_: Exception) {
        null
    }
}