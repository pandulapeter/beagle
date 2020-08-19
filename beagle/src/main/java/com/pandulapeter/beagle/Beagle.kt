package com.pandulapeter.beagle

import android.animation.ValueAnimator
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.pandulapeter.beagle.models.LogItem
import com.pandulapeter.beagle.models.NetworkLogItem
import com.pandulapeter.beagle.utils.BundleArgumentDelegate
import com.pandulapeter.beagle.utils.SimpleActivityLifecycleCallbacks
import com.pandulapeter.beagle.utils.findRootViewGroup
import com.pandulapeter.beagle.utils.hideKeyboard
import com.pandulapeter.beagle.utils.mapToViewModels
import com.pandulapeter.beagle.utils.takeAndShareScreenshot
import com.pandulapeter.beagle.views.BeagleDialogFragment
import com.pandulapeter.beagle.views.BeagleDrawer
import com.pandulapeter.beagle.views.BeagleDrawerLayout
import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel
import com.pandulapeter.beagleCore.configuration.Appearance
import com.pandulapeter.beagleCore.configuration.Behavior
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
@Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
@Suppress("StaticFieldLeak", "unused", "DeprecatedCallableAddReplaceWith")
object Beagle : BeagleContract, SensorEventListener {

    //region Public API
    /**
     * Use this flag to disable the debug drawer at runtime.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
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
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override var currentActivity: Activity? = null
        private set

    /**
     * Can be used to verify if any of the tricks have pending changes (returns whether or not the "Apply" button is visible).
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override val hasPendingChanges get() = Trick.hasPendingChanges

    /**
     * Will be called after the user presses the "Apply" button and the callbacks for all pending changes have been invoked.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override var onAllChangesApplied: (() -> Unit)?
        get() = Trick.onAllChangesApplied
        set(value) {
            Trick.onAllChangesApplied = value
        }

    /**
     * Hooks up the library to the Application's lifecycle. After this is called, a debug drawer will be inserted into every activity. This should be called
     * in the Application's onCreate() method.
     *
     * @param application - The [Application] instance.
     * @param appearance - The [Appearance] that specifies the appearance of the drawer. Optional.
     * @param behavior - The [Behavior] that specifies the behavior of the drawer. Optional.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override fun imprint(
        application: Application,
        appearance: Appearance,
        behavior: Behavior
    ) {
        Trick.clearPendingChanges()
        this.appearance = appearance
        this.behavior = behavior
        this.packageName = behavior.packageName ?: application.packageName.split(".").run { take(max(size - 1, 1)).joinToString(".") }
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
        if (behavior.triggerGesture == TriggerGesture.SWIPE_AND_SHAKE || behavior.triggerGesture == TriggerGesture.SHAKE_ONLY) {
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
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override fun learn(vararg tricks: Trick) {
        Trick.clearPendingChanges()
        moduleList = tricks.toList()
    }

    /**
     * Use this function to add a new trick to the list. If there already is a trick with the same ID, it will be updated.
     *
     * @param trick - The new trick to be added.
     * @param positioning - The positioning of the new trick. [Positioning.Bottom] by default.
     * @param lifecycleOwner - The [LifecycleOwner] which should dictate for how long the module should be added. Null if the module should not be removed automatically. Null by default.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override fun learn(trick: Trick, positioning: Positioning, lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() = addTrick(trick, positioning)

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                forget(trick.id)
                lifecycleOwner.lifecycle.removeObserver(this)
            }
        }) ?: addTrick(trick, positioning)
    }

    /**
     * Removes the trick with the specified ID from the list of modules. The ID-s of unique modules can be accessed through their companion objects.
     *
     * @param id - The ID of the trick to be removed.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override fun forget(id: String) {
        Trick.removePendingChange(id)
        moduleList = moduleList.filterNot { it.id == id }
    }

    /**
     * Tries to open the current Activity's debug drawer.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override fun fetch() {
        if (isEnabled && drawers.containsKey(currentActivity)) {
            notifyListenersOnDragStarted()
            drawers[currentActivity]?.run { (parent as? BeagleDrawerLayout?)?.openDrawer(this) }
        }
    }

    /**
     * Tries to close the current Activity's debug drawer. For proper UX this should be used in onBackPressed() to block any other logic if it returns true.
     *
     * @return - True if the drawer was open, false otherwise
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override fun dismiss(): Boolean {
        val drawer = drawers[currentActivity]
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
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override fun log(message: String, tag: String?, payload: String?) {
        logItems = logItems.toMutableList().apply { add(0, LogItem(message = message, tag = tag, payload = payload)) }
    }

    /**
     * Registers a [BeagleListener] implementation.
     *
     * @param lifecycleOwner - By providing a [LifecycleOwner] Beagle will automatically remove the listener once the lifecycle is over. Null by default.
     * @param listener - The [BeagleListener] to add.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override fun addListener(lifecycleOwner: LifecycleOwner?, listener: BeagleListener) {
        lifecycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() = listeners.add(listener)

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                removeListener(listener)
                lifecycleOwner.lifecycle.removeObserver(this)
            }
        }) ?: listeners.add(listener)
    }

    /**
     * Removes the specified [BeagleListener] implementation.
     *
     * @param listener - The [BeagleListener] to remove.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override fun removeListener(listener: BeagleListener) {
        listeners.remove(listener)
    }

    /**
     * Removes all [BeagleListener] implementations.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override fun removeAllListeners() = listeners.clear()
    //endregion

    //region Implementation details
    private var Bundle.isDrawerOpen by BundleArgumentDelegate.Boolean("isDrawerOpen")
    internal var appearance = Appearance()
        private set
    internal var behavior = Behavior()
        private set
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
    internal var animationDurationMultiplier = 1f
        set(value) {
            if (field != value) {
                field = value
                updateItems()
                try {
                    ValueAnimator::class.java.methods.firstOrNull { it.name == "setDurationScale" }?.invoke(null, value)
                } catch (_: Throwable) {
                }
            }
        }
    internal var shouldTakeScreenshot = false
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
    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            dismiss()
        }
    }
    private var lastSensorUpdate = 0L
    private var lastSensorValues = Triple(0f, 0f, 0f)
    private val lifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            // The check is added to make sure Beagle is not injected into activities that come from other libraries (LeakCanary, Google sign-in / In app purchase) where it causes crashes.
            if (activity.componentName.className.startsWith(packageName) && !behavior.excludedActivities.contains(activity::class.java)) {
                drawers[activity] = createAndAddDrawerLayout(activity, savedInstanceState?.isDrawerOpen == true)
                (activity as? AppCompatActivity?)?.onBackPressedDispatcher?.addCallback(activity, onBackPressedCallback)
            }
        }

        override fun onActivityResumed(activity: Activity) {
            super.onActivityResumed(activity)
            if (currentActivity != activity) {
                dismiss()
                currentActivity = activity
            }
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
        Trick.pendingChangeListener = ::updateItems
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        if (behavior.triggerGesture == TriggerGesture.SWIPE_AND_SHAKE || behavior.triggerGesture == TriggerGesture.SHAKE_ONLY) {
            if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastSensorUpdate > 100) {
                    val diffTime = currentTime - lastSensorUpdate
                    lastSensorUpdate = currentTime
                    val newSensorValues = Triple(event.values[0], event.values[1], event.values[2])
                    val speed =
                        abs(newSensorValues.first + newSensorValues.second + newSensorValues.third - lastSensorValues.first - lastSensorValues.second - lastSensorValues.third) / diffTime * 10000
                    if (speed > SHAKE_THRESHOLD) {
                        fetch()
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
        if (shouldTakeScreenshot) {
            currentActivity?.takeAndShareScreenshot()
            shouldTakeScreenshot = false
        }
        Trick.resetPendingChanges()
        listeners.forEach { it.onDrawerClosed() }
    }

    private fun updateDrawerLockMode() = drawers.values.forEach { drawer ->
        (drawer.parent as? BeagleDrawerLayout?)?.setDrawerLockMode(
            if (isEnabled && (behavior.triggerGesture == TriggerGesture.SWIPE_ONLY || behavior.triggerGesture == TriggerGesture.SWIPE_AND_SHAKE))
                DrawerLayout.LOCK_MODE_UNDEFINED
            else
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        )
    }

    private fun addTrick(trick: Trick, positioning: Positioning) {
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

    private const val SHAKE_THRESHOLD = 1200
    //endregion
}