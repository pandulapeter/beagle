package com.pandulapeter.beagle.common.contracts

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.common.configuration.Placement
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.listeners.LogListener
import com.pandulapeter.beagle.common.listeners.NetworkLogListener
import com.pandulapeter.beagle.common.listeners.OverlayListener
import com.pandulapeter.beagle.common.listeners.UpdateListener
import com.pandulapeter.beagle.common.listeners.VisibilityListener
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.commonBase.randomId
import kotlin.reflect.KClass

/**
 * This interface ensures that the real implementations and the noop variant have the same public API.
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
    fun initialize(
        application: Application,
        appearance: Appearance = Appearance(),
        behavior: Behavior = Behavior()
    ): Boolean = false

    /**
     * Call this to show the debug menu.
     *
     * @return Whether or not the operation was successful. Possible causes of failure:
     *  - The library has not been initialized yet.
     *  - The library UI has explicitly been disabled by setting [isUiEnabled] to false.
     *  - The debug menu or the media preview dialog is already visible.
     *  - The application does not have any visible activities at the moment (the lifecycle must be at least in STARTED state).
     *  - The currently visible Activity is not a subclass of [FragmentActivity].
     *  - The currently visible Activity does not support a debug menu (social login overlay, in-app-purchase overlay, etc).
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
     * To see the list of built-in modules, check out this folder: https://github.com/pandulapeter/beagle/tree/master/common/src/main/java/com/pandulapeter/beagle/modules
     *
     * @param modules - The new [Module] implementations to use.
     */
    fun set(vararg modules: Module<*>) = Unit

    /**
     * Use this function to add new modules to the debug menu without removing the current ones. Modules with duplicated ID-s will get replaced.
     *
     * To see the list of built-in modules, check out this folder: https://github.com/pandulapeter/beagle/tree/master/common/src/main/java/com/pandulapeter/beagle/modules
     *
     * @param modules - The new modules to be added.
     * @param placement - The positioning of the new trick. Optional, [Placement.Bottom] by default.
     * @param lifecycleOwner - The [LifecycleOwner] which should manage for how long the module should remain added. Null if the module should not be removed automatically. Null by default.
     */
    fun add(
        vararg modules: Module<*>,
        placement: Placement = Placement.Bottom,
        lifecycleOwner: LifecycleOwner? = null
    ) = Unit

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
     * @throws ClassCastException - Due to type erasure, the library cannot catch possible type casting errors so expecting an incorrect type will cause a ClassCastException.
     *
     * @return The properly casted instance or null. Reasons for returning null:
     *  - The module with the specified ID is not currently added to the debug menu.
     */
    @Throws(ClassCastException::class)
    fun <M : Module<M>> find(id: String): M? = null

    /**
     * Can be used to find the [Module.Delegate] implementation by the type of the module it's supposed to handle.
     *
     * @return The [Module.Delegate] implementation or null. Reasons for returning null:
     *  - No module delegate is registered for the specified type.
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
    fun addLogListener(
        listener: LogListener,
        lifecycleOwner: LifecycleOwner? = null
    ) = Unit

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
     * Adds a new [NetworkLogListener] implementation which can be used to get notified when a new network event is logged using Beagle.logNetworkEvent().
     * The optional [LifecycleOwner] can be used to to automatically add / remove the listener when the lifecycle is created / destroyed.
     *
     * @param listener - The [NetworkLogListener] implementation to add.
     * @param lifecycleOwner - The [LifecycleOwner] to use for automatically adding or removing the listener. Null by default.
     */
    fun addNetworkLogListener(
        listener: NetworkLogListener,
        lifecycleOwner: LifecycleOwner? = null
    ) = Unit

    /**
     * Removes the [NetworkLogListener] implementation, if it was added to the list of listeners.
     *
     * @param listener - The [LogListener] implementation to remove.
     */
    fun removeNetworkLogListener(listener: NetworkLogListener) = Unit

    /**
     * Removes all [NetworkLogListener] implementations, from the list of listeners.
     */
    fun clearNetworkLogListeners() = Unit

    /**
     * Adds a new [OverlayListener] implementation which can be used to draw over the application layout.
     * The optional [LifecycleOwner] can be used to to automatically add / remove the listener when the lifecycle is created / destroyed.
     *
     * @param listener - The [OverlayListener] implementation to add.
     * @param lifecycleOwner - The [LifecycleOwner] to use for automatically adding or removing the listener. Null by default.
     */
    fun addOverlayListener(
        listener: OverlayListener,
        lifecycleOwner: LifecycleOwner? = null
    ) = Unit

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
     * Adds a new [UpdateListener] implementation which can be used to get notified about change events to the contents of the debug menu.
     * The optional [LifecycleOwner] can be used to to automatically add / remove the listener when the lifecycle is created / destroyed.
     *
     * @param listener - The [UpdateListener] implementation to add.
     * @param lifecycleOwner - The [LifecycleOwner] to use for automatically adding or removing the listener. Null by default.
     */
    fun addUpdateListener(
        listener: UpdateListener,
        lifecycleOwner: LifecycleOwner? = null
    ) = Unit

    /**
     * Removes the [UpdateListener] implementation, if it was added to the list of listeners.
     *
     * @param listener - The [UpdateListener] implementation to remove.
     */
    fun removeUpdateListener(listener: UpdateListener) = Unit

    /**
     * Removes all [UpdateListener] implementations, from the list of listeners.
     */
    fun clearUpdateListeners() = Unit

    /**
     * Adds a new [VisibilityListener] implementation to listen to the debug menu visibility changes.
     * The optional [LifecycleOwner] can be used to to automatically add / remove the listener when the lifecycle is created / destroyed.
     *
     * @param listener - The [VisibilityListener] implementation to add.
     * @param lifecycleOwner - The [LifecycleOwner] to use for automatically adding or removing the listener. Null by default.
     */
    fun addVisibilityListener(
        listener: VisibilityListener,
        lifecycleOwner: LifecycleOwner? = null
    ) = Unit

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
     *  - The currently visible Activity is the debug menu (let me know if this is an issue...)
     *  - The application depends on the noop variant.
     */
    val currentActivity: FragmentActivity? get() = null

    /**
     * Adds a new log message handled by instances of LogListModule and notifies the registered LogListeners.
     *
     * @param message - The message that will be displayed.
     * @param label - Optional tag that can be used to create filtered LogListModule instances, null by default.
     * @param payload - Extra message that will only be displayed when the user selects the log entry. Entries with payloads are marked with "*" at the end. Optional, null by default.
     * @param timestamp - The moment the event happened. The value defaults to the moment this function is invoked.
     * @param id - The unique identifier of the event. [randomId] by default.
     */
    fun log(
        message: CharSequence,
        label: String? = null,
        payload: CharSequence? = null,
        timestamp: Long = currentTimestamp,
        id: String = randomId
    ) = Unit

    /**
     * Clears all log messages for the specified tag.
     *
     * @param label - A specific tag to filter out, or null to delete all logs. Null by default.
     */
    fun clearLogs(label: String? = null) = Unit

    /**
     * Adds a new network log message handled by NetworkLogListModule and notifies the registered NetworkLogListeners.
     *
     * @param isOutgoing - True for requests, false for responses.
     * @param url - The complete URL of the endpoint. This will appear in the log list as the title of the entry.
     * @param payload - The payload String of the request or null if not applicable. This will appear in the dialog when the user selects the entry. JSON strings will automatically be formatted.
     * @param headers - The request headers, or null if not applicable. Null by default.
     * @param duration - The duration of the event, or null if not applicable. Null by default.
     * @param timestamp - The moment the event happened. The value defaults to the moment this function is invoked.
     * @param id - The unique identifier of the event. [randomId] by default.
     */
    fun logNetworkEvent(
        isOutgoing: Boolean,
        url: String,
        payload: String?,
        headers: List<String>? = null,
        duration: Long? = null,
        timestamp: Long = currentTimestamp,
        id: String = randomId
    ) = Unit

    /**
     * Clears all network log messages.
     */
    fun clearNetworkLogs() = Unit

    /**
     * Captures a screenshot image and saves it in the application's private directory (exposing it through a FileProvider).
     * Below Android Lollipop the root view's drawing cache will be used which is an inferior solution (only the current decorView will be captured, without system decorations).
     * Above Android Lollipop the entire screen will be captured, after the user agrees to the system prompt.
     * Check out the [Behavior] class to override the default file naming logic.
     *
     * The app will show a media preview dialog when the recording is done, or a notification that opens to the gallery if it is no longer in the foreground.
     *
     * @param callback - The lambda that gets invoked after the screenshot is done, with the [Uri] pointing to the PNG file. The argument will be null if anything goes wrong.
     */
    fun takeScreenshot(callback: (Uri?) -> Unit) = callback.invoke(null)

    /**
     * Captures a screen recording video and saves it in the application's private directory (exposing it through a FileProvider).
     * A notification will appear during the recording which contains the button to stop it. The recording will have at most 720p resolution.
     * This feature relies on API-s only present on Android Lollipop and above. Recording will only be started after the user agrees to the system prompt.
     * Check out the [Behavior] class to override the default file naming logic.
     *
     * The app will show a media preview dialog when the recording is done, or a notification that opens to the gallery if it is no longer in the foreground.
     *
     * @param callback - The lambda that gets invoked after the recording is done, with the [Uri] pointing to the MP4 file. The argument will be null if anything goes wrong.
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun recordScreen(callback: (Uri?) -> Unit) = callback.invoke(null)

    /**
     * Opens the gallery of captured screenshot images and screen recording videos.
     * Check out the [Appearance] class for customization options.
     */
    fun openGallery() = Unit

    /**
     * Opens the bug reporting screen. Empty sections will not be displayed.
     * Check out the [Appearance] class for customization options.
     *
     * @param shouldShowGallerySection - Whether or not the Gallery section should be added. True by default.
     * @param shouldShowNetworkLogsSection - Whether or not the section of network logs should be added. True by default.
     * @param logLabelSectionsToShow - The list of log labels for which sections should be added. By default it adds a section for all logs, without filtering.
     * @param descriptionTemplate - The default value of the free-text input. Empty string by default.
     */
    fun openBugReportingScreen(
        shouldShowGallerySection: Boolean = true,
        shouldShowNetworkLogsSection: Boolean = true,
        logLabelSectionsToShow: List<String?> = listOf(null),
        descriptionTemplate: String = ""
    ) = Unit

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

    /**
     * Displays a dialog in debug builds.
     *
     * @param content - The text that appears in the dialog.
     * @param isHorizontalScrollEnabled - When true, the dialog will scroll in both directions. If false, the text will be wrapped and only vertical scrolling will be supported. False by default.
     * @param timestamp - The moment the contents of the dialog are relevant to. This value is used for generating the file name when sharing. By default it is the moment of the function call.
     * @param id - The unique identifier of the event. [randomId] by default.
     */
    fun showDialog(
        content: CharSequence,
        isHorizontalScrollEnabled: Boolean = false,
        timestamp: Long = currentTimestamp,
        id: String = randomId
    ) = Unit

    /**
     * Displays a network event dialog in debug builds.
     *
     * @param isOutgoing - True for requests, false for responses.
     * @param url - The complete URL of the endpoint.
     * @param payload - The payload String of the request. JSON strings will automatically be formatted.
     * @param headers - The request headers, or null if not applicable. Null by default
     * @param duration - The duration of the event, or null if not applicable. Null by default
     * @param timestamp - The moment the event happened. The value defaults to the moment this function is invoked.
     * @param id - The unique identifier of the event. [randomId] by default.
     */
    fun showNetworkEventDialog(
        isOutgoing: Boolean,
        url: String,
        payload: String,
        headers: List<String>? = null,
        duration: Long? = null,
        timestamp: Long = currentTimestamp,
        id: String = randomId
    ) = Unit
    //endregion
}