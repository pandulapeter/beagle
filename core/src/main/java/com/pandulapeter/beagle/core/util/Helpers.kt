package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.listeners.VisibilityListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal fun performOnHide(action: () -> Unit) {
    val listener = object : VisibilityListener {
        override fun onHidden() {
            val reference = this
            action()
            GlobalScope.launch {
                delay(100)
                BeagleCore.implementation.removeVisibilityListener(reference)
            }
        }
    }
    if (BeagleCore.implementation.hide()) {
        BeagleCore.implementation.addInternalVisibilityListener(listener)
    } else {
        listener.onHidden()
    }
}