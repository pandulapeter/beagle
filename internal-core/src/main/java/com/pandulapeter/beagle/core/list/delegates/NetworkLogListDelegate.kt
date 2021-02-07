package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.ExpandedItemTextCell
import com.pandulapeter.beagle.core.list.delegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.core.util.model.SerializableNetworkLogEntry
import com.pandulapeter.beagle.modules.NetworkLogListModule

internal class NetworkLogListDelegate : ExpandableModuleDelegate<NetworkLogListModule> {

    override fun canExpand(module: NetworkLogListModule) = BeagleCore.implementation.getNetworkLogEntries().isNotEmpty()

    override fun MutableList<Cell<*>>.addItems(module: NetworkLogListModule) {
        addAll(BeagleCore.implementation.getNetworkLogEntries().take(module.maxItemCount).map { entry ->
            ExpandedItemTextCell(
                id = "${module.id}_${entry.id}",
                text = format(entry, module.timestampFormatter),
                isEnabled = true,
                shouldEllipsize = true,
                onItemSelected = {
                    BeagleCore.implementation.showNetworkEventDialog(
                        isOutgoing = entry.isOutgoing,
                        url = entry.url,
                        payload = entry.payload,
                        headers = entry.headers,
                        duration = entry.duration,
                        timestamp = entry.timestamp,
                        id = entry.id
                    )
                }
            )
        })
    }

    companion object {
        fun format(
            entry: SerializableNetworkLogEntry,
            formatter: ((Long) -> CharSequence)?
        ) = BeagleCore.implementation.appearance.networkLogTexts.titleFormatter(
            entry.isOutgoing,
            entry.url,
            formatter?.invoke(entry.timestamp),
            entry.headers,
            BeagleCore.implementation.behavior.networkLogBehavior.baseUrl
        )
    }
}