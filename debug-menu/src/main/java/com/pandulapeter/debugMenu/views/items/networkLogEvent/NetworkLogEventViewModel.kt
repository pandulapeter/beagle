package com.pandulapeter.debugMenu.views.items.networkLogEvent

import com.pandulapeter.debugMenu.models.NetworkEvent
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.configuration.modules.NetworkLoggingModule
import java.text.SimpleDateFormat
import java.util.Locale

internal data class NetworkLogEventViewModel(private val networkLoggingModule: NetworkLoggingModule, val networkEvent: NetworkEvent) : DrawerItem {

    override val id = "networkLogMessage_${networkEvent.id}"
    val timestamp = if (networkLoggingModule.shouldShowTimestamp) SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(networkEvent.timestamp) else null
    val url = networkEvent.url.replace(networkLoggingModule.baseUrl, "")
    val isOutgoing = networkEvent.isOutgoing
}