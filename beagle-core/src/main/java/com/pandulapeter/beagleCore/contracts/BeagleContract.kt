package com.pandulapeter.beagleCore.contracts

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.pandulapeter.beagleCore.configuration.Appearance
import com.pandulapeter.beagleCore.configuration.Positioning
import com.pandulapeter.beagleCore.configuration.Trick
import com.pandulapeter.beagleCore.configuration.TriggerGesture

/**
 * This interface ensures that the real implementation and the "noop" variant have the same public API.
 */
@Suppress("unused")
interface BeagleContract {

    //region Public API
    var isEnabled: Boolean
    val currentActivity: Activity?
    val hasPendingChanges: Boolean

    fun imprint(
        application: Application,
        appearance: Appearance = Appearance(),
        triggerGesture: TriggerGesture = TriggerGesture.SWIPE_AND_SHAKE,
        shouldShowResetButton: Boolean = true,
        packageName: String? = null,
        excludedActivities: List<Class<out Activity>> = emptyList()
    ) = Unit

    fun learn(vararg tricks: Trick) = Unit

    fun learn(
        trick: Trick,
        positioning: Positioning = Positioning.Bottom,
        lifecycleOwner: LifecycleOwner? = null
    ) = Unit

    fun forget(id: String) = Unit

    fun fetch() = Unit

    fun dismiss() = false

    fun log(
        message: String,
        tag: String? = null,
        payload: String? = null
    ) = Unit

    fun addListener(listener: BeagleListener) = addListener(null, listener)

    fun addListener(
        lifecycleOwner: LifecycleOwner? = null,
        listener: BeagleListener
    ) = Unit

    fun removeListener(listener: BeagleListener) = Unit

    fun removeAllListeners() = Unit
    //endregion

    //region Alternative API
    /**
     * Hooks up the library to the Application's lifecycle. After this is called, a debug drawer will be inserted into every activity. This should be called
     * in the Application's onCreate() method.
     *
     * @param application - The [Application] instance.
     * @param appearance - The [Appearance] that specifies the appearance the drawer. Optional.
     * @param triggerGesture - Specifies the way the drawer can be opened. [TriggerGesture.SWIPE_AND_SHAKE] by default.
     * @param shouldShowResetButton - Whether or not to display a Reset button besides the Apply button that appears when the user makes changes that are not handled in real-time (see the "needsConfirmation" parameter of some Tricks). True by default.
     * @param packageName - Tha base package name of the application. Beagle will only work in Activities that are under this package. If not specified, an educated guess will be made (that won't work if your setup includes product flavors for example).
     * @param excludedActivities - The list of Activity classes where you specifically don't want to use Beagle. Empty by default.
     */
    fun initialize(
        application: Application,
        appearance: Appearance = Appearance(),
        triggerGesture: TriggerGesture = TriggerGesture.SWIPE_AND_SHAKE,
        shouldShowResetButton: Boolean,
        packageName: String? = null,
        excludedActivities: List<Class<out Activity>> = emptyList()
    ) = imprint(application, appearance, triggerGesture, shouldShowResetButton, packageName, excludedActivities)

    /**
     * Use this function to clear the contents of the menu and set a new list of tricks.
     *
     * @param modules - The new list of modules.
     */
    fun setModules(vararg modules: Trick) = learn(*modules)

    /**
     * Use this function to add a new trick to the list. If there already is a trick with the same ID, it will be updated.
     *
     * @param module - The new module to be added.
     * @param positioning - The positioning of the new trick. [Positioning.Bottom] by default.
     * @param lifecycleOwner - The [LifecycleOwner] which should dictate for how long the module should be added. Null if the module should not be removed automatically. Null by default.
     */
    fun putModule(
        module: Trick,
        positioning: Positioning = Positioning.Bottom,
        lifecycleOwner: LifecycleOwner? = null
    ) = learn(module, positioning, lifecycleOwner)

    /**
     * Removes the module with the specified ID from the list of modules. The ID-s of unique modules can be accessed through their companion objects.
     *
     * @param id - The ID of the module to be removed.
     */
    fun removeModule(id: String) = forget(id)
    //endregion

    //region Deprecated
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Use Beagle.learn(vararg modules) function instead.")
    fun learn(tricks: List<Trick>) = learn(*Array(tricks.size) { tricks[it] })

    @Deprecated("There is no longer a need for the Activity parameter", replaceWith = ReplaceWith("Beagle.fetch()"))
    fun fetch(activity: Activity) = fetch()

    @Deprecated("There is no longer a need for the Activity parameter", replaceWith = ReplaceWith("Beagle.dismiss()"))
    fun dismiss(activity: Activity) = dismiss()

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Use Beagle.setModules(vararg modules) function instead.")
    fun setModules(modules: List<Trick>) = learn(*Array(modules.size) { modules[it] })

    @Deprecated("There is no longer a need for the Activity parameter", replaceWith = ReplaceWith("Beagle.openDrawer()"))
    fun openDrawer(activity: Activity) = fetch()

    @Deprecated("There is no longer a need for the Activity parameter", replaceWith = ReplaceWith("Beagle.closeDrawer()"))
    fun closeDrawer(activity: Activity) = dismiss()
    //endregion
}