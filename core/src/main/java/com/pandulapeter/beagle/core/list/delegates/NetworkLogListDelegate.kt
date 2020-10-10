package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.ExpandedItemTextCell
import com.pandulapeter.beagle.core.list.delegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.modules.NetworkLogListModule

internal class NetworkLogListDelegate : ExpandableModuleDelegate<NetworkLogListModule> {

    override fun canExpand(module: NetworkLogListModule) = BeagleCore.implementation.getNetworkLogEntries().isNotEmpty()

    override fun MutableList<Cell<*>>.addItems(module: NetworkLogListModule) {
        addAll(BeagleCore.implementation.getNetworkLogEntries().take(module.maxItemCount).map { entry ->
            (if (entry.isOutgoing) "↑ " else "↓ ").let { prefix ->
                (module.timestampFormatter?.let { formatter -> formatter(entry.timestamp) }).let { formattedTimestamp ->
                    ExpandedItemTextCell(
                        id = "${module.id}_${entry.id}",
                        text = Text.CharSequence(entry.url.replace(module.baseUrl, "").let { url ->
                            formattedTimestamp?.let { "$prefix[".append(formattedTimestamp).append("] ").append(url) } ?: prefix.append(url)
                        }.let { text ->
                            module.maxItemTitleLength?.let { maxItemTitleLength ->
                                if (text.length > maxItemTitleLength) {
                                    "${text.take(maxItemTitleLength)}…"
                                } else text
                            } ?: text
                        }),
                        isEnabled = true,
                        onItemSelected = {
                            BeagleCore.implementation.showNetworkEventDialog(
                                isOutgoing = entry.isOutgoing,
                                url = entry.url,
                                payload = entry.payload,
                                headers = entry.headers,
                                duration = entry.duration,
                                timestamp = entry.timestamp
                            )
                        }
                    )
                }
            }
        })
    }
}