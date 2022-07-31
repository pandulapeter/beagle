package com.pandulapeter.beagle.logCrash.implementation

import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.BeagleCore

val isDrawerLocked
    get() = !Beagle.isUiEnabled
            || BeagleCore.implementation.behavior.shouldLockDrawer
            || BeagleCore.implementation.currentActivity?.let { BeagleCore.implementation.behavior.shouldShowDebugMenu(it) } != true