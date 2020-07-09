package com.pandulapeter.beagle

import android.app.Activity
import com.pandulapeter.beagleCore.contracts.BeagleContract

/**
 * Fake implementation to be used in release builds.
 */
@Suppress("unused")
@Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
object Beagle : BeagleContract {

    /**
     * Does nothing (Beagle is always disabled in the noop variant).
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override var isEnabled = false

    /**
     * Returns null (Beagle does not work at all in the noop variant).
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override val currentActivity: Activity? = null

    /**
     * Returns false (Beagle does not work at all in the noop variant).
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override val hasPendingChanges: Boolean = false

    /**
     * Does nothing (Beagle does not work at all in the noop variant).
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    override var onAllChangesApplied: (() -> Unit)? = null
}