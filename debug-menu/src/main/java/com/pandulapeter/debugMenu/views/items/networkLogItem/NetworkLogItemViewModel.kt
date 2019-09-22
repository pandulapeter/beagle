package com.pandulapeter.debugMenu.views.items.networkLogItem

import com.pandulapeter.debugMenu.models.NetworkLogItem
import com.pandulapeter.debugMenu.views.items.DrawerItemViewModel
import com.pandulapeter.debugMenuCore.configuration.modules.NetworkLogListModule
import java.text.SimpleDateFormat
import java.util.Locale

internal data class NetworkLogItemViewModel(private val networkLogListModule: NetworkLogListModule, val networkLogItem: NetworkLogItem) : DrawerItemViewModel {

    override val id = "networkLogMessage_${networkLogItem.id}"
    val timestamp = if (networkLogListModule.shouldShowTimestamp) SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(networkLogItem.timestamp) else null
    val url = networkLogItem.url.replace(networkLogListModule.baseUrl, "")
    val isOutgoing = networkLogItem.isOutgoing
}