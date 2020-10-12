package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.ID
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.ID
import com.pandulapeter.beagle.modules.LifecycleLogListModule.Companion.DEFAULT_EVENT_TYPES
import com.pandulapeter.beagle.modules.LifecycleLogListModule.Companion.DEFAULT_IS_EXPANDED_INITIALLY
import com.pandulapeter.beagle.modules.LifecycleLogListModule.Companion.DEFAULT_MAX_ITEM_COUNT
import com.pandulapeter.beagle.modules.LifecycleLogListModule.Companion.DEFAULT_SHOULD_DISPLAY_FULL_NAMES
import com.pandulapeter.beagle.modules.LifecycleLogListModule.Companion.DEFAULT_TITLE
import com.pandulapeter.beagle.modules.LifecycleLogListModule.Companion.ID
import com.pandulapeter.beagle.modules.LifecycleLogListModule.EventType
import com.pandulapeter.beagle.modules.NetworkLogListModule.Companion.ID
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Logs Fragment and Activity lifecycle events. Useful for getting to know the navigational classes on a new project or debugging lifecycle / state restoration issues.
 * Note: In case of obfuscated builds, Fragment names will only appear properly if you choose to keep them in your ProGuard configuration. Use the following line:
 * -keepnames class * extends androidx.fragment.app.Fragment
 *
 * Activities and Fragments coming from Beagle will not appear in the logs.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param title - The title of the module. [DEFAULT_TITLE] by default.
 * @param eventTypes - The list of [EventType] values that the module should track. [DEFAULT_EVENT_TYPES] by default.
 * @param shouldDisplayFullNames - Whether or not displayed class names should include full package names. [DEFAULT_SHOULD_DISPLAY_FULL_NAMES] by default.
 * @param maxItemCount - The maximum number of messages that will appear when expanded. [DEFAULT_MAX_ITEM_COUNT] by default.
 * @param timestampFormatter - The formatter used for displaying the timestamp of each entry, or null if the timestamps should not be displayed at all. Formats with [BeagleContract.LOG_TIME_FORMAT] by default.
 * @param isExpandedInitially - Whether or not the list should be expanded when the drawer is opened for the first time. [DEFAULT_IS_EXPANDED_INITIALLY] by default.
 */
@Suppress("unused")
data class LifecycleLogListModule(
    val title: Text = Text.CharSequence(DEFAULT_TITLE),
    val eventTypes: List<EventType> = DEFAULT_EVENT_TYPES,
    val shouldDisplayFullNames: Boolean = DEFAULT_SHOULD_DISPLAY_FULL_NAMES,
    val maxItemCount: Int = DEFAULT_MAX_ITEM_COUNT,
    val timestampFormatter: ((Long) -> CharSequence)? = { DEFAULT_DATE_FORMAT.format(it) },
    override val isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY
) : ExpandableModule<LifecycleLogListModule> {

    override val id = ID

    override fun getHeaderTitle(beagle: BeagleContract) = title

    /**
     * Contains all event types for Fragments and Activities that can be tracked.
     */
    enum class EventType(val formattedName: String) {
        ON_CREATE("onCreate()"),
        ON_START("onStart()"),
        ON_RESUME("onResume()"),
        ON_SAVE_INSTANCE_STATE("onSaveInstanceState()"),
        ON_PAUSE("onPause()"),
        ON_STOP("onStop()"),
        ON_DESTROY("onDestroy()"),
        FRAGMENT_ON_ATTACH("onAttach()"),
        FRAGMENT_ON_ACTIVITY_CREATED("onActivityCreated()"),
        FRAGMENT_ON_VIEW_CREATED("onCreateView()"),
        FRAGMENT_ON_VIEW_DESTROYED("onDestroyView()"),
        FRAGMENT_ON_DETACH("onDetach()")
    }

    companion object {
        const val ID = "lifecycleLogList"
        private const val DEFAULT_TITLE = "Lifecycle logs"
        private val DEFAULT_EVENT_TYPES = EventType.values().toList()
        private const val DEFAULT_SHOULD_DISPLAY_FULL_NAMES = false
        private const val DEFAULT_MAX_ITEM_COUNT = 20
        private const val DEFAULT_IS_EXPANDED_INITIALLY = false
        private val DEFAULT_DATE_FORMAT by lazy { SimpleDateFormat(BeagleContract.LOG_TIME_FORMAT, Locale.ENGLISH) }
    }
}