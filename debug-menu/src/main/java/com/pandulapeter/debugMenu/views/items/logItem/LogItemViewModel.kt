package com.pandulapeter.debugMenu.views.items.logItem

import com.pandulapeter.debugMenu.models.LogItem
import com.pandulapeter.debugMenu.views.items.DrawerItemViewModel
import com.pandulapeter.debugMenuCore.configuration.modules.LogListModule
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