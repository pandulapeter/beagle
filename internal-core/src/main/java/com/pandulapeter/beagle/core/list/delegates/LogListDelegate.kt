package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.ExpandedItemTextCell
import com.pandulapeter.beagle.core.list.delegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.core.util.model.SerializableLogEntry
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.modules.LogListModule

internal class LogListDelegate : ExpandableModuleDelegate<LogListModule> {

    override fun canExpand(module: LogListModule) = BeagleCore.implementation.getLogEntriesInternal(module.label).isNotEmpty()

    override fun MutableList<Cell<*>>.addItems(module: LogListModule) {
        addAll(BeagleCore.implementation.getLogEntriesInternal(module.label).take(module.maxItemCount).map { entry ->
            ExpandedItemTextCell(
                id = "${module.id}_${entry.id}",
                text = format(entry, module.timestampFormatter),
                isEnabled = true,
                shouldEllipsize = true,
                onItemSelected = {
                    BeagleCore.implementation.showDialog(
                        content = entry.getFormattedContents(BeagleCore.implementation.appearance.logLongTimestampFormatter).toText(),
                        isHorizontalScrollEnabled = module.isHorizontalScrollEnabled,
                        timestamp = entry.timestamp,
                        id = entry.id
                    )
                }
            )
        })
    }

    companion object {
        fun format(entry: SerializableLogEntry, timestampFormatter: ((Long) -> CharSequence)?) = (timestampFormatter?.let { formatter ->
            "[".append(formatter(entry.timestamp)).append("] ").append(entry.title.charSequence)
        } ?: entry.title.charSequence).let {
            if (entry.payload == null) it else it.append("*")
        }.toText()
    }
}