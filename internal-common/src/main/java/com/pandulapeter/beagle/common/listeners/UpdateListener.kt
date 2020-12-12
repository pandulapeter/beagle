package com.pandulapeter.beagle.common.listeners

/**
 * Implement this interface to get notified about when the list of modules is changed in any way.
 */
interface UpdateListener {

    /**
     * Called on the main thread after the asynchronous diff calculation is finished and the debug menu is ready to be updated.
     */
    fun onContentsChanged()

    /**
     * Called after the user presses the "Apply" button in case of the bulk apply feature of some modules (see the shouldRequireConfirmation parameter where relevant) and all callbacks are invoked.
     */
    fun onAllPendingChangesApplied() = Unit
}