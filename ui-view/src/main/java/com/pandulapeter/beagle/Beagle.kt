package com.pandulapeter.beagle

import com.pandulapeter.beagle.core.BeagleImplementation
import com.pandulapeter.beagle.core.manager.UiManagerContract
import com.pandulapeter.beagle.common.contracts.BeagleContract

/**
 * View UI implementation to be used in internal builds. See [BeagleContract] for function documentation.
 */
@Suppress("unused")
object Beagle : BeagleContract by BeagleImplementation(object : UiManagerContract {})