package com.pandulapeter.beagle.logCrash.implementation

import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.BeagleCore

val isDrawerLocked = !Beagle.isUiEnabled || BeagleCore.implementation.behavior.shouldLockDrawer