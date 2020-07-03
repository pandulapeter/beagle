package com.pandulapeter.beagle.core.util.extension

import android.os.Bundle
import androidx.fragment.app.Fragment

internal inline fun <T : Fragment> T.withArguments(bundleOperations: (Bundle) -> Unit): T = apply {
    arguments = Bundle().apply { bundleOperations(this) }
}