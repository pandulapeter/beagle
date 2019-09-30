package com.pandulapeter.beagleCore.contracts

/**
 * Must be implemented by all modules that support the expand / collapse logic.
 */
interface BeagleExpandableModuleContract : BeagleModuleContract {

    val title: CharSequence
    val isInitiallyExpanded: Boolean
}