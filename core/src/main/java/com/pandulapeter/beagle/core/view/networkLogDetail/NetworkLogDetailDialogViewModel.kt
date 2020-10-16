package com.pandulapeter.beagle.core.view.networkLogDetail

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.core.util.extension.createAndShareLogFile
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.view.networkLogDetail.list.DetailsViewHolder
import com.pandulapeter.beagle.core.view.networkLogDetail.list.LineViewHolder
import com.pandulapeter.beagle.core.view.networkLogDetail.list.NetworkLogDetailListItem
import com.pandulapeter.beagle.core.view.networkLogDetail.list.TitleViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import kotlin.math.max
import kotlin.math.min

internal class NetworkLogDetailDialogViewModel(application: Application) : AndroidViewModel(application) {

    private val textHeaders = application.text(BeagleCore.implementation.appearance.networkLogTexts.headers)
    private val textNone = application.text(BeagleCore.implementation.appearance.networkLogTexts.none)
    private val textTimestamp = application.text(BeagleCore.implementation.appearance.networkLogTexts.timestamp)
    private val textDuration = application.text(BeagleCore.implementation.appearance.networkLogTexts.duration)
    private val _isProgressBarVisible = MutableLiveData(true)
    val isProgressBarVisible: LiveData<Boolean> = _isProgressBarVisible
    private val _areDetailsEnabled = MutableLiveData(false)
    val areDetailsEnabled: LiveData<Boolean> = _areDetailsEnabled
    private val _isShareButtonEnabled = MutableLiveData(false)
    val isShareButtonEnabled: LiveData<Boolean> = _isShareButtonEnabled
    private val _items = MutableLiveData(emptyList<NetworkLogDetailListItem>())
    val items: LiveData<List<NetworkLogDetailListItem>> = _items
    private var title = ""
    private var details = ""
    private var jsonLines = mutableListOf<Pair<String, Int>>()
    private var collapsedLineIndices = mutableListOf<Int>()

    init {
        areDetailsEnabled.observeForever {
            viewModelScope.launch { refreshUi() }
        }
    }

    fun formatJson(
        isOutgoing: Boolean,
        url: String,
        headers: List<String>,
        timestamp: Long?,
        duration: Long?,
        payload: String
    ) = viewModelScope.launch {
        title = "${if (isOutgoing) "↑" else "↓"} $url"
        details = "\n\n• ${textHeaders}:${headers.formatHeaders()}"
            .let { text -> timestamp?.let { text.append("\n• ${textTimestamp}: ${BeagleCore.implementation.appearance.networkEventTimestampFormatter(it)}") } ?: text }
            .let { text -> duration?.let { text.append("\n• ${textDuration}: ${max(0, it)} ms") } ?: text }
            .toString()
        jsonLines.addAll(payload.formatToJson().split("\n").map { line -> line to line.level })
        refreshUi()
        _isProgressBarVisible.postValue(false)
        _isShareButtonEnabled.postValue(true)
    }

    fun onToggleDetailsButtonPressed() {
        _areDetailsEnabled.value = !(areDetailsEnabled.value ?: true)
    }

    fun shareLogs(activity: Activity?, timestamp: Long, id: String) {
        if (_isShareButtonEnabled.value == true) {
            viewModelScope.launch {
                _isShareButtonEnabled.postValue(false)
                activity?.createAndShareLogFile(
                    fileName = "${BeagleCore.implementation.behavior.getNetworkLogFileName(timestamp, id)}.txt",
                    content = title.run { if (_areDetailsEnabled.value == true) append(details) else this }
                        .append("\n\n${jsonLines.joinToString("\n")}")
                        .toString()
                )
                _isShareButtonEnabled.postValue(true)
            }
        }
    }

    fun onItemClicked(lineIndex: Int) {
        viewModelScope.launch {
            if (collapsedLineIndices.contains(lineIndex)) {
                collapsedLineIndices.remove(lineIndex)
            } else {
                collapsedLineIndices.add(lineIndex)
            }
            refreshUi()
        }
    }

    private suspend fun refreshUi() = withContext(Dispatchers.Default) {
        _items.postValue(mutableListOf<NetworkLogDetailListItem>().apply {
            add(TitleViewHolder.UiModel(title))
            if (_areDetailsEnabled.value == true) {
                add(DetailsViewHolder.UiModel(details.trim()))
            }
            var levelToSkip = Int.MAX_VALUE
            val linesToAdd = jsonLines.mapIndexedNotNull { index, (line, level) ->
                if (collapsedLineIndices.contains(index)) {
                    levelToSkip = min(levelToSkip, level)
                }
                when {
                    line.level == levelToSkip -> {
                        if (!collapsedLineIndices.contains(index)) {
                            levelToSkip = Int.MAX_VALUE
                        }
                        ((if (line.level == levelToSkip) "$line ..." else line) to index to level)
                    }
                    line.level < levelToSkip -> line to index to level
                    else -> null
                }
            }
            addAll(linesToAdd.map { (line, level) ->
                LineViewHolder.UiModel(
                    lineIndex = line.second,
                    line = line.first,
                    isClickable = line.second != jsonLines.lastIndex && level < jsonLines[line.second + 1].second,
                    isCollapsed = collapsedLineIndices.contains(line.second)
                )
            })
        })
    }

    private fun List<String>.formatHeaders() = if (isNullOrEmpty()) " [${textNone}]" else joinToString("") { header -> "\n    • $header" }

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

    private val String.level get() = if (isEmpty() || get(0) != ' ') 0 else indexOfFirst { it != ' ' }

}