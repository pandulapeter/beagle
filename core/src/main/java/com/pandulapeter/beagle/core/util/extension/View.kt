package com.pandulapeter.beagle.core.util.extension

import android.view.View

internal var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

internal var View.isScaledDown: Boolean
    get() = scaleX < 1.0f
    set(value) {
        scaleX = if (value) 0.9f else 1f
        scaleY = scaleX
    }