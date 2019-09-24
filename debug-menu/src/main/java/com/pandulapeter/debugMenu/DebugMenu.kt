package com.pandulapeter.debugMenu

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.ContextThemeWrapper
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
import com.pandulapeter.debugMenu.utils.hideKeyboard
import com.pandulapeter.debugMenu.views.DebugMenuDrawer
import com.pandulapeter.debugMenu.views.DebugMenuDrawerLayout
import com.pandulapeter.debugMenu.views.items.DrawerItemViewModel
import com.pandulapeter.debugMenu.views.items.button.ButtonViewModel
import com.pandulapeter.debugMenu.views.items.header.HeaderViewModel
import com.pandulapeter.debugMenu.views.items.listHeader.ListHeaderViewModel
import com.pandulapeter.debugMenu.views.items.listItem.ListItemViewModel
import com.pandulapeter.debugMenu.views.items.logItem.LogItemViewModel
import com.pandulapeter.debugMenu.views.items.longText.LongTextViewModel
import com.pandulapeter.debugMenu.views.items.networkLogItem.NetworkLogItemViewModel
import com.pandulapeter.debugMenu.views.items.singleSelectionListItem.SingleSelectionListItemViewModel
import com.pandulapeter.debugMenu.views.items.text.TextViewModel
import com.pandulapeter.debugMenu.views.items.toggle.ToggleViewModel
import com.pandulapeter.debugMenuCore.ModulePositioning
import com.pandulapeter.debugMenuCore.configuration.UiCustomization
import com.pandulapeter.debugMenuCore.configuration.modules.AppInfoButtonModule
import com.pandulapeter.debugMenuCore.configuration.modules.ButtonModule
import com.pandulapeter.debugMenuCore.configuration.modules.DebugMenuModule
import com.pandulapeter.debugMenuCore.configuration.modules.DebugMenuExpandableModule
import com.pandulapeter.debugMenuCore.configuration.modules.HeaderModule
import com.pandulapeter.debugMenuCore.configuration.modules.KeylineOverlayToggleModule
import com.pandulapeter.debugMenuCore.configuration.modules.ListModule
import com.pandulapeter.debugMenuCore.configuration.modules.LogListModule
import com.pandulapeter.debugMenuCore.configuration.modules.LongTextModule
import com.pandulapeter.debugMenuCore.configuration.modules.NetworkLogListModule
import com.pandulapeter.debugMenuCore.configuration.modules.ScreenshotButtonModule
import com.pandulapeter.debugMenuCore.configuration.modules.SingleSelectionListModule
import com.pandulapeter.debugMenuCore.configuration.modules.TextModule
import com.pandulapeter.debugMenuCore.configuration.modules.ToggleModule
import com.pandulapeter.debugMenuCore.contracts.DebugMenuContract
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * The main singleton that handles the debug drawer's functionality.
 */
@SuppressLint("StaticFieldLeak")
object DebugMenu : DebugMenuContract {

