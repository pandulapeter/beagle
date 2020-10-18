package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.ExpandedItemTextCell
import com.pandulapeter.beagle.core.list.delegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.core.util.NetworkLogEntry
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.modules.NetworkLogListModule

internal class NetworkLogListDelegate : ExpandableModuleDelegate<NetworkLogListModule> {

    override fun canExpand(module: NetworkLogListModule) = BeagleCore.implementation.getNetworkLogEntries().isNotEmpty()

    override fun MutableList<Cell<*>>.addItems(module: NetworkLogListModule) {
        addAll(BeagleCore.implementation.getNetworkLogEntries().take(module.maxItemCount).map { entry ->
            ExpandedItemTextCell(
                id = "${module.id}_${entry.id}",
                text = format(entry, module.timestampFormatter, module.baseUrl),
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
        fun format(entry: NetworkLogEntry, formatter: ((Long) -> CharSequence)?, baseUrl: String = "") = Text.CharSequence(entry.url.replace(baseUrl, "").let { url ->
            (if (entry.isOutgoing) "↑ " else "↓ ").let { prefix ->
                formatter?.invoke(entry.timestamp)?.let { formattedTimestamp -> "$prefix[".append(formattedTimestamp).append("] ").append(url) } ?: prefix.append(url)
            }
        })
    }
}