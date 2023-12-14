package com.pandulapeter.beagle

import com.pandulapeter.beagle.logCrash.implementation.ActivityUiManager
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.core.BeagleImplementation

/**
 * Activity UI implementation to be used in internal builds. See [BeagleContract] for documentation.
 */
object Beagle : BeagleContract by BeagleImplementation(ActivityUiManager())