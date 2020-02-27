package com.pandulapeter.beagle

import android.app.Activity
import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.pandulapeter.beagle.models.LogItem
import com.pandulapeter.beagle.models.NetworkLogItem
import com.pandulapeter.beagle.utils.BundleArgumentDelegate
import com.pandulapeter.beagle.utils.SimpleActivityLifecycleCallbacks
import com.pandulapeter.beagle.utils.findRootViewGroup
import com.pandulapeter.beagle.utils.hideKeyboard
import com.pandulapeter.beagle.utils.mapToViewModels
import com.pandulapeter.beagle.views.BeagleDialogFragment
import com.pandulapeter.beagle.views.BeagleDrawer
import com.pandulapeter.beagle.views.BeagleDrawerLayout
import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel
import com.pandulapeter.beagleCore.configuration.Appearance
import com.pandulapeter.beagleCore.configuration.Positioning
import com.pandulapeter.beagleCore.configuration.Trick
import com.pandulapeter.beagleCore.configuration.TriggerGesture
import com.pandulapeter.beagleCore.contracts.BeagleContract
import com.pandulapeter.beagleCore.contracts.BeagleListener
import kotlin.math.abs
import kotlin.math.max


/**
 * The main singleton that handles the debug drawer's functionality.
 */
@Suppress("StaticFieldLeak")
object Beagle : BeagleContract, SensorEventListener {

    //region Public API
    /**
     * Use this flag to disable the debug drawer at runtime.
     */
    override var isEnabled = true
        set(value) {
            if (field != value) {
                field = value
                updateDrawerLockMode()
            }
        }

    /**
     * Can be used to access the most recently resumed Activity instance.
     */
    override var currentActivity: Activity? = null
        private set

