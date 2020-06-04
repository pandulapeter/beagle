package com.pandulapeter.beagle

import androidx.annotation.RestrictTo
import com.pandulapeter.beagle.core.BeagleImplementation

/**
 * Never reference this class in your app. It's not present in the noop variant so it will prevent your release builds from compiling.
 */
//TODO: Make sure this annotation works as expected
@RestrictTo(RestrictTo.Scope.LIBRARY)
object BeagleCore {

    lateinit var implementation: BeagleImplementation
        internal set
}