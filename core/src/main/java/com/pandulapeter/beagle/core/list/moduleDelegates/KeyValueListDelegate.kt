package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.KeyValueCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.modules.KeyValueListModule

internal class KeyValueListDelegate : ExpandableModuleDelegate<KeyValueListModule> {

    override fun MutableList<Cell<*>>.addItems(module: KeyValueListModule) {
        addAll(module.pairs.mapIndexed { index, item ->
            KeyValueCell(
                id = "${module.id}_$index",
                key = item.first,
                value = item.second
            )
        })
    }
}