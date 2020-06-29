package com.pandulapeter.beagle.common.listeners

/**
 * Implement this interface to get notified about when the list of modules in changed in any way.
 */
interface UpdateListener {

    /**
     * Called on the main thread after the asynchronous diff calculation is finished and the debug menu is ready to be updated.
     */
    fun onContentsChanged()
}