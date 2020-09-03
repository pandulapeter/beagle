package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.ID
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.ID
import com.pandulapeter.beagle.modules.LifecycleLogListModule.Companion.ID
import com.pandulapeter.beagle.modules.NetworkLogListModule.Companion.ID
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Logs Fragment and Activity lifecycle events. Useful for getting to know the navigational classes on a new project or debugging lifecycle / state restoration issues.
 * Note: In case of obfuscated builds, Fragment names will only appear properly if you choose to keep them in your ProGuard configuration. Use the following line:
 * -keepnames class * extends androidx.fragment.app.Fragment
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param title - The title of the module. "Lifecycle logs" by default.
 * @param maxItemCount - The maximum number of messages that will appear when expanded. 10 by default.
 * @param timestampFormatter - The formatter used for displaying the timestamp of each entry, or null if the timestamps should not be displayed at all. Formats with "HH:mm:ss" by default.
 * @param isExpandedInitially - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
 */
//TODO: Add support for filtering by type and displaying full package names.
data class LifecycleLogListModule(
    override val title: CharSequence = "Lifecycle logs",
    val maxItemCount: Int = 10,
    val timestampFormatter: ((Long) -> CharSequence)? = { defaultFormatter.format(it) },
    override val isExpandedInitially: Boolean = false
) : ExpandableModule<LifecycleLogListModule> {

    override val id = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        private val defaultFormatter by lazy { SimpleDateFormat(BeagleContract.LOG_TIME_FORMAT, Locale.ENGLISH) }

        const val ID = "lifecycleLogList"
    }
}