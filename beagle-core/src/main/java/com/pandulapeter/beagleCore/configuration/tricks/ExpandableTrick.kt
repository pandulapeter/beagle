package com.pandulapeter.beagleCore.configuration.tricks

/**
 * Must be implemented by all modules that support the expand / collapse logic.
 */
interface ExpandableTrick : Trick {

    val title: CharSequence
    val isInitiallyExpanded: Boolean
}

@Suppress("unused")
typealias ExpandableModule = ExpandableTrick