    /**
     * Hooks up the library to the Application's lifecycle. After this is called, a debug drawer will be inserted into every activity. This should be called
     * in the Application's onCreate() method.
     *
     * @param application - The [Application] instance.
     * @param packageName - Tha base package name of the application. Beagle will only work in Activities that are under this package. If not specified, an educated guess will be made (that won't work if your setup includes product flavors for example).
     * @param triggerGesture - Specifies the way the drawer can be opened. [TriggerGesture.SWIPE_AND_SHAKE] by default.
     * @param appearance - The [Appearance] that specifies the appearance the drawer. Optional.
     */
    override fun imprint(application: Application, packageName: String?, triggerGesture: TriggerGesture, appearance: Appearance) {
        this.appearance = appearance
        this.triggerGesture = triggerGesture
        this.packageName = packageName ?: application.packageName.split(".").run { take(max(size - 1, 1)).joinToString(".") }
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
        if (triggerGesture == TriggerGesture.SWIPE_AND_SHAKE || triggerGesture == TriggerGesture.SHAKE_ONLY) {
            (application.getSystemService(Context.SENSOR_SERVICE) as? SensorManager?)?.run {
                registerListener(this@Beagle, getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
            }
        }
    }

    /**
     * Use this function to clear the contents of the menu and set a new list of tricks.
     *
     * @param tricks - The new list of tricks.
     */
    override fun learn(vararg tricks: Trick) {
        moduleList = tricks.toList()
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
            notifyListenersOnDragStarted()
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
     * @param tag - An optional tag that can be later used for filtering. Null by default.
     * @param payload - An optional String payload that can be opened in a dialog when the user clicks on a log message. Null by default.
     */
    override fun log(message: String, tag: String?, payload: String?) {
        logItems = logItems.toMutableList().apply { add(0, LogItem(message = message, tag = tag, payload = payload)) }
    }

    /**
     * Registers a [BeagleListener] implementation.
     */
    override fun addListener(listener: BeagleListener) {
        listeners.add(listener)
    }

    /**
     * Removes the specified [BeagleListener] implementation.
     */
    override fun removeListener(listener: BeagleListener) {
        listeners.remove(listener)
    }

    /**
     * Removes all [BeagleListener] implementations.
     */
    override fun removeAllListeners() = listeners.clear()
    //endregion

    //region Implementation details
    private var Bundle.isDrawerOpen by BundleArgumentDelegate.Boolean("isDrawerOpen")
    private var appearance = Appearance()
    private var moduleList = emptyList<Trick>()
        set(value) {
            field = value.distinctBy { it.id }.sortedBy { it !is Trick.Header }
            updateItems()
        }
    private val keylineOverlayToggleModule get() = moduleList.filterIsInstance<Trick.KeylineOverlayToggle>().firstOrNull()
    private val viewBoundsOverlayToggleModule get() = moduleList.filterIsInstance<Trick.ViewBoundsOverlayToggle>().firstOrNull()
    internal val drawers = mutableMapOf<Activity, BeagleDrawer>()
    private var packageName = ""
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
    internal var isViewBoundsOverlayEnabled = false
        set(value) {
            if (field != value) {
                field = value
                updateItems()
                (if (value) viewBoundsOverlayToggleModule else null).let { viewBoundsOverlayToggleModule ->
                    drawers.values.forEach { drawer -> (drawer.parent as? BeagleDrawerLayout?)?.viewBoundsOverlay = viewBoundsOverlayToggleModule }
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
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            currentActivity?.run { dismiss(this) }
        }
    }
    private var triggerGesture = TriggerGesture.SWIPE_AND_SHAKE
    private var lastSensorUpdate = 0L
    private var lastSensorValues = Triple(0f, 0f, 0f)
    private val lifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            // The check is added to make sure Beagle is not injected into activities that come from other libraries (LeakCanary, Google sign-in / In app purchase) where it causes crashes.
            if (activity.componentName.className.startsWith(packageName)) {
                drawers[activity] = createAndAddDrawerLayout(activity, savedInstanceState?.isDrawerOpen == true)
                (activity as? AppCompatActivity?)?.onBackPressedDispatcher?.addCallback(activity, onBackPressedCallback)
            }
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
    private val listeners = mutableListOf<BeagleListener>()

    init {
        moduleList = listOf(
            Trick.Header(
                title = "Beagle",
                subtitle = "Version ${BuildConfig.VERSION_NAME}",
                text = "Configure the list of modules by calling Beagle.learn()."
            )
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        if (triggerGesture == TriggerGesture.SWIPE_AND_SHAKE || triggerGesture == TriggerGesture.SHAKE_ONLY) {
            if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastSensorUpdate > 100) {
                    val diffTime = currentTime - lastSensorUpdate
                    lastSensorUpdate = currentTime
                    val newSensorValues = Triple(event.values[0], event.values[1], event.values[2])
                    val speed =
                        abs(newSensorValues.first + newSensorValues.second + newSensorValues.third - lastSensorValues.first - lastSensorValues.second - lastSensorValues.third) / diffTime * 10000
                    if (speed > SHAKE_THRESHOLD) {
                        currentActivity?.also { fetch(it) }
                    }
                    lastSensorValues = newSensorValues
                }
            }
        }
    }

    internal fun logNetworkEvent(networkLogItem: NetworkLogItem) {
        networkLogItems = networkLogItems.toMutableList().apply { add(0, networkLogItem) }
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
                                updateDrawerLockMode()
                                post { updateDrawerLockMode() }
                                if (isKeylineOverlayEnabled) {
                                    keylineOverlay = keylineOverlayToggleModule
                                }
                                if (isViewBoundsOverlayEnabled) {
                                    viewBoundsOverlay = viewBoundsOverlayToggleModule
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

    internal fun openNetworkEventBodyDialog(networkLogItem: NetworkLogItem, shouldShowHeaders: Boolean) {
        (currentActivity as? AppCompatActivity?)?.run {
            val content = if (shouldShowHeaders) {
                "Headers:\n" + (if (networkLogItem.headers.isEmpty()) "No headers" else networkLogItem.headers.joinToString("\n")) + "\n\nBody:\n" + networkLogItem.body.let {
                    if (it.isEmpty() || it.isBlank()) "No data" else it
                }
            } else {
                networkLogItem.body.let {
                    if (it.isEmpty() || it.isBlank()) "No data" else it
                }
            }
            BeagleDialogFragment.show(
                fragmentManager = supportFragmentManager,
                title = networkLogItem.url,
                content = content,
                appearance = appearance,
                shouldWrapContent = false
            )
        } ?: throw IllegalArgumentException("This feature only works with AppCompatActivity")
    }

    internal fun openLogPayloadDialog(logItem: LogItem) {
        (currentActivity as? AppCompatActivity?)?.run {
            BeagleDialogFragment.show(
                fragmentManager = supportFragmentManager,
                title = logItem.message,
                content = logItem.payload ?: "",
                appearance = appearance,
                shouldWrapContent = true
            )
        } ?: throw IllegalArgumentException("This feature only works with AppCompatActivity")
    }

    internal fun updateItems() {
        items = moduleList.mapToViewModels(
            appearance = appearance,
            networkLogItems = networkLogItems,
            logItems = logItems
        ).also { items ->
            drawers.values.forEach { it.updateItems(items) }
        }
    }

    internal fun notifyListenersOnDragStarted() = listeners.forEach { it.onDrawerDragStarted() }

    internal fun notifyListenersOnOpened() {
        onBackPressedCallback.isEnabled = true
        drawers.values.forEach { drawer -> (drawer.parent as? BeagleDrawerLayout?)?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED) }
        listeners.forEach { it.onDrawerOpened() }
    }

    internal fun notifyListenersOnClosed() {
        onBackPressedCallback.isEnabled = false
        updateDrawerLockMode()
        listeners.forEach { it.onDrawerClosed() }
    }

    private fun updateDrawerLockMode() = drawers.values.forEach { drawer ->
        (drawer.parent as? BeagleDrawerLayout?)?.setDrawerLockMode(if (isEnabled && (triggerGesture == TriggerGesture.SWIPE_ONLY || triggerGesture == TriggerGesture.SWIPE_AND_SHAKE)) DrawerLayout.LOCK_MODE_UNDEFINED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private const val SHAKE_THRESHOLD = 1200
    //endregion
}