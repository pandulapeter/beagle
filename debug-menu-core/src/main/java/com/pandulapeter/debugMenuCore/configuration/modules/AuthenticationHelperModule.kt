package com.pandulapeter.debugMenuCore.configuration.modules

/**
 * Displays an expandable list of test accounts. It is recommended to dynamically add and remove this module to make sure it only is visible on the relevant screens.
 * This module can only be added once.
 *
 * @param title - The text that appears near the switch. "Test accounts" by default.
 * @param accounts - The hardcoded list of test accounts.
 * @param onAccountSelected - The callback that will be executed when a test account is selected. It should submit the information on the UI.
 */
//TODO: Create a more generic implementation that also supports selection states.
data class AuthenticationHelperModule(
    val title: String = "Test accounts",
    val accounts: List<Pair<String, String>>,
    val onAccountSelected: (account: Pair<String, String>) -> Unit
) : DebugMenuModule {

    override val id = ID

    companion object {
        const val ID = "authenticationHelper"
    }
}