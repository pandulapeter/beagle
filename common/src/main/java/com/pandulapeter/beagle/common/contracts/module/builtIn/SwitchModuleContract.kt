package com.pandulapeter.beagle.common.contracts.module.builtIn

import com.pandulapeter.beagle.common.contracts.module.PersistableModule

/**
 * This interface ensures that the real implementation and the noop variant have the same public API.
 */
interface SwitchModuleContract : TextModuleContract, PersistableModule<Boolean>