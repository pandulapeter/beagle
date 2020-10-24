package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.modules.LifecycleLogListModule
import java.util.UUID

internal data class LifecycleLogEntry(
    val classType: Class<*>,
    val eventType: LifecycleLogListModule.EventType,
    val hasSavedInstanceState: Boolean?,
    val timestamp: Long = currentTimestamp
) : BeagleListItemContract {

    override val title = UUID.randomUUID().toString().toText()

    fun getFormattedTitle(shouldDisplayFullNames: Boolean) = "${(if (shouldDisplayFullNames) classType.name else classType.simpleName)}: ${eventType.formattedName}".let {
        if (hasSavedInstanceState == null) it else "$it, savedInstanceState ${if (hasSavedInstanceState) "!=" else "="} null"
    }
}