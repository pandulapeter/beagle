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
import com.pandulapeter.beagle.core.util.extension.jsonLevel
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.view.networkLogDetail.list.DetailsViewHolder
import com.pandulapeter.beagle.core.view.networkLogDetail.list.HeaderViewHolder
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
    private val _areTagsExpanded = MutableLiveData(true)
    val areTagsExpanded: LiveData<Boolean> = _areTagsExpanded
    private val _isShareButtonEnabled = MutableLiveData(false)
    val isShareButtonEnabled: LiveData<Boolean> = _isShareButtonEnabled
    private val _items = MutableLiveData(emptyList<NetworkLogDetailListItem>())
    val items: LiveData<List<NetworkLogDetailListItem>> = _items
    private val _longestLine = MutableLiveData("")
    val longestLine: LiveData<String> = _longestLine
    private var title = ""
    private var details = ""
    private var jsonLines = emptyList<Line>()
    private var collapsedLineIndices = mutableListOf<Int>()
    private var hasCollapsingContent = false
    private var shouldShowMetadata = false
    private val metadataText = BeagleCore.implementation.appearance.networkLogTexts.metadata

    init {
        areTagsExpanded.observeForever { areDetailsEnabled ->
            viewModelScope.launch {
                shouldShowMetadata = areDetailsEnabled
                collapsedLineIndices.clear()
                if (!areDetailsEnabled) {
                    collapsedLineIndices.addAll(items.value?.filter { it is LineViewHolder.UiModel && it.isClickable }?.map { it.lineIndex }.orEmpty())
                }
                refreshUi()
            }
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
        var longestLine = ""
        jsonLines = payload.formatToJson().split("\n").mapIndexed { index, line ->
            Line(index, line).also {
                if (line.length > longestLine.length) {
                    longestLine = line
                }
                if (it.level > 1) {
                    hasCollapsingContent = true
                }
            }
        }
        details.split("\n").forEach { line ->
            if (line.length > longestLine.length) {
                longestLine = line
            }
        }
        if (title.length > longestLine.length) {
            longestLine = title
        }
        _longestLine.postValue(longestLine)
        refreshUi()
        _isProgressBarVisible.postValue(false)
        _isShareButtonEnabled.postValue(true)
    }

    fun onToggleDetailsButtonPressed() {
        if (_isProgressBarVisible.value == false) {
            _areTagsExpanded.value = !(areTagsExpanded.value ?: true)
        }
    }

    fun shareLogs(activity: Activity?, timestamp: Long, id: String) {
        if (_isProgressBarVisible.value == false && _isShareButtonEnabled.value == true) {
            viewModelScope.launch {
                _isShareButtonEnabled.postValue(false)
                activity?.createAndShareLogFile(
                    fileName = "${BeagleCore.implementation.behavior.getNetworkLogFileName(timestamp, id)}.txt",
                    content = title.run { if (shouldShowMetadata) append(details) else this }
                        .append(
                            "\n\n${
                                jsonLines.joinToString("\n") { line ->
                                    var prefix = ""
                                    repeat(line.level) {
                                        prefix = "    $prefix"
                                    }
                                    "$prefix${line.content}"
                                }
                            }"
                        )
                        .toString()
                )
                _isShareButtonEnabled.postValue(true)
            }
        }
    }

    fun onHeaderClicked() {
        viewModelScope.launch {
            if (_isProgressBarVisible.value == false) {
                shouldShowMetadata = !shouldShowMetadata
                refreshUi()
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
            add(TitleViewHolder.UiModel(title = title))
            add(
                HeaderViewHolder.UiModel(
                    lineIndex = -300,
                    content = metadataText,
                    isCollapsed = !shouldShowMetadata
                )
            )
            if (shouldShowMetadata) {
                add(DetailsViewHolder.UiModel(details.trim()))
            }
            var levelToSkip = Int.MAX_VALUE
            val linesToAdd = jsonLines.mapNotNull { line ->
                if (collapsedLineIndices.contains(line.index)) {
                    levelToSkip = min(levelToSkip, line.level)
                }
                when {
                    line.level == levelToSkip -> {
                        if (!collapsedLineIndices.contains(line.index)) {
                            levelToSkip = Int.MAX_VALUE
                        }
                        if (line.level == levelToSkip) line.copy(content = "${line.content} …") else line
                    }
                    line.level < levelToSkip -> line
                    else -> null
                }
            }
            addAll(linesToAdd.map { line ->
                LineViewHolder.UiModel(
                    lineIndex = line.index,
                    content = line.content,
                    level = line.level,
                    hasCollapsingContent = hasCollapsingContent,
                    isClickable = line.level != 0 && line.index != jsonLines.lastIndex && line.level < jsonLines[line.index + 1].level,
                    isCollapsed = collapsedLineIndices.contains(line.index)
                )
            })
        })
    }

    private fun List<String>.formatHeaders() = if (isNullOrEmpty()) " [${textNone}]" else joinToString("") { header -> "\n    • $header" }

    private fun String.formatToJson() = try {
        if (isJson()) {
            val obj = JSONTokener(this).nextValue()
            if (obj is JSONObject) obj.toString(1) else (obj as? JSONArray)?.toString(1) ?: (obj as String)
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

    private data class Line(
        val index: Int,
        val content: String,
        val level: Int
    ) {

        constructor(
            index: Int,
            formattedContent: String
        ) : this(
            index = index,
            content = formattedContent.trim(),
            level = formattedContent.jsonLevel
        )
    }
}