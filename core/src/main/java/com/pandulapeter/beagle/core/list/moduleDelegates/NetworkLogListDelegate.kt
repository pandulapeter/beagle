package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.modules.NetworkLogListModule
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

internal class NetworkLogListDelegate : ExpandableModuleDelegate<NetworkLogListModule> {

    override fun canExpand(module: NetworkLogListModule) = BeagleCore.implementation.getNetworkLogEntries().isNotEmpty()

    override fun MutableList<Cell<*>>.addItems(module: NetworkLogListModule) {
        addAll(BeagleCore.implementation.getNetworkLogEntries().take(module.maxItemCount).map { entry ->
            (if (entry.isOutgoing) "↑ " else "↓ ").let { prefix ->
                (module.timestampFormatter?.let { formatter -> formatter(entry.timestamp) }).let { formattedTimestamp ->
                    TextCell(
                        id = "${module.id}_${entry.id}",
                        text = entry.url.replace(module.baseUrl, "").let { url ->
                            formattedTimestamp?.let { "$prefix[".append(formattedTimestamp).append("] ").append(url) } ?: prefix.append(url)
                        },
                        onItemSelected = {
                            BeagleCore.implementation.showDialog(
                                contents = "$prefix${entry.url}${formattedTimestamp?.let { "\nTimestamp: $formattedTimestamp" } ?: ""}${entry.duration?.let { "\nDuration: $it ms" } ?: ""}\n\n${entry.payload.formatToJson()}",
                                isHorizontalScrollEnabled = true
                            )
                        }
                    )
                }
            }
        })
    }

    private fun String.formatToJson() = try {
        val obj = JSONTokener(this).nextValue()
        if (obj is JSONObject) obj.toString(4) else (obj as? JSONArray)?.toString(4) ?: (obj as String)
    } catch (e: JSONException) {
        this
    }
}