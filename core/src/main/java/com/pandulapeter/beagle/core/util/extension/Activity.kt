package com.pandulapeter.beagle.core.util.extension

import android.app.Activity
import androidx.fragment.app.FragmentActivity

internal val Activity.supportsDebugMenu get() = this is FragmentActivity //TODO: Verify that the current activity belongs to the app (do not inject to LeakCanary, Google Play IAP overlay, social log in overlay, etc)