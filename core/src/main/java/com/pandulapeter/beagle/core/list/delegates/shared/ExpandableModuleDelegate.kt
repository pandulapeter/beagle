package com.pandulapeter.beagle.core.list.delegates.shared

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.ExpandableHeaderCell
import com.pandulapeter.beagle.core.list.cells.PaddingCell

internal interface ExpandableModuleDelegate<M : ExpandableModule<M>> : Module.Delegate<M> {

    override fun createCells(module: M): List<Cell<*>> = mutableListOf<Cell<*>>().apply {
        addHeader(module)
        if (module.isExpanded) {
            addItems(module)
            addFooter(module)
        }
    }

    private fun MutableList<Cell<*>>.addHeader(module: M) = add(
        ExpandableHeaderCell(
            id = "header_${module.id}",
            text = getTitle(module),
            isExpanded = module.isExpanded,
            canExpand = canExpand(module),
            onItemSelected = {
                module.isExpanded = !module.isExpanded
                BeagleCore.implementation.refresh()
            }
        )
    )

    private fun MutableList<Cell<*>>.addFooter(module: M) = add(PaddingCell(id = "footer_${module.id}"))

    fun canExpand(module: M): Boolean

    fun MutableList<Cell<*>>.addItems(module: M)

    fun getTitle(module: M): Text = module.getInternalTitle(BeagleCore.implementation)

    private var ExpandableModule<M>.isExpanded: Boolean
        get() = BeagleCore.implementation.memoryStorageManager.booleans[id] ?: isExpandedInitially
        set(value) {
            BeagleCore.implementation.memoryStorageManager.booleans[id] = value
        }
}