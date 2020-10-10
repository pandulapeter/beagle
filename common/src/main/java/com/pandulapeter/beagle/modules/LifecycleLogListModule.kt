package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.ID
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.ID
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
 * @param title - The title of the module. "Lifecycle logs" by default.
 * @param eventTypes - The list of [EventType] values that the module should track. By default all event types are displayed.
 * @param shouldDisplayFullNames - Whether or not displayed class names should include full package names. False by default.
 * @param maxItemCount - The maximum number of messages that will appear when expanded. 10 by default.
 * @param timestampFormatter - The formatter used for displaying the timestamp of each entry, or null if the timestamps should not be displayed at all. Formats with "HH:mm:ss" by default.
 * @param isExpandedInitially - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
 */
data class LifecycleLogListModule(
    val title: Text = Text.CharSequence("Lifecycle logs"),
    val eventTypes: List<EventType> = EventType.values().toList(),
    val shouldDisplayFullNames: Boolean = false,
    val maxItemCount: Int = 10,
    val timestampFormatter: ((Long) -> CharSequence)? = { defaultFormatter.format(it) },
    override val isExpandedInitially: Boolean = false
) : ExpandableModule<LifecycleLogListModule> {

    override val id = ID

    override fun getInternalTitle(beagle: BeagleContract) = title

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

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
        private val defaultFormatter by lazy { SimpleDateFormat(BeagleContract.LOG_TIME_FORMAT, Locale.ENGLISH) }

        const val ID = "lifecycleLogList"
    }
}