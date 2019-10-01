package com.pandulapeter.beagle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.pandulapeter.beagle.dialogs.LogPayloadDialog
import com.pandulapeter.beagle.dialogs.NetworkEventBodyDialog
import com.pandulapeter.beagle.models.LogItem
import com.pandulapeter.beagle.models.NetworkLogItem
import com.pandulapeter.beagle.utils.BundleArgumentDelegate
import com.pandulapeter.beagle.utils.SimpleActivityLifecycleCallbacks
import com.pandulapeter.beagle.utils.hideKeyboard
import com.pandulapeter.beagle.utils.mapToViewModels
import com.pandulapeter.beagle.views.BeagleDrawer
import com.pandulapeter.beagle.views.BeagleDrawerLayout
import com.pandulapeter.beagle.views.items.DrawerItemViewModel
import com.pandulapeter.beagleCore.configuration.Appearance
import com.pandulapeter.beagleCore.configuration.Positioning
import com.pandulapeter.beagleCore.configuration.Trick
import com.pandulapeter.beagleCore.contracts.BeagleContract
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * The main singleton that handles the debug drawer's functionality.
 */
@Suppress("StaticFieldLeak")
object Beagle : BeagleContract {

    /**
     * Use this flag to disable the debug drawer at runtime.
     */
    override var isEnabled = true
        set(value) {
            if (field != value) {
                field = value
                drawers.values.forEach { drawer ->
                    (drawer.parent as? BeagleDrawerLayout?)?.setDrawerLockMode(if (value) DrawerLayout.LOCK_MODE_UNDEFINED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }

    /**
     * Hooks up the library to the Application's lifecycle. After this is called, a debug drawer will be inserted into every activity. This should be called
     * in the Application's onCreate() method.
     *
     * @param application - The [Application] instance.
     * @param appearance - The [Appearance] that specifies the appearance the drawer.
     */
    override fun imprint(application: Application, appearance: Appearance) {
        this.appearance = appearance
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    /**
     * Use this function to clear the contents of the menu and set a new list of tricks.
     *
     * @param tricks - The new list of tricks.
     */
    override fun learn(tricks: List<Trick>) {
        moduleList = tricks
    }

    /**
     * Use this function to add a new trick to the list. If there already is a trick with the same ID, it will be updated.
     *
     * @param trick - The new trick to be added.
     * @param positioning - The positioning of the new trick. [Positioning.Bottom] by default.
     */
    override fun learn(trick: Trick, positioning: Positioning) {
        moduleList = moduleList.toMutableList().apply {
            indexOfFirst { it.id == trick.id }.also { currentIndex ->
                if (currentIndex != -1) {
                    removeAt(currentIndex)
                    add(currentIndex, trick)
                } else {
                    when (positioning) {
                        Positioning.Bottom -> add(trick)
                        Positioning.Top -> add(0, trick)
                        is Positioning.Below -> {
                            indexOfFirst { it.id == positioning.id }.also { referencePosition ->
                                if (referencePosition == -1) {
                                    add(trick)
                                } else {
                                    add(referencePosition + 1, trick)
                                }
                            }
                        }
                        is Positioning.Above -> {
                            indexOfFirst { it.id == positioning.id }.also { referencePosition ->
                                if (referencePosition == -1) {
                                    add(0, trick)
                                } else {
                                    add(referencePosition, trick)
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
    override fun forget(id: String) {
        moduleList = moduleList.filterNot { it.id == id }
    }

    /**
     * Tries to open the current Activity's debug drawer.
     *
     * @param activity - The current [Activity] instance.
     */
    override fun fetch(activity: Activity) {
        if (isEnabled) {
            drawers[activity]?.run { (parent as? BeagleDrawerLayout?)?.openDrawer(this) }
        }
    }

    /**
     * Tries to close the current Activity's debug drawer. For proper UX this should be used in onBackPressed() to block any other logic if it returns true.
     *
     * @param activity - The current [Activity] instance.
     * @return - True if the drawer was open, false otherwise
     */
    override fun dismiss(activity: Activity): Boolean {
        val drawer = drawers[activity]
        val drawerLayout = drawer?.parent as? BeagleDrawerLayout?
        return (drawerLayout?.isDrawerOpen(drawer) == true).also {
            drawerLayout?.closeDrawers()
        }
    }

    /**
     * Adds a log message item which will be displayed at the top of the list in the [Trick.LogList] module.
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
    private var appearance = Appearance()
    private var currentJob: CoroutineContext? = null
    private var moduleList = emptyList<Trick>()
        set(value) {
            field = value.distinctBy { it.id }.sortedBy { it !is Trick.Header }
            updateItems()
        }
    private val keylineOverlayToggleModule get() = moduleList.filterIsInstance<Trick.KeylineOverlayToggle>().firstOrNull()
    internal val drawers = mutableMapOf<Activity, BeagleDrawer>()
    private var items = emptyList<DrawerItemViewModel>()
    internal var isKeylineOverlayEnabled = false
        set(value) {
            if (field != value) {
                field = value
                updateItems()
                (if (value) keylineOverlayToggleModule else null).let { keylineOverlayModule ->
                    drawers.values.forEach { drawer -> (drawer.parent as? BeagleDrawerLayout?)?.keylineOverlay = keylineOverlayModule }
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
    internal var currentActivity: Activity? = null
        private set
    private val lifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            drawers[activity] = createAndAddDrawerLayout(activity, savedInstanceState?.isDrawerOpen == true)
        }

        override fun onActivityResumed(activity: Activity) {
            super.onActivityResumed(activity)
            currentActivity = activity
        }

        override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
            p1.isDrawerOpen = drawers[activity]?.let { drawer -> (drawer.parent as? BeagleDrawerLayout?)?.isDrawerOpen(drawer) } ?: false
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
            Trick.Header(
                title = "Beagle",
                subtitle = "Version ${BuildConfig.VERSION_NAME}",
                text = "Configure the list of modules by setting the value of Beagle.modules."
            )
        )
    }

    internal fun logNetworkEvent(networkLogItem: NetworkLogItem) {
        networkLogItems = networkLogItems.toMutableList().apply { add(0, networkLogItem) }.take(MAX_ITEM_COUNT)
    }

    //TODO: Make sure this doesn't break Activity shared element transitions.
    //TODO: Find a smart way to handle the case when the root view is already a DrawerLayout.
    private fun createAndAddDrawerLayout(activity: Activity, shouldOpenDrawer: Boolean) =
        (appearance.themeResourceId?.let { ContextThemeWrapper(activity, it) } ?: activity).let { themedContext ->
            BeagleDrawer(themedContext).also { drawer ->
                drawer.updateItems(items)
                activity.findRootViewGroup().run {
                    post {
                        val oldViews = (0 until childCount).map { getChildAt(it) }
                        removeAllViews()
                        addView(
                            BeagleDrawerLayout(
                                context = themedContext,
                                oldViews = oldViews,
                                drawer = drawer,
                                drawerWidth = appearance.drawerWidth
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

    internal fun openNetworkEventBodyDialog(networkLogItem: NetworkLogItem, shouldShowHeaders: Boolean) {
        (currentActivity as? AppCompatActivity?)?.run {
            NetworkEventBodyDialog.show(
                fragmentManager = supportFragmentManager,
                networkLogItem = networkLogItem,
                appearance = appearance,
                shouldShowHeaders = shouldShowHeaders
            )
        } ?: throw IllegalArgumentException("This feature only works with AppCompatActivity")
    }

    internal fun openLogPayloadDialog(logItem: LogItem) {
        (currentActivity as? AppCompatActivity?)?.run { LogPayloadDialog.show(supportFragmentManager, logItem, appearance) }
            ?: throw IllegalArgumentException("This feature only works with AppCompatActivity")
    }

    internal fun updateItems() {
        currentJob?.cancel()
        currentJob = GlobalScope.launch {
            items = moduleList.mapToViewModels(
                appearance = appearance,
                networkLogItems = networkLogItems,
                logItems = logItems
            ).also { items ->
                drawers.values.forEach { it.updateItems(items) }
            }
            currentJob = null
        }
    }
    //endregion
}