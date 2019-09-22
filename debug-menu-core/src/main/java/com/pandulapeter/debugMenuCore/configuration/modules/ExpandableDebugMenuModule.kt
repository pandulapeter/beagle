package com.pandulapeter.debugMenuCore.configuration.modules

/**
 * Must be implemented by all modules that support the expand / collapse logic.
 */
interface ExpandableDebugMenuModule : DebugMenuModule {

    val title: CharSequence
}