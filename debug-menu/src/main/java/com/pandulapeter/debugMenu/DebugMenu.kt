package com.pandulapeter.debugMenu

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import com.pandulapeter.debugMenu.models.LogMessage
import com.pandulapeter.debugMenu.models.NetworkEvent
import com.pandulapeter.debugMenu.utils.BundleArgumentDelegate
import com.pandulapeter.debugMenu.utils.SimpleActivityLifecycleCallbacks
import com.pandulapeter.debugMenu.views.DebugMenuDrawer
import com.pandulapeter.debugMenu.views.DebugMenuDrawerLayout
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenu.views.items.header.HeaderViewModel
import com.pandulapeter.debugMenu.views.items.logMessage.LogMessageViewModel
import com.pandulapeter.debugMenu.views.items.loggingHeader.LoggingHeaderViewModel
import com.pandulapeter.debugMenu.views.items.networkLogEvent.NetworkLogEventViewModel
import com.pandulapeter.debugMenu.views.items.networkLoggingHeader.NetworkLoggingHeaderViewModel
import com.pandulapeter.debugMenu.views.items.settingsLink.SettingsLinkViewModel
import com.pandulapeter.debugMenuCore.DebugMenuContract
import com.pandulapeter.debugMenuCore.ModuleConfiguration
import com.pandulapeter.debugMenuCore.UiConfiguration
import com.pandulapeter.debugMenuCore.modules.LoggingModule

/**
 * The main singleton that handles the debug drawer's functionality.
 */
object DebugMenu : DebugMenuContract {

    //region Public API
    /**
     * Update this field at any time to change the moodule configuration of the drawer. Using the copy function of the data class is recommended.
     */
    override var moduleConfiguration = ModuleConfiguration()
        set(value) {
            field = value
            updateItems()
        }

    /**
     * Hooks up the library to the Application's lifecycle. After this is called, a debug drawer will be inserted into every activity. This should be called
     * in the Application's onCreate() method.
     * @param application - The [Application] instance.
     * @param uiConfiguration - The [UiConfiguration] that specifies the appearance the drawer.
     * @param moduleConfiguration - The [ModuleConfiguration] that specifies the contents of the drawer.
     */
    override fun initialize(application: Application, uiConfiguration: UiConfiguration, moduleConfiguration: ModuleConfiguration) {
        this.uiConfiguration = uiConfiguration
        this.moduleConfiguration = moduleConfiguration
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    /**
     * Tries to open the current Activity's debug drawer.
     * @param activity - The current [Activity] instance.
     */
    override fun openDrawer(activity: Activity) {
        drawers[activity]?.run { (parent as? DebugMenuDrawerLayout?)?.openDrawer(this) }
    }

    /**
     * Tries to close the current Activity's debug drawer. For proper UX this should be used in onBackPressed() to block any other logic if it returns true.
     * @param activity - The current [Activity] instance.
     * @return - True if the drawer was open, false otherwise
     */
    override fun closeDrawer(activity: Activity): Boolean {
        val drawer = drawers[activity]
        val drawerLayout = drawer?.parent as? DebugMenuDrawerLayout?
        return (drawerLayout?.isDrawerOpen(drawer) == true).also {
            drawerLayout?.closeDrawers()
        }
    }

    /**
     * Adds a log message item which will be displayed at the top of the list if the [LoggingModule] is enabled.
     */
    override fun log(message: String) {
        moduleConfiguration.loggingModule?.run {
            logMessages = logMessages.toMutableList().apply { add(0, LogMessage(message = message)) }.take(maxMessageCount)
        }
    }
    //endregion

    //region Implementation details
    private var Bundle.isDrawerOpen by BundleArgumentDelegate.Boolean("isDrawerOpen")
    private val drawers = mutableMapOf<Activity, DebugMenuDrawer>()
    private var uiConfiguration = UiConfiguration()
    private var items = emptyList<DrawerItem>()
    private var logMessages = emptyList<LogMessage>()
        set(value) {
            field = value
            updateItems()
        }
    private var areLogMessagesExpanded = false
        set(value) {
            field = value
            updateItems()
        }
    private var networkLogs = emptyList<NetworkEvent>()
        set(value) {
            field = value
            updateItems()
        }
    private var areNetworkLogsExpanded = false
        set(value) {
            field = value
            updateItems()
        }
    private val lifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            drawers[activity] = createAndAddDrawerLayout(activity, savedInstanceState?.isDrawerOpen == true)
        }

        override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
            p1.isDrawerOpen = drawers[activity]?.let { drawer -> (drawer.parent as? DebugMenuDrawerLayout?)?.isDrawerOpen(drawer) } ?: false
        }

        override fun onActivityDestroyed(activity: Activity) {
            drawers.remove(activity)
        }
    }

    internal fun logNetworkEvent(networkEvent: NetworkEvent) {
        moduleConfiguration.networkLoggingModule?.run {
            networkLogs = networkLogs.toMutableList().apply { add(0, networkEvent) }.take(maxMessageCount)
        }
    }

    private fun createAndAddDrawerLayout(activity: Activity, shouldOpenDrawer: Boolean) = DebugMenuDrawer(
        context = activity,
        uiConfiguration = uiConfiguration,
        onLoggingHeaderPressed = { if (logMessages.isNotEmpty()) areLogMessagesExpanded = !areLogMessagesExpanded },
        onNetworkLoggingHeaderPressed = { if (networkLogs.isNotEmpty()) areNetworkLogsExpanded = !areNetworkLogsExpanded },
        onNetworkLogEventClicked = { Toast.makeText(activity, "Work in progress", Toast.LENGTH_SHORT).show() }
    ).also { drawer ->
        drawer.updateItems(items)
        activity.findRootViewGroup().run {
            post {
                val oldViews = (0 until childCount).map { getChildAt(it) }
                removeAllViews()
                addView(
                    DebugMenuDrawerLayout(
                        context = activity,
                        oldViews = oldViews,
                        drawer = drawer,
                        drawerWidth = uiConfiguration.drawerWidth
                    ).apply {
                        if (shouldOpenDrawer) {
                            openDrawer(drawer)
                        }
                    },
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }
    }

    private fun Activity.findRootViewGroup(): ViewGroup = findViewById(android.R.id.content) ?: window.decorView.findViewById(android.R.id.content)

    private fun updateItems() {
        val items = mutableListOf<DrawerItem>()

        // Set up Header module
        moduleConfiguration.headerModule?.let { headerModule -> items.add(HeaderViewModel(headerModule)) }

        // Set up SettingsLink module
        moduleConfiguration.settingsLinkModule?.let { settingsLinkModule -> items.add(SettingsLinkViewModel(settingsLinkModule)) }

        // Set up the NetworkLogging module
        moduleConfiguration.networkLoggingModule?.let { networkLoggingModule ->
            items.add(NetworkLoggingHeaderViewModel(networkLoggingModule, areNetworkLogsExpanded, networkLogs.isNotEmpty()))
            if (areNetworkLogsExpanded) {
                items.addAll(networkLogs.map { NetworkLogEventViewModel(networkLoggingModule, it) })
            }
        }

        // Set up the Logging module
        moduleConfiguration.loggingModule?.let { loggingModule ->
            items.add(LoggingHeaderViewModel(loggingModule, areLogMessagesExpanded, logMessages.isNotEmpty()))
            if (areLogMessagesExpanded) {
                items.addAll(logMessages.map { LogMessageViewModel(loggingModule, it) })
            }
        }

        // Update the UI
        this.items = items
        drawers.values.forEach { it.updateItems(items) }
    }
    //endregion
}