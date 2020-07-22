package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.listeners.VisibilityListener

internal fun performOnHide(action: () -> Unit) {
    val listener = object : VisibilityListener {
        override fun onHidden() {
            BeagleCore.implementation.removeVisibilityListener(this)
            action()
        }
    }
    BeagleCore.implementation.addInternalVisibilityListener(listener)
    if (!BeagleCore.implementation.hide()) {
        listener.onHidden()
    }
}