package com.pandulapeter.beagle.appDemo.data.networking.retrofit

import com.pandulapeter.beagle.appDemo.data.model.User
import com.pandulapeter.beagle.appDemo.data.networking.Constants
import com.pandulapeter.beagle.logOkHttp.BeagleOkHttpLogger
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class UserRemoteSource {

    private val userService: UserService = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(
            OkHttpClient.Builder()
                .apply { (BeagleOkHttpLogger.logger as? Interceptor?)?.let { addInterceptor(it) } }
                .build()
        )
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()
        .create(UserService::class.java)

    suspend fun getAllUsers(): List<User>? = try {
        userService.getAllUsers().users
    } catch (_: Exception) {
        null
    }

    suspend fun searchForUser(query: String) = try {
        userService.searchForUser(query).users
    } catch (_: Exception) {
        null
    }
}