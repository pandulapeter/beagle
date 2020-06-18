package com.pandulapeter.beagle.core.list.moduleDelegates.shared

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.ExpandableHeaderCell

internal interface ExpandableModuleDelegate<M : ExpandableModule<M>> : Module.Delegate<M> {

    override fun createCells(module: M): List<Cell<*>> = mutableListOf<Cell<*>>().apply {
        addHeader(module)
        if (module.isExpanded) {
            addItems(module)
        }
    }

    private fun MutableList<Cell<*>>.addHeader(module: M) = add(
        ExpandableHeaderCell(
            id = "header_${module.id}",
            text = module.title,
            isExpanded = module.isExpanded,
            canExpand = module.canExpand,
            onItemSelected = {
                module.isExpanded = !module.isExpanded
                BeagleCore.implementation.refreshCells()
            }
        )
    )

    fun MutableList<Cell<*>>.addItems(module: M)

    private var ExpandableModule<M>.isExpanded: Boolean
        get() = BeagleCore.implementation.memoryStorageManager.booleans[id] ?: isExpandedInitially
        set(value) {
            BeagleCore.implementation.memoryStorageManager.booleans[id] = value
        }
}