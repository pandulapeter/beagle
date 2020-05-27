package com.pandulapeter.shared.contracts

import android.app.Application
import androidx.annotation.RestrictTo
import com.pandulapeter.shared.configuration.Appearance
import com.pandulapeter.shared.configuration.Behavior

/**
 * This interface ensures that the real implementation and the noop variant have the same public API.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface BeagleContract {

    /**
     * Initializes the library. No UI-related functionality will work before calling this function.
     *
     * @param application - Needed for hooking into the lifecycle.
     * @param appearance - Optional [Appearance] instance for customizing the appearance of the debug menu.
     * @param behavior - Optional [Behavior] instance for customizing the behavior of the debug menu.
     *
     * @return Whether or not the initialization was successful. Possible causes of failure:
     *      - The behavior specified the shake to open trigger gesture and the device does not have an accelerometer sensor.
     *      - The application depends the noop variant.
     */
    fun initialize(application: Application, appearance: Appearance = Appearance(), behavior: Behavior = Behavior()): Boolean = false

    /**
     * Call this to show the debug menu.
     *
     * @return Whether or not the operation was successful. Possible causes of failure:
     *      - The application does not have any created activities.
     *      - The currently visible Activity is not a subclass of [androidx.fragment.app.FragmentActivity].
     *      - The application depends the noop variant.
     */
    fun show(): Boolean = false

    /**
     * Call this to hide the debug menu.
     *
     * @return Whether or not the operation was successful. Possible causes of failure:
     *      - The debug menu is not currently visible.
     *      - The application depends the noop variant.
     */
    fun hide(): Boolean = false
}