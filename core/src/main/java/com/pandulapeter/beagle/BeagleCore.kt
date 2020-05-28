package com.pandulapeter.beagle

import androidx.annotation.RestrictTo
import com.pandulapeter.beagle.core.BeagleImplementation

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
object BeagleCore {

    lateinit var implementation: BeagleImplementation
        internal set
}