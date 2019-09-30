package com.pandulapeter.beagle.views.items.networkLogItem

import com.pandulapeter.beagle.models.NetworkLogItem
import com.pandulapeter.beagle.views.items.DrawerItemViewModel
import com.pandulapeter.beagleCore.configuration.modules.NetworkLogListModule
import java.text.SimpleDateFormat
import java.util.Locale

internal data class NetworkLogItemViewModel(
    private val networkLogListModule: NetworkLogListModule,
    val networkLogItem: NetworkLogItem,
    val onItemSelected: () -> Unit
) : DrawerItemViewModel {

    override val id = "networkLogMessage_${networkLogItem.id}"
    val timestamp = if (networkLogListModule.shouldShowTimestamp) SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(networkLogItem.timestamp) else null
    val url = networkLogItem.url.replace(networkLogListModule.baseUrl, "")
    val isOutgoing = networkLogItem.isOutgoing
}