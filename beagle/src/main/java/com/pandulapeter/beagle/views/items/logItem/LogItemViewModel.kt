package com.pandulapeter.beagle.views.items.logItem

import com.pandulapeter.beagle.models.LogItem
import com.pandulapeter.beagle.views.items.DrawerItemViewModel
import com.pandulapeter.beagleCore.configuration.modules.LogListModule
import java.text.SimpleDateFormat
import java.util.Locale

internal data class LogItemViewModel(
    private val logListModule: LogListModule,
    val logItem: LogItem,
    val onItemSelected: () -> Unit
) : DrawerItemViewModel {

    override val id = "logMessage_${logItem.id}"
    val timestamp = if (logListModule.shouldShowTimestamp) SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(logItem.timestamp) else null
    val message = logItem.message
    val hasPayload = logItem.payload != null
}