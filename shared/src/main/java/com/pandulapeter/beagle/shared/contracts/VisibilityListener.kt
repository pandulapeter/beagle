package com.pandulapeter.beagle.shared.contracts

/**
 * Implement this interface to get notified about when the debug menu gets shown or hidden.
 */
interface VisibilityListener {

    /**
     * Called just before the UI becomes visible.
     */
    fun onShown() = Unit

    /**
     * Called right after the UI becomes visible.
     */
    fun onHidden() = Unit
}