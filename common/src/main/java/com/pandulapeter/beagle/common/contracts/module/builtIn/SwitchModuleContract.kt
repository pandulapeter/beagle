package com.pandulapeter.beagle.common.contracts.module.builtIn

import androidx.annotation.RestrictTo
import com.pandulapeter.beagle.common.contracts.module.PersistableModule

/**
 * This interface ensures that the real implementation and the noop variant have the same public API.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
interface SwitchModuleContract : TextModuleContract, PersistableModule<Boolean>