package com.pandulapeter.beagle.common.contracts

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.common.configuration.Positioning
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.listeners.LogListener
import com.pandulapeter.beagle.common.listeners.OverlayListener
import com.pandulapeter.beagle.common.listeners.VisibilityListener
import kotlin.reflect.KClass

/**
 * This interface ensures that the real implementation and the noop variant have the same public API.
 */
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
     * Note: If any of your modules uses persisted data, you must call this function before any other Beagle-related calls to avoid crashes.
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
    //endregion

    //region Module management
    /**
     * Replaces the list of modules.
     *
     * @param modules - The new [Module] implementations to use.
     */
    fun set(vararg modules: Module<*>) = Unit

    /**
     * Use this function to add new modules to the debug menu without removing the current ones. Modules with duplicated ID-s will get replaced.
     *
     * @param modules - The new modules to be added.
     * @param positioning - The positioning of the new trick. Optional, [Positioning.Bottom] by default.
     * @param lifecycleOwner - The [LifecycleOwner] which should manage for how long the module should remain added. Null if the module should not be removed automatically. Null by default.
     */
    fun add(vararg modules: Module<*>, positioning: Positioning = Positioning.Bottom, lifecycleOwner: LifecycleOwner? = null) = Unit

    /**
     * Remove one or more modules with the specified ID-s from the debug menu.
     *
     * @param ids - The ID-s of the modules to be removed.
     */
    fun remove(vararg ids: String) = Unit

    /**
     * Can be used to verify if a [Module] with the specified ID is added to the debug menu.
     *
     * @param id - The String identifier of the module specified in its constructor.
     *
     * @return Whether or not the debug menu contains a module with the specified ID.
     */
    fun contains(id: String): Boolean = false

    /**
     * Can be used to get the reference to a [Module].
     * Useful if Beagle is used as the single source of truth for persisted debug data which can be queried from individual modules.
     *
     * @param id - The String identifier of the module specified in its constructor.
     *
     * @return The properly casted instance or null. Reasons for returning null:
     *  - The module with the specified ID is not currently added to the debug menu.
     *  - The type casting failed.
     */
    fun <M : Module<M>> find(id: String): M? = null

    /**
     * Can be used to find the [Module.Delegate] implementation by the type of the module it's supposed to handle.
     *
     * @return The [Module.Delegate] implementation or null. Reasons for returning null:
     *  - No module delegate is registered for the specified type.
     *  - The type casting failed.
     */
    fun <M : Module<M>> delegateFor(type: KClass<out M>): Module.Delegate<M>? = null
    //endregion

    //region Listeners
    /**
     * Adds a new [LogListener] implementation which can be used to get notified when a new log message is added using Beagle.log().
     * The optional [LifecycleOwner] can be used to to automatically add / remove the listener when the lifecycle is created / destroyed.
     *
     * @param listener - The [LogListener] implementation to add.
     * @param lifecycleOwner - The [LifecycleOwner] to use for automatically adding or removing the listener. Null by default.
     */
    fun addLogListener(listener: LogListener, lifecycleOwner: LifecycleOwner? = null) = Unit

    /**
     * Removes the [LogListener] implementation, if it was added to the list of listeners.
     *
     * @param listener - The [LogListener] implementation to remove.
     */
    fun removeLogListener(listener: LogListener) = Unit

    /**
     * Removes all [LogListener] implementations, from the list of listeners.
     */
    fun clearLogListeners() = Unit

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

    /**
     * Adds a new log handled by instances of LogListModule and notifies the registered LogListeners.
     *
     * @param message - The message that will be displayed.
     * @param tag - Optional tag that can be used to create filtered LogListModule instances, null by default.
     * @param payload - Extra message that will only be displayed when the user selects the log entry. Optional, null by default.
     */
    fun log(message: String, tag: String? = null, payload: String? = null) = Unit

    /**
     * Call this function to trigger recreating every cell model for every module.
     * Due to the underlying RecyclerView implementation this will only result in UI update events where differences are found.
     *
     * Manually updating the cells is only needed when writing custom modules, the build-in features already handle calling this function when needed.
     */
    fun refresh() = Unit

    /**
     * Call this function to trigger invalidating the overlay layout. This will result in calling all registered [OverlayListener] implementations.
     */
    fun invalidateOverlay() = Unit
    //endregion
}