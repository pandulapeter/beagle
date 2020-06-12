package com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract

data class TestAccount(
    val email: String,
    val password: String
) : BeagleListItemContract {

    override val id = email
    override val title = email
}