package com.pandulapeter.beagle.core.util.extension

import android.app.Activity
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity

//TODO: Verify that the current activity belongs to the app (do not inject to LeakCanary, Google Play IAP overlay, social log in overlay, etc)
//TODO: Find a better way than blacklisting package names
internal val Activity.supportsDebugMenu
    get() = this is FragmentActivity
            && !componentName.className.startsWith("com.pandulapeter.beagle.implementation.BeagleActivity")

internal fun Activity.findRootViewGroup(): ViewGroup = findViewById(android.R.id.content) ?: window.decorView.findViewById(android.R.id.content)