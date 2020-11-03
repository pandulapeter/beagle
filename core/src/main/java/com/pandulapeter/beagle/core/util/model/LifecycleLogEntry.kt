package com.pandulapeter.beagle.core.util.model

import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.commonBase.randomId
import com.pandulapeter.beagle.modules.LifecycleLogListModule
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class LifecycleLogEntry(
    @Json(name = "name") val name: String,
    @Json(name = "simpleName") val simpleName: String,
    @Json(name = "eventType") val eventType: LifecycleLogListModule.EventType,
    @Json(name = "hasSavedInstanceState") val hasSavedInstanceState: Boolean?,
    @Json(name = "timestamp") val timestamp: Long = currentTimestamp
) : BeagleListItemContract {

    constructor(
        classType: Class<*>,
        eventType: LifecycleLogListModule.EventType,
        hasSavedInstanceState: Boolean?,
        timestamp: Long = currentTimestamp
    ) : this(
        name = classType.name,
        simpleName = classType.simpleName,
        eventType = eventType,
        hasSavedInstanceState = hasSavedInstanceState,
        timestamp = timestamp
    )

    override val id = randomId
    override val title = "${name}: ${eventType.name}".toText()

    fun getFormattedTitle(shouldDisplayFullNames: Boolean) = "${(if (shouldDisplayFullNames) name else simpleName)}: ${eventType.formattedName}".let {
        if (hasSavedInstanceState == null) it else "$it, savedInstanceState ${if (hasSavedInstanceState) "!=" else "="} null"
    }
}