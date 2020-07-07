package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.core.list.delegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.modules.LongTextModule

internal class LongTextDelegate : ExpandableModuleDelegate<LongTextModule> {

    override fun canExpand(module: LongTextModule) = module.text.isNotBlank()

    override fun MutableList<Cell<*>>.addItems(module: LongTextModule) {
        add(
            TextCell(
                id = "text_${module.id}",
                text = "• ".append(module.text),
                onItemSelected = null
            )
        )
    }
}