package com.pandulapeter.beagle.appDemo.data.networking.retrofit

import com.pandulapeter.beagle.appDemo.data.model.UserWrapper
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {

    @GET("users")
    suspend fun getAllUsers(): UserWrapper

    @GET("users/search")
    suspend fun searchForUser(@Query("q") query: String): UserWrapper
}