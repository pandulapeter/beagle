package com.pandulapeter.beagle.shared.contracts

import android.app.Application
import androidx.annotation.RestrictTo
import com.pandulapeter.beagle.shared.configuration.Appearance
import com.pandulapeter.beagle.shared.configuration.Behavior

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
     *      - The behavior specified the shake to open trigger gesture but the device does not have an accelerometer sensor.
     *      - The application depends on the noop variant.
     */
    fun initialize(application: Application, appearance: Appearance = Appearance(), behavior: Behavior = Behavior()): Boolean = false

    /**
     * Call this to show the debug menu.
     *
     * @return Whether or not the operation was successful. Possible causes of failure:
     *      - The library has not been initialized yet.
     *      - The debug menu is already visible.
     *      - The application does not have any created activities.
     *      - The currently visible Activity is not a subclass of [androidx.fragment.app.FragmentActivity].
     *      - The application depends on the ui-view variant (in this case its your responsibility to show / hide the ).
     *      - The application depends on the noop variant.
     */
    fun show(): Boolean = false

    /**
     * Call this to hide the debug menu.
     *
     * @return Whether or not the operation was successful. Possible causes of failure:
     *      - The library has not been initialized yet.
     *      - The debug menu is not currently visible.
     *      - The application depends on the noop variant.
     */
    fun hide(): Boolean = false
}