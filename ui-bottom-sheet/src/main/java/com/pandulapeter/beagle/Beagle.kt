package com.pandulapeter.beagle

import com.pandulapeter.beagle.logCrash.implementation.BottomSheetUiManager
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.core.BeagleImplementation

/**
 * Bottom sheet UI implementation to be used in internal builds. See [BeagleContract] for documentation.
 */
@Suppress("unused")
object Beagle : BeagleContract by BeagleImplementation(BottomSheetUiManager())