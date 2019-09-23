package com.pandulapeter.debugMenu

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.pandulapeter.debugMenu.dialogs.LogPayloadDialog
import com.pandulapeter.debugMenu.dialogs.NetworkEventBodyDialog
import com.pandulapeter.debugMenu.models.LogItem
import com.pandulapeter.debugMenu.models.NetworkLogItem
import com.pandulapeter.debugMenu.utils.BundleArgumentDelegate
import com.pandulapeter.debugMenu.utils.SimpleActivityLifecycleCallbacks
import com.pandulapeter.debugMenu.utils.getTextColor
import com.pandulapeter.debugMenu.utils.hideKeyboard
import com.pandulapeter.debugMenu.utils.setBackground
import com.pandulapeter.debugMenu.views.DebugMenuDrawer
import com.pandulapeter.debugMenu.views.DebugMenuDrawerLayout
import com.pandulapeter.debugMenu.views.items.DrawerItemViewModel
import com.pandulapeter.debugMenu.views.items.button.ButtonViewModel
import com.pandulapeter.debugMenu.views.items.header.HeaderViewModel
import com.pandulapeter.debugMenu.views.items.listHeader.ListHeaderViewModel
import com.pandulapeter.debugMenu.views.items.listItem.ListItemViewModel
import com.pandulapeter.debugMenu.views.items.logItem.LogItemViewModel
import com.pandulapeter.debugMenu.views.items.networkLogItem.NetworkLogItemViewModel
import com.pandulapeter.debugMenu.views.items.text.TextViewModel
import com.pandulapeter.debugMenu.views.items.toggle.ToggleViewModel
import com.pandulapeter.debugMenuCore.configuration.UiConfiguration
import com.pandulapeter.debugMenuCore.configuration.modules.AppInfoButtonModule
import com.pandulapeter.debugMenuCore.configuration.modules.ButtonModule
import com.pandulapeter.debugMenuCore.configuration.modules.DebugMenuModule
import com.pandulapeter.debugMenuCore.configuration.modules.ExpandableDebugMenuModule
import com.pandulapeter.debugMenuCore.configuration.modules.HeaderModule
import com.pandulapeter.debugMenuCore.configuration.modules.KeylineOverlayToggleModule
import com.pandulapeter.debugMenuCore.configuration.modules.ListModule
import com.pandulapeter.debugMenuCore.configuration.modules.LogListModule
import com.pandulapeter.debugMenuCore.configuration.modules.NetworkLogListModule
import com.pandulapeter.debugMenuCore.configuration.modules.TextModule
import com.pandulapeter.debugMenuCore.configuration.modules.ToggleModule
import com.pandulapeter.debugMenuCore.contracts.DebugMenuContract

/**
 * The main singleton that handles the debug drawer's functionality.
 */
