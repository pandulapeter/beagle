package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.ExpandedItemKeyValueCell
import com.pandulapeter.beagle.core.list.delegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.modules.KeyValueListModule

internal class KeyValueListDelegate : ExpandableModuleDelegate<KeyValueListModule> {

    override fun canExpand(module: KeyValueListModule) = module.pairs.isNotEmpty()

    override fun MutableList<Cell<*>>.addItems(module: KeyValueListModule) {
        addAll(module.pairs.mapIndexed { index, item ->
            ExpandedItemKeyValueCell(
                id = "${module.id}_$index",
                key = item.first,
                value = item.second
            )
        })
    }
}