package com.pandulapeter.debugMenu.views.items.networkLogEvent

import com.pandulapeter.debugMenu.utils.NetworkEvent
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.modules.NetworkLoggingModule
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

internal data class NetworkLogEventViewModel(private val networkLoggingModule: NetworkLoggingModule, val networkEvent: NetworkEvent) : DrawerItem {

    override val id = "networkLogMessage_${UUID.randomUUID()}"
    val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(networkEvent.timestamp)
    val url = networkEvent.url
    val isOutgoing = networkEvent.isOutgoing
}