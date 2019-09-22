package com.pandulapeter.debugMenu

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.pandulapeter.debugMenu.dialogs.LogPayloadDialog
import com.pandulapeter.debugMenu.dialogs.NetworkEventBodyDialog
import com.pandulapeter.debugMenu.models.LogMessage
import com.pandulapeter.debugMenu.models.NetworkEvent
import com.pandulapeter.debugMenu.utils.BundleArgumentDelegate
import com.pandulapeter.debugMenu.utils.SimpleActivityLifecycleCallbacks
import com.pandulapeter.debugMenu.utils.getTextColor
import com.pandulapeter.debugMenu.utils.hideKeyboard
import com.pandulapeter.debugMenu.utils.setBackground
import com.pandulapeter.debugMenu.views.DebugMenuDrawer
import com.pandulapeter.debugMenu.views.DebugMenuDrawerLayout
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenu.views.items.listItem.ListItemViewModel
import com.pandulapeter.debugMenu.views.items.expandCollapseHeader.ExpandCollapseHeaderViewModel
import com.pandulapeter.debugMenu.views.items.header.HeaderViewModel
import com.pandulapeter.debugMenu.views.items.keylineOverlay.KeylineOverlayViewModel
import com.pandulapeter.debugMenu.views.items.logMessage.LogMessageViewModel
import com.pandulapeter.debugMenu.views.items.networkLogEvent.NetworkLogEventViewModel
import com.pandulapeter.debugMenu.views.items.settingsLink.SettingsLinkViewModel
import com.pandulapeter.debugMenuCore.configuration.UiConfiguration
import com.pandulapeter.debugMenuCore.configuration.modules.DebugMenuModule
import com.pandulapeter.debugMenuCore.configuration.modules.ExpandableDebugMenuModule
import com.pandulapeter.debugMenuCore.configuration.modules.HeaderModule
import com.pandulapeter.debugMenuCore.configuration.modules.ItemListModule
import com.pandulapeter.debugMenuCore.configuration.modules.KeylineOverlayModule
import com.pandulapeter.debugMenuCore.configuration.modules.LoggingModule
import com.pandulapeter.debugMenuCore.configuration.modules.NetworkLoggingModule
import com.pandulapeter.debugMenuCore.configuration.modules.SettingsLinkModule
import com.pandulapeter.debugMenuCore.contracts.DebugMenuContract

/**
 * The main singleton that handles the debug drawer's functionality.
 */
object DebugMenu : DebugMenuContract {

    //region Public API
    /**
     * Update this property at any time to change the module configuration of the drawer. Using the copy function of the data classes is recommended to avoid code duplication.
     */
    override var modules = emptyList<DebugMenuModule>()
        set(value) {
            field = value.distinctBy { it.id }.sortedBy { it !is HeaderModule }
            updateItems()
        }

