package com.pandulapeter.beagle.common.modules.text

import com.pandulapeter.beagle.common.contracts.ModuleCell
import com.pandulapeter.beagle.common.contracts.Module
import java.util.UUID

data class TextModule(
    override val id: String = "text_${UUID.randomUUID()}",
    val text: String
) : Module {

    override fun createCells(): List<ModuleCell> = listOf(
        TextModuleCell(
            id = id,
            text = text
        )
    )
}