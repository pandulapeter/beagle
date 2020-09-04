package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.modules.LifecycleLogListModule
import java.util.UUID

internal data class LifecycleLogEntry(
    val classType: Class<*>,
    val eventType: LifecycleLogListModule.EventType,
    val hasSavedInstanceState: Boolean?,
    override val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis()
) : BeagleListItemContract {

    override val title = id

    fun getFormattedTitle(shouldDisplayFullNames: Boolean) = "${(if (shouldDisplayFullNames) classType.name else classType.simpleName)}: ${eventType.formattedName}".let {
        if (hasSavedInstanceState == null) it else "$it, savedInstanceState ${if (hasSavedInstanceState) "!=" else "="} null"
    }
}