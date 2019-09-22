package com.pandulapeter.debugMenuCore.configuration.modules

/**
 * Displays an expandable list of test accounts. It is recommended to dynamically add and remove this module to make sure it only is visible on the relevant screens.
 * The class is generic to a representation of an account model which must implement the [AuthenticationHelperModule.Item] interface.
 * This module can only be added once.
 *
 * @param title - The text that appears near the switch. "Test accounts" by default.
 * @param accounts - The hardcoded list of test accounts.
 * @param onItemSelected - The callback that will be executed when a test account is selected.
 */
//TODO: Create a more generic implementation that also supports selection states.
data class AuthenticationHelperModule<T : AuthenticationHelperModule.Item>(
    override val title: String = "Test accounts",
    val accounts: List<T>,
    val onItemSelected: (itemId: String) -> Unit
) : ExpandableDebugMenuModule {

    override val id = ID

    /**
     * Represents a single item from the list (account).
     */
    interface Item {
        val id: String
        val name: String
    }

    companion object {
        const val ID = "authenticationHelper"
    }
}