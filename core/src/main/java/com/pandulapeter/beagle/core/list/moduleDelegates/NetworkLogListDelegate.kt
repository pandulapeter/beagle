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
import kotlin.math.max

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
                                contents = "${prefix}${entry.url}"
                                    .let { text -> if (module.shouldShowHeaders) text.append("\n• Headers:${if (entry.headers.isEmpty()) " [none]" else entry.headers.joinToString("") { "\n    • $it" }}") else text }
                                    .let { text -> formattedTimestamp?.let { text.append("\n• Timestamp: $it") } ?: text }
                                    .let { text -> entry.duration?.let { text.append("\n• Duration: ${max(0, it)} ms") } ?: text }
                                    .append("\n\n${entry.payload.formatToJson()}"),
                                isHorizontalScrollEnabled = true
                            )
                        }
                    )
                }
            }
        })
    }

    private fun String.formatToJson() = try {
        if (isJson()) {
            val obj = JSONTokener(this).nextValue()
            if (obj is JSONObject) obj.toString(4) else (obj as? JSONArray)?.toString(4) ?: (obj as String)
        } else this
    } catch (e: JSONException) {
        this
    }

    private fun String.isJson(): Boolean {
        try {
            JSONObject(this)
        } catch (_: JSONException) {
            try {
                JSONArray(this)
            } catch (_: JSONException) {
                return false
            }
        }
        return true
    }
}