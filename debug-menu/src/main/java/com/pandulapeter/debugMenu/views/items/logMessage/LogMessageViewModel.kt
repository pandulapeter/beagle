package com.pandulapeter.debugMenu.views.items.logMessage

import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.modules.LoggingModule
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

internal data class LogMessageViewModel(private val loggingModule: LoggingModule, private val log: Pair<Long, String>) : DrawerItem {

    override val id = "logMessage_${UUID.randomUUID()}"
    val timestamp = if (loggingModule.shouldShowTimestamp) SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(log.first) else null
    val message = log.second
}