package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.ID
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.ID
import com.pandulapeter.beagle.modules.NetworkLogListModule.Companion.ID
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Displays an expandable list of historical network activity. The entries are clickable to inspect their JSON payloads.
 *
 * While you can use Beagle.logNetworkEvent() to push messages to this list, automatically intercepting the network requests is probably a better solution.
 * Check out this section for guidance: https://github.com/pandulapeter/beagle#intercepting-network-events
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param title - The title of the module. "Network activity" by default.
 * @param baseUrl - When not empty, all URL-s will have the specified String filtered out to reduce the amount of redundant information. Empty by default.
 * @param maxItemCount - The maximum number of messages that will appear when expanded. 10 by default.
 * @param maxItemTitleLength - The maximum number of characters displayed on a list item, or null for no limitation. Null by default.
 * @param timestampFormatter - The formatter used for displaying the timestamp of each entry, or null if the timestamps should not be displayed at all. Formats with "HH:mm:ss" by default.
 * @param isExpandedInitially - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
 */
data class NetworkLogListModule(
    override val title: CharSequence = "Network activity",
    val baseUrl: String = "",
    val maxItemCount: Int = 10,
    val maxItemTitleLength: Int? = null,
    val timestampFormatter: ((Long) -> CharSequence)? = { defaultFormatter.format(it) },
    override val isExpandedInitially: Boolean = false
) : ExpandableModule<NetworkLogListModule> {

    override val id: String = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        private val defaultFormatter by lazy { SimpleDateFormat(BeagleContract.LOG_TIME_FORMAT, Locale.ENGLISH) }

        const val ID = "networkLogList"
    }
}