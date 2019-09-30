package com.pandulapeter.beagleCore.configuration.tricks

/**
 * Must be implemented by all modules.
 */
interface Trick {

    val id: String
}

typealias Module = Trick