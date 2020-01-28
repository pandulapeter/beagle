package com.pandulapeter.beagleCore.contracts

/**
 * Listener used for observing the state changes of the debug menu. Used for Beagle.addListener(), Beagle.removeListener or Beagle.removeAllListeners().
 */
interface BeagleListener {

    /**
     * Called when the drawer starts opening.
     */
    fun onDrawerDragStarted() = Unit


    /**
     * Called when the drawer gets opened.
     */
    fun onDrawerOpened() = Unit

    /**
     * Called when the drawer gets closed.
     */
    fun onDrawerClosed() = Unit
}