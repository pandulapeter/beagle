package com.pandulapeter.beagleCore.contracts

/**
 * Listener used for observing the state changes of the debug menu. Used for Beagle.addListener(), Beagle.removeListener or Beagle.clearListeners().
 */
interface BeagleListener {

    /**
     * Called when the drawer gets opened.
     */
    fun onDrawerOpened() = Unit

    /**
     * Called when the drawer gets closed.
     */
    fun onDrawerClosed() = Unit
}