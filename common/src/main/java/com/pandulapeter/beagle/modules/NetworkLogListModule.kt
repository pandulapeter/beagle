package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.commonBase.LOG_TIME_FORMAT
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.ID
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.ID
import com.pandulapeter.beagle.modules.NetworkLogListModule.Companion.DEFAULT_BASE_URL
import com.pandulapeter.beagle.modules.NetworkLogListModule.Companion.DEFAULT_IS_EXPANDED_INITIALLY
import com.pandulapeter.beagle.modules.NetworkLogListModule.Companion.DEFAULT_MAX_ITEM_COUNT
import com.pandulapeter.beagle.modules.NetworkLogListModule.Companion.DEFAULT_TITLE
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
 * @param title - The title of the module. [DEFAULT_TITLE] by default.
 * @param baseUrl - When not empty, all URL-s will have the specified String filtered out to reduce the amount of redundant information. [DEFAULT_BASE_URL] by default.
 * @param maxItemCount - The maximum number of messages that will appear when expanded. [DEFAULT_MAX_ITEM_COUNT] by default.
 * @param timestampFormatter - The formatter used for displaying the timestamp of each entry, or null if the timestamps should not be displayed at all. Formats with [LOG_TIME_FORMAT] by default.
 * @param isExpandedInitially - Whether or not the list should be expanded when the drawer is opened for the first time. [DEFAULT_IS_EXPANDED_INITIALLY] by default.
 */
@Suppress("unused")
data class NetworkLogListModule(
    val title: Text = Text.CharSequence(DEFAULT_TITLE),
    val baseUrl: String = DEFAULT_BASE_URL,
    val maxItemCount: Int = DEFAULT_MAX_ITEM_COUNT,
    val timestampFormatter: ((timestamp: Long) -> CharSequence)? = { DEFAULT_DATE_FORMAT.format(it) },
    override val isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY
) : ExpandableModule<NetworkLogListModule> {

    override val id: String = ID

    override fun getHeaderTitle(beagle: BeagleContract) = title

    companion object {
        const val ID = "networkLogList"
        private const val DEFAULT_TITLE = "Network activity"
        private const val DEFAULT_BASE_URL = ""
        private const val DEFAULT_MAX_ITEM_COUNT = 10
        private val DEFAULT_MAX_ITEM_TITLE_LENGTH: Int? = null
        private val DEFAULT_DATE_FORMAT by lazy { SimpleDateFormat(LOG_TIME_FORMAT, Locale.ENGLISH) }
        private const val DEFAULT_IS_EXPANDED_INITIALLY = false
    }
}