package com.pandulapeter.debugMenu

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import com.pandulapeter.debugMenu.models.LogMessage
import com.pandulapeter.debugMenu.models.NetworkEvent
import com.pandulapeter.debugMenu.utils.BundleArgumentDelegate
import com.pandulapeter.debugMenu.utils.SimpleActivityLifecycleCallbacks
import com.pandulapeter.debugMenu.utils.getTextColor
import com.pandulapeter.debugMenu.utils.setBackground
import com.pandulapeter.debugMenu.views.DebugMenuDrawer
import com.pandulapeter.debugMenu.views.DebugMenuDrawerLayout
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenu.views.items.authenticationHelperHeader.AuthenticationHelperHeaderViewModel
import com.pandulapeter.debugMenu.views.items.authenticationHelperItem.AuthenticationHelperItemViewModel
import com.pandulapeter.debugMenu.views.items.header.HeaderViewModel
import com.pandulapeter.debugMenu.views.items.keylineOverlay.KeylineOverlayViewModel
import com.pandulapeter.debugMenu.views.items.logMessage.LogMessageViewModel
import com.pandulapeter.debugMenu.views.items.loggingHeader.LoggingHeaderViewModel
import com.pandulapeter.debugMenu.views.items.networkLogEvent.NetworkLogEventViewModel
import com.pandulapeter.debugMenu.views.items.networkLoggingHeader.NetworkLoggingHeaderViewModel
import com.pandulapeter.debugMenu.views.items.settingsLink.SettingsLinkViewModel
import com.pandulapeter.debugMenuCore.configuration.ModuleConfiguration
import com.pandulapeter.debugMenuCore.configuration.UiConfiguration
import com.pandulapeter.debugMenuCore.configuration.modules.LoggingModule
import com.pandulapeter.debugMenuCore.contracts.DebugMenuContract

/**
 * The main singleton that handles the debug drawer's functionality.
 */
object DebugMenu : DebugMenuContract {

    //region Public API
    /**
     * Update this property at any time to change the module configuration of the drawer. Using the copy function of the data classes is recommended to avoid code duplication.
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
     * @param message - The message that should be logged.
     * @param tag - An optional tag that can be later used for filtering. Null by default.
     * @param payload - An optional String payload that can be opened in a dialog when the user clicks on a log message. Null by default.
     */
    override fun log(message: String, tag: String?, payload: String?) {
        moduleConfiguration.loggingModule?.run {
            logMessages = logMessages.toMutableList().apply { add(0, LogMessage(message = message, tag = tag, payload = payload)) }.take(maxMessageCount)
        }
    }
    //endregion

    //region Implementation details
    private var Bundle.isDrawerOpen by BundleArgumentDelegate.Boolean("isDrawerOpen")
    private var uiConfiguration = UiConfiguration()
    internal var textColor = Color.WHITE
        private set
    private val drawers = mutableMapOf<Activity, DebugMenuDrawer>()
    private var items = emptyList<DrawerItem>()
    private var isKeylineOverlayEnabled = false
        set(value) {
            if (field != value) {
                field = value
                updateItems()
                (if (value) moduleConfiguration.keylineOverlayModule else null).let { keylineOverlayModule ->
                    drawers.values.forEach { drawer -> (drawer.parent as? DebugMenuDrawerLayout?)?.keylineOverlay = keylineOverlayModule }
                }
            }
        }
    private var areTestAccountsExpanded = false
        set(value) {
            field = value
            updateItems()
        }
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
            textColor = activity.getTextColor(uiConfiguration)
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
        onKeylineOverlaySwitchChanged = { isKeylineOverlayEnabled = !isKeylineOverlayEnabled },
        onAuthenticationHelperHeaderPressed = { areTestAccountsExpanded = !areTestAccountsExpanded },
        onAuthenticationHelperItemClicked = { account -> moduleConfiguration.authenticationHelperModule?.onAccountSelected?.invoke(account) },
        onNetworkLoggingHeaderPressed = { if (networkLogs.isNotEmpty()) areNetworkLogsExpanded = !areNetworkLogsExpanded },
        onNetworkLogEventClicked = { Toast.makeText(activity, "Work in progress", Toast.LENGTH_SHORT).show() },
        onLoggingHeaderPressed = { if (logMessages.isNotEmpty()) areLogMessagesExpanded = !areLogMessagesExpanded }
    ).also { drawer ->
        drawer.setBackground(uiConfiguration)
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
                        if (isKeylineOverlayEnabled) {
                            keylineOverlay = moduleConfiguration.keylineOverlayModule
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

        // Set up the KeylineOverlay module
        moduleConfiguration.keylineOverlayModule?.let { keylineOverlayModule -> items.add(KeylineOverlayViewModel(keylineOverlayModule, isKeylineOverlayEnabled)) }

        // Set up the AuthenticationHelper module
        moduleConfiguration.authenticationHelperModule?.let { authenticationHelperModule ->
            items.add(AuthenticationHelperHeaderViewModel(authenticationHelperModule, areTestAccountsExpanded))
            if (areTestAccountsExpanded) {
                items.addAll(authenticationHelperModule.accounts.map { AuthenticationHelperItemViewModel(it) })
            }
        }

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