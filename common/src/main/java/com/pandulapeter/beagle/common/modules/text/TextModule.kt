package com.pandulapeter.beagle.common.modules.text

import com.pandulapeter.beagle.common.contracts.Cell
import com.pandulapeter.beagle.common.contracts.Module
import java.util.UUID

data class TextModule(
    override val id: String = "text_${UUID.randomUUID()}",
    val text: String
) : Module {

    override fun createCells(): List<Cell> = listOf(
        TextCell(
            id = id,
            text = text
        )
    )
}