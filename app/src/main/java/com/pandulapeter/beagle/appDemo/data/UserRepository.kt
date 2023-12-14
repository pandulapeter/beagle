package com.pandulapeter.beagle.appDemo.data

import com.pandulapeter.beagle.appDemo.data.networking.retrofit.UserRemoteSource

class UserRepository(
    private val userRemoteSource: UserRemoteSource,
) {
    suspend fun getAllUsers() = userRemoteSource.getAllUsers()

    suspend fun searchForUser(query: String) = userRemoteSource.searchForUser(query)
}