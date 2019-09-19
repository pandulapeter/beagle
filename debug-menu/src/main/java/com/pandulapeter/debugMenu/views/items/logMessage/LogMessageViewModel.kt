package com.pandulapeter.debugMenu.views.items.logMessage

import com.pandulapeter.debugMenu.models.LogMessage
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.configuration.modules.LoggingModule
import java.text.SimpleDateFormat
import java.util.Locale

internal data class LogMessageViewModel(private val loggingModule: LoggingModule, val logMessage: LogMessage) : DrawerItem {

    override val id = "logMessage_${logMessage.id}"
    val timestamp = if (loggingModule.shouldShowTimestamp) SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(logMessage.timestamp) else null
    val message = logMessage.message
}