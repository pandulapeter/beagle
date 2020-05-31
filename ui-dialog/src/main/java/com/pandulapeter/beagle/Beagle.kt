package com.pandulapeter.beagle

import com.pandulapeter.beagle.implementation.DialogUiManager
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.core.BeagleImplementation

/**
 * Dialog UI implementation to be used in internal builds. See [BeagleContract] for documentation.
 */
@Suppress("unused")
object Beagle : BeagleContract by BeagleImplementation(DialogUiManager())