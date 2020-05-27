package com.pandulapeter.beagle

import com.pandulapeter.beagle.implementation.BeagleImplementation
import com.pandulapeter.beagleCore.contracts.BeagleContract

/**
 * Real implementation to be used in internal builds. See [BeagleContract] for function documentation.
 */
@Suppress("unused")
object Beagle : BeagleContract by BeagleImplementation()