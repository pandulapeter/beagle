package com.pandulapeter.beagle.common.contracts

import android.app.Application
import android.content.Context
import androidx.annotation.RestrictTo
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.listeners.OverlayListener
import com.pandulapeter.beagle.common.listeners.VisibilityListener

/**
 * This interface ensures that the real implementation and the noop variant have the same public API.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface BeagleContract {

    //region Core functionality
    /**
     * Can be used to enable or disable the library UI (but not its functionality) at runtime. Setting this to false has the side effect of calling the [hide] function.
     * Note: to completely disable the library UI as well as its functionality at compile-time, use the noop variant instead.
     *
     * @return Whether or not the library is currently enabled. Possible reasons for returning false:
     *  - The library UI has explicitly been disabled.
     *  - The application depends on the noop variant.
     */
    var isUiEnabled: Boolean
        get() = false
        set(_) = Unit

    /**
     * Initializes the library. No UI-related functionality will work before calling this function.
     *
     * @param application - Needed for hooking into the application lifecycle.
     * @param appearance - Optional [Appearance] instance for customizing the appearance of the debug menu.
     * @param behavior - Optional [Behavior] instance for customizing the behavior of the debug menu.
     *
     * @return Whether or not the initialization was successful. Possible causes of failure:
     *  - The behavior specified the shake to open trigger gesture but the device does not have an accelerometer sensor.
     *  - The application depends on the noop variant.
     */
    fun initialize(application: Application, appearance: Appearance = Appearance(), behavior: Behavior = Behavior()): Boolean = false

    /**
     * Call this to show the debug menu.
     *
     * @return Whether or not the operation was successful. Possible causes of failure:
     *  - The library has not been initialized yet.
     *  - The library UI has explicitly been disabled by setting [isUiEnabled] to false.
     *  - The debug menu is already visible.
     *  - The application does not have any visible activities at the moment (the lifecycle must be at least in STARTED state).
     *  - The currently visible Activity is not a subclass of [FragmentActivity].
     *  - The currently visible Activity does not support a debug menu (social login overlay, in-app-purchase overlay, manually excluded package specified by the [Behavior], etc).
     *  - The currently visible Activity is part of a package that has manually been excluded in the [Behavior] class.
     *  - The application depends on the ui-view variant (in this case its your responsibility to show / hide the UI).
     *  - The application depends on the noop variant.
     */
    fun show(): Boolean = false

    /**
     * Call this to hide the debug menu.
     *
     * @return Whether or not the operation was successful. Possible causes of failure:
     *  - The library has not been initialized yet.
     *  - The debug menu is not currently visible.
     *  - The application depends on the ui-view variant (in this case its your responsibility to show / hide the UI).
     *  - The application depends on the noop variant.
     */
    fun hide(): Boolean = false

    /**
     * Replaces the list of modules.
     *
     * @param modules - The new [Module] implementations to use.
     */
    fun setModules(vararg modules: Module) = Unit

    /**
     * Call this function to trigger recreating every cell model for every module.
     * Due to the underlying RecyclerView implementation this will only result in UI update events where differences are found.
     *
     * Manually updating the cells is only needed when writing custom modules, the build-in features already handle calling this function when needed.
     */
    fun updateCells() = Unit

    /**
     * Call this function to trigger invalidating the overlay layout. This will result in calling all registered [OverlayListener] implementations.
     */
    fun invalidateOverlay() = Unit
    //endregion

    //region Listeners
    /**
     * Adds a new [VisibilityListener] implementation to listen to the debug menu visibility changes.
     * The optional [LifecycleOwner] can be used to to automatically add / remove the listener when the lifecycle is created / destroyed.
     *
     * @param listener - The [VisibilityListener] implementation to add.
     * @param lifecycleOwner - The [LifecycleOwner] to use for automatically adding or removing the listener. Null by default.
     */
    fun addVisibilityListener(listener: VisibilityListener, lifecycleOwner: LifecycleOwner? = null) = Unit

    /**
     * Removes the [VisibilityListener] implementation, if it was added to the list of listeners.
     *
     * @param listener - The [VisibilityListener] implementation to remove.
     */
    fun removeVisibilityListener(listener: VisibilityListener) = Unit

    /**
     * Removes all [VisibilityListener] implementations, from the list of listeners.
     */
    fun clearVisibilityListeners() = Unit

    /**
     * Adds a new [OverlayListener] implementation which can be used to draw over the application layout.
     * The optional [LifecycleOwner] can be used to to automatically add / remove the listener when the lifecycle is created / destroyed.
     *
     * @param listener - The [OverlayListener] implementation to add.
     * @param lifecycleOwner - The [LifecycleOwner] to use for automatically adding or removing the listener. Null by default.
     */
    fun addOverlayListener(listener: OverlayListener, lifecycleOwner: LifecycleOwner? = null) = Unit

    /**
     * Removes the [OverlayListener] implementation, if it was added to the list of listeners.
     *
     * @param listener - The [OverlayListener] implementation to remove.
     */
    fun removeOverlayListener(listener: OverlayListener) = Unit

    /**
     * Removes all [OverlayListener] implementations, from the list of listeners.
     */
    fun clearOverlayListeners() = Unit
    //endregion

    //region Helpers
    /**
     * Convenience getter when a module callback implementation needs to perform UI-related operations or simply needs a [Context] instance.
     *
     * @return The nullable [FragmentActivity] instance which is currently on top of the back stack. Possible reasons for returning null:
     *  - The library has not been initialized yet.
     *  - The application does not have any created activities.
     *  - The currently visible Activity is not a subclass of [FragmentActivity].
     *  - The currently visible Activity does not support a debug menu (social login overlay, in-app-purchase overlay, manually excluded package specified by the [Behavior], etc).
     *  - The application depends on the noop variant.
     */
    val currentActivity: FragmentActivity? get() = null
    //endregion
}