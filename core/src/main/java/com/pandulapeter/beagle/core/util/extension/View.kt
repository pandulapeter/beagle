package com.pandulapeter.beagle.core.util.extension

import android.view.View

internal var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }