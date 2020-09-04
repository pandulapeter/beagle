package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.core.list.delegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.modules.LogListModule

internal class LogListDelegate : ExpandableModuleDelegate<LogListModule> {

    override fun canExpand(module: LogListModule) = BeagleCore.implementation.getLogEntries(module.label).isNotEmpty()

    override fun MutableList<Cell<*>>.addItems(module: LogListModule) {
        addAll(BeagleCore.implementation.getLogEntries(module.label).take(module.maxItemCount).map { entry ->
            TextCell(
                id = "${module.id}_${entry.id}",
                text = (module.timestampFormatter?.let { formatter -> "• [".append(formatter(entry.timestamp)).append("] ").append(entry.title) } ?: "• ".append(entry.title)).let {
                    if (entry.payload == null) it else it.append("*")
                },
                isEnabled = true,
                onItemSelected = if (entry.payload != null) fun() {
                    BeagleCore.implementation.showDialog(
                        contents = entry.payload,
                        isHorizontalScrollEnabled = module.isHorizontalScrollEnabled
                    )
                } else null
            )
        })
    }
}