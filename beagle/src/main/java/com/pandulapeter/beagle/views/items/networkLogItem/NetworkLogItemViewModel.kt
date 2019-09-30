package com.pandulapeter.beagle.views.items.networkLogItem

import com.pandulapeter.beagle.models.NetworkLogItem
import com.pandulapeter.beagle.views.items.DrawerItemViewModel
import com.pandulapeter.beagleCore.configuration.Trick
import java.text.SimpleDateFormat
import java.util.Locale

internal data class NetworkLogItemViewModel(
    private val networkLogListTrick: Trick.NetworkLogList,
    val networkLogItem: NetworkLogItem,
    val onItemSelected: () -> Unit
) : DrawerItemViewModel {

    override val id = "networkLogMessage_${networkLogItem.id}"
    val timestamp = if (networkLogListTrick.shouldShowTimestamp) SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(networkLogItem.timestamp) else null
    val url = networkLogItem.url.replace(networkLogListTrick.baseUrl, "")
    val isOutgoing = networkLogItem.isOutgoing
}