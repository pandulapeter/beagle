package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.modules.LogListModule

internal class LogListDelegate : ExpandableModuleDelegate<LogListModule> {

    override fun MutableList<Cell<*>>.addItems(module: LogListModule) {
        addAll(BeagleCore.implementation.getLogEntries(module.tag).map { entry ->
            TextCell(
                id = "${module.id}_${entry.id}",
                text = entry.message,
                onItemSelected = if (entry.payload == null) null else null //TODO: Open dialog
            )
        })
    }
}