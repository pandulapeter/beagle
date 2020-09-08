package com.pandulapeter.beagle.utils.extensions

import android.view.View
import android.view.ViewTreeObserver
import com.pandulapeter.beagle.utils.consume

inline fun View.waitForPreDraw(crossinline block: () -> Unit) = with(viewTreeObserver) {
    addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw() = consume {
            block()
            viewTreeObserver.removeOnPreDrawListener(this)
        }
    })
}