    /**
     * Use this flag to disable the debug drawer at runtime.
     */
    override var isEnabled = true
        set(value) {
            if (field != value) {
                field = value
                drawers.values.forEach { drawer ->
                    (drawer.parent as? DebugMenuDrawerLayout?)?.setDrawerLockMode(if (value) DrawerLayout.LOCK_MODE_UNDEFINED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }

    /**
     * Hooks up the library to the Application's lifecycle. After this is called, a debug drawer will be inserted into every activity. This should be called
     * in the Application's onCreate() method.
     *
     * @param application - The [Application] instance.
     * @param uiCustomization - The [UiCustomization] that specifies the appearance the drawer.
     */
    override fun attachToApplication(application: Application, uiCustomization: UiCustomization) {
        this.uiCustomization = uiCustomization
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    /**
     * Use this function to clear the contents of the menu and set a new list of modules.
     *
     * @param modules - The new list of modules.
     */
    override fun setModules(modules: List<DebugMenuModule>) {
        moduleList = modules
    }

    /**
     * Use this function to add a new module to the list. If there already is a module with the same ID, it will be updated.
     *
     * @param module - The new module to be added.
     * @param positioning - The positioning of the new module. [ModulePositioning.Bottom] by default.
     */
    override fun putModule(module: DebugMenuModule, positioning: ModulePositioning) {
        moduleList = moduleList.toMutableList().apply {
            indexOfFirst { it.id == module.id }.also { currentIndex ->
                if (currentIndex != -1) {
                    removeAt(currentIndex)
                    add(currentIndex, module)
                } else {
                    when (positioning) {
                        ModulePositioning.Bottom -> add(module)
                        ModulePositioning.Top -> add(0, module)
                        is ModulePositioning.Below -> {
                            indexOfFirst { it.id == positioning.id }.also { referencePosition ->
                                if (referencePosition == -1) {
                                    add(module)
                                } else {
                                    add(referencePosition + 1, module)
                                }
                            }
                        }
                        is ModulePositioning.Above -> {
                            indexOfFirst { it.id == positioning.id }.also { referencePosition ->
                                if (referencePosition == -1) {
                                    add(0, module)
                                } else {
                                    add(referencePosition, module)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes the module with the specified ID from the list of modules. The ID-s of unique modules can be accessed through their companion objects.
     *
     * @param id - The ID of the module to be removed.
     */
    override fun removeModule(id: String) {
        moduleList = moduleList.filterNot { it.id == id }
    }

    /**
     * Tries to open the current Activity's debug drawer.
     *
     * @param activity - The current [Activity] instance.
     */
    override fun openDrawer(activity: Activity) {
        if (isEnabled) {
            drawers[activity]?.run { (parent as? DebugMenuDrawerLayout?)?.openDrawer(this) }
        }
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
     * @param tag - An optional tag that can be later used for filtering. Null by default. //TODO: Implement filtering by tag.
     * @param payload - An optional String payload that can be opened in a dialog when the user clicks on a log message. Null by default.
     */
    override fun log(message: String, tag: String?, payload: String?) {
        logItems = logItems.toMutableList().apply { add(0, LogItem(message = message, tag = tag, payload = payload)) }.take(MAX_ITEM_COUNT)
    }
    //endregion

    //region Implementation details
    private var Bundle.isDrawerOpen by BundleArgumentDelegate.Boolean("isDrawerOpen")
    private const val MAX_ITEM_COUNT = 500
    private var uiCustomization = UiCustomization()
    private var currentJob: CoroutineContext? = null
    private var moduleList = emptyList<DebugMenuModule>()
        set(value) {
            field = value.distinctBy { it.id }.sortedBy { it !is HeaderModule }
            updateItems()
        }
    private val keylineOverlayToggleModule get() = moduleList.filterIsInstance<KeylineOverlayToggleModule>().firstOrNull()
    private val drawers = mutableMapOf<Activity, DebugMenuDrawer>()
    private val expandCollapseStates = mutableMapOf<String, Boolean>()
    private val toggles = mutableMapOf<String, Boolean>()
    private val singleSelectionListStates = mutableMapOf<String, String>()
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
    private var logItems = emptyList<LogItem>()
        set(value) {
            field = value
            updateItems()
        }
    private var networkLogItems = emptyList<NetworkLogItem>()
        set(value) {
            field = value
            updateItems()
        }
    //TODO: Should not leak. Test it to make sure.
    private var currentActivity: Activity? = null
    private val lifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
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
        moduleList = listOf(
            HeaderModule(
                title = "DebugMenu",
                subtitle = "Version ${BuildConfig.VERSION_NAME}",
                text = "Configure the list of modules by setting the value of DebugMenu.modules."
            )
        )
    }

    internal fun logNetworkEvent(networkLogItem: NetworkLogItem) {
        networkLogItems = networkLogItems.toMutableList().apply { add(0, networkLogItem) }.take(MAX_ITEM_COUNT)
    }

    //TODO: Make sure this doesn't break Activity shared element transitions.
    //TODO: Find a smart way to handle the case when the root view is already a DrawerLayout.
    private fun createAndAddDrawerLayout(activity: Activity, shouldOpenDrawer: Boolean) =
        (uiCustomization.themeResourceId?.let { ContextThemeWrapper(activity, it) } ?: activity).let { themedContext ->
            DebugMenuDrawer(themedContext).also { drawer ->
                drawer.updateItems(items)
                activity.findRootViewGroup().run {
                    post {
                        val oldViews = (0 until childCount).map { getChildAt(it) }
                        removeAllViews()
                        addView(
                            DebugMenuDrawerLayout(
                                context = themedContext,
                                oldViews = oldViews,
                                drawer = drawer,
                                drawerWidth = uiCustomization.drawerWidth
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
        }

    private fun Activity.findRootViewGroup(): ViewGroup = findViewById(android.R.id.content) ?: window.decorView.findViewById(android.R.id.content)

    private fun Activity.openNetworkEventBodyDialog(networkLogItem: NetworkLogItem, shouldShowHeaders: Boolean) {
        (this as? AppCompatActivity?)?.run {
            NetworkEventBodyDialog.show(
                fragmentManager = supportFragmentManager,
                networkLogItem = networkLogItem,
                uiCustomization = uiCustomization,
                shouldShowHeaders = shouldShowHeaders
            )
        } ?: throw IllegalArgumentException("This feature only works with AppCompatActivity")
    }

    private fun Activity.openLogPayloadDialog(logItem: LogItem) {
        (this as? AppCompatActivity?)?.run { LogPayloadDialog.show(supportFragmentManager, logItem, uiCustomization) }
            ?: throw IllegalArgumentException("This feature only works with AppCompatActivity")
    }

    private fun updateItems() {
        currentJob?.cancel()
        currentJob = GlobalScope.launch {
            val items = mutableListOf<DrawerItemViewModel>()

            //TODO: DiffUtil seems to rebind the expanded list items even if they didn't change.
            fun addListModule(module: DebugMenuExpandableModule, shouldShowIcon: Boolean, addItems: () -> List<DrawerItemViewModel>) {
                items.add(
                    ListHeaderViewModel(
                        id = module.id,
                        title = module.title,
                        isExpanded = expandCollapseStates[module.id] ?: module.isInitiallyExpanded,
                        shouldShowIcon = shouldShowIcon,
                        onItemSelected = {
                            expandCollapseStates[module.id] = !(expandCollapseStates[module.id] ?: module.isInitiallyExpanded)
                            updateItems()
                        }
                    )
                )
                if (expandCollapseStates[module.id] ?: module.isInitiallyExpanded) {
                    items.addAll(addItems().distinctBy { it.id })
                }
            }

            moduleList.forEach { module ->
                when (module) {
                    is TextModule -> items.add(
                        TextViewModel(
                            id = module.id,
                            text = module.text,
                            isTitle = module.isTitle
                        )
                    )
                    is LongTextModule -> addListModule(
                        module = module,
                        shouldShowIcon = true,
                        addItems = {
                            listOf(
                                LongTextViewModel(
                                    id = "longText_${module.id}",
                                    text = module.text
                                )
                            )
                        }
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
                            shouldUseListItem = uiCustomization.shouldUseItemsInsteadOfButtons,
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
                    is SingleSelectionListModule<*> -> addListModule(
                        module = module,
                        shouldShowIcon = module.items.isNotEmpty(),
                        addItems = {
                            module.items.map { item ->
                                SingleSelectionListItemViewModel(
                                    listModuleId = module.id,
                                    item = item,
                                    isSelected = (singleSelectionListStates[module.id] ?: module.initialSelectionId) == item.id,
                                    onItemSelected = { itemId ->
                                        singleSelectionListStates[module.id] = itemId
                                        updateItems()
                                        module.invokeItemSelectedCallback(itemId)
                                    }
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
                            shouldUseListItem = uiCustomization.shouldUseItemsInsteadOfButtons,
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
                    is ScreenshotButtonModule -> items.add(
                        ButtonViewModel(
                            id = module.id,
                            shouldUseListItem = uiCustomization.shouldUseItemsInsteadOfButtons,
                            text = module.text,
                            onButtonPressed = { (drawers[currentActivity]?.parent as? DebugMenuDrawerLayout?)?.takeAndShareScreenshot() }
                        )
                    )
                    is NetworkLogListModule -> networkLogItems.take(module.maxItemCount).let { networkLogItems ->
                        addListModule(
                            module = module,
                            shouldShowIcon = networkLogItems.isNotEmpty(),
                            addItems = {
                                networkLogItems.map { networkLogItem ->
                                    NetworkLogItemViewModel(
                                        networkLogListModule = module,
                                        networkLogItem = networkLogItem,
                                        onItemSelected = { currentActivity?.openNetworkEventBodyDialog(networkLogItem, module.shouldShowHeaders) }
                                    )
                                }
                            }
                        )
                    }
                    is LogListModule -> logItems.take(module.maxItemCount).let { logItems ->
                        addListModule(
                            module = module,
                            shouldShowIcon = logItems.isNotEmpty(),
                            addItems = {
                                logItems.map { logItem ->
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
            }
            this@DebugMenu.items = items
            drawers.values.forEach { it.updateItems(items) }
            currentJob = null
        }
    }
    //endregion
}