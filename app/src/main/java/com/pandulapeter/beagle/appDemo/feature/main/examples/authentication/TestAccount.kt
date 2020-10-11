package com.pandulapeter.beagle.appDemo.feature.main.examples.authentication

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract

data class TestAccount(
    val email: String,
    val password: String
) : BeagleListItemContract {

    override val title = Text.CharSequence(email)
}