    /**
     * Hooks up the library to the Application's lifecycle. After this is called, a debug drawer will be inserted into every activity. This should be called
     * in the Application's onCreate() method.
     *
     * @param application - The [Application] instance.
     * @param uiConfiguration - The [UiConfiguration] that specifies the appearance the drawer.
     */
    override fun attachToUi(application: Application, uiConfiguration: UiConfiguration) {
        this.uiConfiguration = uiConfiguration
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    /**
     * Tries to open the current Activity's debug drawer.
     *
     * @param activity - The current [Activity] instance.
     */
    override fun openDrawer(activity: Activity) {
        drawers[activity]?.run { (parent as? DebugMenuDrawerLayout?)?.openDrawer(this) }
    }

    /**
     * Tries to close the current Activity's debug drawer. For proper UX this should be used in onBackPressed() to block any other logic if it returns true.
     *
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
     *
     * @param message - The message that should be logged.
     * @param tag - An optional tag that can be later used for filtering. Null by default.
     * @param payload - An optional String payload that can be opened in a dialog when the user clicks on a log message. Null by default.
     */
    override fun log(message: String, tag: String?, payload: String?) {
        //TODO: Logs should be saved even if there is no loggingModule added.
        loggingModule?.run {
            logMessages = logMessages.toMutableList().apply { add(0, LogMessage(message = message, tag = tag, payload = payload)) }.take(maxMessageCount)
        }
    }
    //endregion

    //region Implementation details
    private var Bundle.isDrawerOpen by BundleArgumentDelegate.Boolean("isDrawerOpen")
    private var uiConfiguration = UiConfiguration()
    internal var textColor = Color.WHITE
        private set
    private val loggingModule get() = modules.filterIsInstance<LoggingModule>().firstOrNull()
    private val networkLoggingModule get() = modules.filterIsInstance<NetworkLoggingModule>().firstOrNull()
    private val keylineOverlayModule get() = modules.filterIsInstance<KeylineOverlayModule>().firstOrNull()
    private val drawers = mutableMapOf<Activity, DebugMenuDrawer>()
    private val expandCollapseStates = mutableMapOf<String, Boolean>()
    private var items = emptyList<DrawerItem>()
    private var isKeylineOverlayEnabled = false
        set(value) {
            if (field != value) {
                field = value
                updateItems()
                (if (value) keylineOverlayModule else null).let { keylineOverlayModule ->
                    drawers.values.forEach { drawer -> (drawer.parent as? DebugMenuDrawerLayout?)?.keylineOverlay = keylineOverlayModule }
                }
            }
        }
    private var logMessages = emptyList<LogMessage>()
        set(value) {
            field = value
            updateItems()
        }
    private var networkLogs = emptyList<NetworkEvent>()
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

    init {
        modules = listOf(
            HeaderModule(
                title = "DebugMenu",
                subtitle = "Version ${BuildConfig.VERSION_NAME}",
                text = "Configure the list of modules by changing the value of DebugMenu.modules.",
                shouldShowBuildDate = false,
                shouldShowBuildTime = false
            )
        )
    }

    internal fun logNetworkEvent(networkEvent: NetworkEvent) {
        //TODO: Network logs should be saved even if there is no networkLoggingModule added.
        networkLoggingModule?.run {
            networkLogs = networkLogs.toMutableList().apply { add(0, networkEvent) }.take(maxMessageCount)
        }
    }

    //TODO: Make sure this doesn't break Activity shared element transitions.
    private fun createAndAddDrawerLayout(activity: Activity, shouldOpenDrawer: Boolean) = DebugMenuDrawer(
        context = activity,
        onKeylineOverlaySwitchChanged = { isKeylineOverlayEnabled = !isKeylineOverlayEnabled },
        onExpandCollapseHeaderPressed = { id ->
            expandCollapseStates[id] = !(expandCollapseStates[id] ?: false)
            updateItems()
        },
        onListItemPressed = { itemListModule, itemId -> itemListModule.onItemSelected(itemId) },
        onNetworkLogEventClicked = { networkEvent -> activity.openNetworkEventBodyDialog(networkEvent) },
        onLogMessageClicked = { logMessage -> activity.openLogPayloadDialog(logMessage) }
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
                            keylineOverlay = keylineOverlayModule
                        }
                        addDrawerListener(object : DrawerLayout.DrawerListener {

                            override fun onDrawerStateChanged(newState: Int) = Unit

                            override fun onDrawerSlide(drawerView: View, slideOffset: Float) = activity.currentFocus?.hideKeyboard() ?: Unit

                            override fun onDrawerClosed(drawerView: View) = Unit

                            override fun onDrawerOpened(drawerView: View) = Unit
                        })
                    },
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }
    }

    private fun Activity.findRootViewGroup(): ViewGroup = findViewById(android.R.id.content) ?: window.decorView.findViewById(android.R.id.content)

    private fun Activity.openNetworkEventBodyDialog(networkEvent: NetworkEvent) {
        (this as? AppCompatActivity?)?.run {
            NetworkEventBodyDialog.show(
                fragmentManager = supportFragmentManager,
                networkEvent = networkEvent,
                uiConfiguration = uiConfiguration,
                shouldShowHeaders = networkLoggingModule?.shouldShowHeaders == true
            )
        } ?: throw IllegalArgumentException("This feature only works with AppCompatActivity")
    }

    private fun Activity.openLogPayloadDialog(logMessage: LogMessage) {
        (this as? AppCompatActivity?)?.run { LogPayloadDialog.show(supportFragmentManager, logMessage, uiConfiguration) }
            ?: throw IllegalArgumentException("This feature only works with AppCompatActivity")
    }

    private fun updateItems() {
        val items = mutableListOf<DrawerItem>()

        fun addExpandCollapseModule(module: ExpandableDebugMenuModule, shouldShowIcon: Boolean, addItems: () -> Unit) {
            items.add(
                ExpandCollapseHeaderViewModel(
                    id = module.id,
                    title = module.title,
                    isExpanded = expandCollapseStates[module.id] == true,
                    shouldShowIcon = shouldShowIcon
                )
            )
            if (expandCollapseStates[module.id] == true) {
                addItems()
            }

        }

        modules.forEach { module ->
            when (module) {
                is HeaderModule -> items.add(HeaderViewModel(module))
                is SettingsLinkModule -> items.add(SettingsLinkViewModel(module))
                is KeylineOverlayModule -> items.add(KeylineOverlayViewModel(module, isKeylineOverlayEnabled))
                is ItemListModule<*> -> addExpandCollapseModule(module, true) { items.addAll(module.items.map { ListItemViewModel(module, it) }) }
                is NetworkLoggingModule -> addExpandCollapseModule(module, networkLogs.isNotEmpty()) { items.addAll(networkLogs.map { NetworkLogEventViewModel(module, it) }) }
                is LoggingModule -> addExpandCollapseModule(module, logMessages.isNotEmpty()) { items.addAll(logMessages.map { LogMessageViewModel(module, it) }) }
            }
        }
        this.items = items
        drawers.values.forEach { it.updateItems(items) }
    }
    //endregion
}