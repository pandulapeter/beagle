package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.commonBase.randomId
import com.pandulapeter.beagle.modules.PaddingModule.Companion.DEFAULT_SIZE
import com.pandulapeter.beagle.modules.PaddingModule.Size

/**
 * Adds empty space between two other modules.
 *
 * @param size - The size of the padding. See the [Size] enum for dimensions. [DEFAULT_SIZE] by default.
 * @param id - A unique identifier for the module. [randomId] by default.
 */
data class PaddingModule(
    val size: Size = Size.MEDIUM,
    override val id: String = randomId
) : Module<PaddingModule> {

    enum class Size {
        SMALL, // 4dp
        MEDIUM, // 8dp
        LARGE // 16dp
    }

    companion object {
        private val DEFAULT_SIZE = Size.MEDIUM
    }
}