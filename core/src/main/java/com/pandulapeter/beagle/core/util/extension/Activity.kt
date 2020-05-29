package com.pandulapeter.beagle.core.util.extension

import android.app.Activity
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.BeagleCore

//TODO: Handle LeakCanary, Google Play IAP overlay, social log in overlays.
private val excludedPackageNames = listOf(
    "com.pandulapeter.beagle.implementation.DebugMenuActivity"
)

internal val Activity.supportsDebugMenu
    get() = this is FragmentActivity
            && excludedPackageNames.none { componentName.className.startsWith(it) }
            && BeagleCore.implementation.behavior.excludedPackageNames.none { componentName.className.startsWith(it) }

internal fun Activity.findRootViewGroup(): ViewGroup = findViewById(android.R.id.content) ?: window.decorView.findViewById(android.R.id.content)