@SuppressLint("StaticFieldLeak")
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
     * Adds a log message item which will be displayed at the top of the list if the [LogListModule] is enabled.
     *
     * @param message - The message that should be logged.
     * @param tag - An optional tag that can be later used for filtering. Null by default.
     * @param payload - An optional String payload that can be opened in a dialog when the user clicks on a log message. Null by default.
     */
    override fun log(message: String, tag: String?, payload: String?) {
        //TODO: Logs should be saved even if there is no loggingModule added.
        loggingModule?.run {
            logMessages = logMessages.toMutableList().apply { add(0, LogItem(message = message, tag = tag, payload = payload)) }.take(maxMessageCount)
        }
    }
    //endregion

    //region Implementation details
    private var Bundle.isDrawerOpen by BundleArgumentDelegate.Boolean("isDrawerOpen")
    private var uiConfiguration = UiConfiguration()
    internal var textColor = Color.WHITE
        private set
    private val loggingModule get() = modules.filterIsInstance<LogListModule>().firstOrNull()
    private val networkLogListModule get() = modules.filterIsInstance<NetworkLogListModule>().firstOrNull()
    private val keylineOverlayToggleModule get() = modules.filterIsInstance<KeylineOverlayToggleModule>().firstOrNull()
    private val drawers = mutableMapOf<Activity, DebugMenuDrawer>()
    private val expandCollapseStates = mutableMapOf<String, Boolean>()
    private val toggles = mutableMapOf<String, Boolean>()
    private var items = emptyList<DrawerItemViewModel>()
    private var isKeylineOverlayEnabled = false
        set(value) {
            if (field != value) {
                field = value
                updateItems()
                (if (value) keylineOverlayToggleModule else null).let { keylineOverlayModule ->
                    drawers.values.forEach { drawer -> (drawer.parent as? DebugMenuDrawerLayout?)?.keylineOverlay = keylineOverlayModule }
                }
            }
        }
    private var logMessages = emptyList<LogItem>()
        set(value) {
            field = value
            updateItems()
        }
    private var networkLogs = emptyList<NetworkLogItem>()
        set(value) {
            field = value
            updateItems()
        }
    //TODO: Should not leak. Test it to make sure.
    private var currentActivity: Activity? = null
    private val lifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            textColor = activity.getTextColor(uiConfiguration)
            drawers[activity] = createAndAddDrawerLayout(activity, savedInstanceState?.isDrawerOpen == true)
        }

        override fun onActivityResumed(activity: Activity) {
            super.onActivityResumed(activity)
            currentActivity = activity
        }

        override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
            p1.isDrawerOpen = drawers[activity]?.let { drawer -> (drawer.parent as? DebugMenuDrawerLayout?)?.isDrawerOpen(drawer) } ?: false
        }

        override fun onActivityDestroyed(activity: Activity) {
            drawers.remove(activity)
            if (activity == currentActivity) {
                currentActivity = null
            }
        }
    }

    init {
        modules = listOf(
            HeaderModule(
                title = "DebugMenu",
                subtitle = "Version ${BuildConfig.VERSION_NAME}",
                text = "Configure the list of modules by setting the value of DebugMenu.modules."
            )
        )
    }

    internal fun logNetworkEvent(networkLogItem: NetworkLogItem) {
        //TODO: Network logs should be saved even if there is no networkLogListModule added.
        networkLogListModule?.run {
            networkLogs = networkLogs.toMutableList().apply { add(0, networkLogItem) }.take(maxMessageCount)
        }
    }

    //TODO: Make sure this doesn't break Activity shared element transitions.
    private fun createAndAddDrawerLayout(activity: Activity, shouldOpenDrawer: Boolean) = DebugMenuDrawer(activity).also { drawer ->
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
                            keylineOverlay = keylineOverlayToggleModule
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

    private fun Activity.openNetworkEventBodyDialog(networkLogItem: NetworkLogItem) {
        (this as? AppCompatActivity?)?.run {
            NetworkEventBodyDialog.show(
                fragmentManager = supportFragmentManager,
                networkLogItem = networkLogItem,
                uiConfiguration = uiConfiguration,
                shouldShowHeaders = networkLogListModule?.shouldShowHeaders == true
            )
        } ?: throw IllegalArgumentException("This feature only works with AppCompatActivity")
    }

    private fun Activity.openLogPayloadDialog(logItem: LogItem) {
        (this as? AppCompatActivity?)?.run { LogPayloadDialog.show(supportFragmentManager, logItem, uiConfiguration) }
            ?: throw IllegalArgumentException("This feature only works with AppCompatActivity")
    }

    private fun updateItems() {
        val items = mutableListOf<DrawerItemViewModel>()

        fun addListModule(module: ExpandableDebugMenuModule, shouldShowIcon: Boolean, addItems: () -> List<DrawerItemViewModel>) {
            items.add(
                ListHeaderViewModel(
                    id = module.id,
                    title = module.title,
                    isExpanded = expandCollapseStates[module.id] == true,
                    shouldShowIcon = shouldShowIcon,
                    onItemSelected = {
                        expandCollapseStates[module.id] = !(expandCollapseStates[module.id] ?: false)
                        updateItems()
                    }
                )
            )
            if (expandCollapseStates[module.id] == true) {
                items.addAll(addItems())
            }
        }

        modules.forEach { module ->
            when (module) {
                is TextModule -> items.add(
                    TextViewModel(
                        id = module.id,
                        text = module.text
                    )
                )
                is ToggleModule -> items.add(
                    ToggleViewModel(
                        id = module.id,
                        title = module.title,
                        isEnabled = toggles[module.id] ?: module.initialValue,
                        onToggleStateChanged = { newValue ->
                            if (toggles[module.id] != newValue) {
                                module.onValueChanged(newValue)
                                toggles[module.id] = newValue
                            }
                        })
                )
                is ButtonModule -> items.add(
                    ButtonViewModel(
                        id = module.id,
                        text = module.text,
                        onButtonPressed = module.onButtonPressed
                    )
                )
                is ListModule<*> -> addListModule(
                    module = module,
                    shouldShowIcon = module.items.isNotEmpty(),
                    addItems = {
                        module.items.map { item ->
                            ListItemViewModel(
                                listModuleId = module.id,
                                item = item,
                                onItemSelected = { module.invokeItemSelectedCallback(item.id) }
                            )
                        }
                    }
                )
                is HeaderModule -> items.add(
                    HeaderViewModel(
                        headerModule = module
                    )
                )
                is KeylineOverlayToggleModule -> items.add(
                    ToggleViewModel(
                        id = module.id,
                        title = module.title,
                        isEnabled = isKeylineOverlayEnabled,
                        onToggleStateChanged = { newValue -> isKeylineOverlayEnabled = newValue })
                )
                is AppInfoButtonModule -> items.add(
                    ButtonViewModel(
                        id = module.id,
                        text = module.text,
                        onButtonPressed = {
                            currentActivity?.run {
                                startActivity(Intent().apply {
                                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                    data = Uri.fromParts("package", packageName, null)
                                })
                            }
                        })
                )
                is NetworkLogListModule -> addListModule(
                    module = module,
                    shouldShowIcon = networkLogs.isNotEmpty(),
                    addItems = {
                        networkLogs.map { networkLogItem ->
                            NetworkLogItemViewModel(
                                networkLogListModule = module,
                                networkLogItem = networkLogItem,
                                onItemSelected = { currentActivity?.openNetworkEventBodyDialog(networkLogItem) }
                            )
                        }
                    }
                )
                is LogListModule -> addListModule(
                    module = module,
                    shouldShowIcon = logMessages.isNotEmpty(),
                    addItems = {
                        logMessages.map { logItem ->
                            LogItemViewModel(
                                logListModule = module,
                                logItem = logItem,
                                onItemSelected = { currentActivity?.openLogPayloadDialog(logItem) }
                            )
                        }
                    }
                )
            }
        }
        this.items = items
        drawers.values.forEach { it.updateItems(items) }
    }
    //endregion
}