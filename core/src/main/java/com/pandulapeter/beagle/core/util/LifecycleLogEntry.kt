package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.modules.LifecycleLogListModule
import java.util.UUID

internal data class LifecycleLogEntry(
    val classType: Class<*>,
    val eventType: LifecycleLogListModule.EventType,
    val hasSavedInstanceState: Boolean?,
    val timestamp: Long = currentTimestamp
) : BeagleListItemContract {

    override val title = Text.CharSequence(UUID.randomUUID().toString())

    fun getFormattedTitle(shouldDisplayFullNames: Boolean) = "${(if (shouldDisplayFullNames) classType.name else classType.simpleName)}: ${eventType.formattedName}".let {
        if (hasSavedInstanceState == null) it else "$it, savedInstanceState ${if (hasSavedInstanceState) "!=" else "="} null"
    }
}