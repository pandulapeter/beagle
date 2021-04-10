package com.pandulapeter.beagle.core.view.networkLogDetail

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.core.util.extension.createAndShareLogFile
import com.pandulapeter.beagle.core.util.extension.jsonLevel
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.view.networkLogDetail.list.LineViewHolder
import com.pandulapeter.beagle.core.view.networkLogDetail.list.MetadataDetailsViewHolder
import com.pandulapeter.beagle.core.view.networkLogDetail.list.MetadataHeaderViewHolder
import com.pandulapeter.beagle.core.view.networkLogDetail.list.NetworkLogDetailListItem
import com.pandulapeter.beagle.core.view.networkLogDetail.list.TitleViewHolder
import com.pandulapeter.beagle.utils.mutableLiveDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.util.concurrent.Executors
import kotlin.math.max
import kotlin.math.min

internal class NetworkLogDetailDialogViewModel(application: Application) : AndroidViewModel(application) {

    private val _isProgressBarVisible = mutableLiveDataOf(true)
    val isProgressBarVisible: LiveData<Boolean> = _isProgressBarVisible
    private val _areTagsExpanded = mutableLiveDataOf(true)
    private val _shouldShowSearch = MutableStateFlow(false)
    val shouldShowSearch = _shouldShowSearch.asLiveData()
    val areTagsExpanded: LiveData<Boolean> = _areTagsExpanded
    private val _isShareButtonEnabled = mutableLiveDataOf(false)
    val isShareButtonEnabled: LiveData<Boolean> = _isShareButtonEnabled
    private val _items = mutableLiveDataOf(emptyList<NetworkLogDetailListItem>())
    val items: LiveData<List<NetworkLogDetailListItem>> = _items
    private val _longestLine = mutableLiveDataOf("")
    val longestLine: LiveData<String> = _longestLine
    private var title = ""
    private var metadata = ""
    private var jsonLines = emptyList<Line>()
    private var collapsedLineIndices = mutableListOf<Int>()
    private var hasCollapsingContent = false
    private var shouldShowMetadata = false
    private val tagManagerContext = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    val searchQuery = MutableStateFlow("")
    private val _matchCount = MutableStateFlow(0)
    val matchCount = _matchCount.asLiveData()
    val isCaseSensitive = MutableLiveData(true)
    var searchResultLineIndices = emptyList<Int>()
    private val currentResultLineIndex = MutableStateFlow(0)
    val scrollToPosition = currentResultLineIndex.map { currentResultLineIndex ->
        _items.value?.indexOfFirst { (it as? LineViewHolder.UiModel)?.lineIndex == currentResultLineIndex }?.let { result ->
            if (result == -1) 0 else result
        } ?: 0
    }.asLiveData()

    init {
        areTagsExpanded.observeForever { areDetailsEnabled ->
            viewModelScope.launch(tagManagerContext) {
                _isProgressBarVisible.postValue(true)
                shouldShowMetadata = areDetailsEnabled
                collapsedLineIndices.clear()
                if (!areDetailsEnabled) {
                    collapsedLineIndices.addAll(items.value?.filter { it is LineViewHolder.UiModel && it.isClickable }?.map { it.lineIndex }.orEmpty())
                }
                refreshUi()
                _isProgressBarVisible.postValue(false)
            }
        }
        isCaseSensitive.observeForever { viewModelScope.launch { refreshUi() } }
        searchQuery.onEach {
            refreshUi()
            onMatchCounterClicked()
        }.flowOn(Dispatchers.Default).launchIn(viewModelScope)
    }

    fun formatJson(
        isOutgoing: Boolean,
        url: String,
        headers: List<String>,
        timestamp: Long?,
        duration: Long?,
        payload: String
    ) = viewModelScope.launch(tagManagerContext) {
        title = createTitle(isOutgoing, url)
        metadata = createMetadata(getApplication(), headers, timestamp, duration)
        var longestLine = ""
        jsonLines = formatJson(payload, 1).split("\n").mapIndexed { index, line ->
            Line(index, line).also {
                if (line.length > longestLine.length) {
                    longestLine = line
                }
                if (it.level > 0) {
                    hasCollapsingContent = true
                }
            }
        }
        metadata.split("\n").forEach { line ->
            if (line.length > longestLine.length) {
                longestLine = line
            }
        }
        title.split("\n").first().let { firstTitleLine ->
            if (firstTitleLine.length > longestLine.length) {
                longestLine = firstTitleLine
            }
        }
        _longestLine.postValue(longestLine)
        refreshUi()
        _isShareButtonEnabled.postValue(true)
    }

    fun onMatchCounterClicked() {
        val lineIndices = searchResultLineIndices
        val currentScrollPosition = currentResultLineIndex.value
        currentResultLineIndex.value = if (currentScrollPosition == lineIndices.lastOrNull() ?: 0) {
            lineIndices.firstOrNull() ?: 0
        } else {
            val currentIndex = lineIndices.indexOf(currentScrollPosition)
            if (currentIndex == -1) {
                lineIndices.firstOrNull() ?: 0
            } else {
                lineIndices[currentIndex + 1]
            }
        }
    }

    fun onToggleSearchButtonPressed() {
        if (_isProgressBarVisible.value == false) {
            _shouldShowSearch.value = !_shouldShowSearch.value
            viewModelScope.launch { refreshUi() }
        }
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
                    fileName = "${BeagleCore.implementation.behavior.networkLogBehavior.getFileName(timestamp, id)}.txt",
                    content = createLogFileContents(
                        title = title,
                        metadata = metadata,
                        formattedJson = jsonLines.joinToString("\n") { line ->
                            var prefix = ""
                            repeat(line.level) {
                                prefix = "    $prefix"
                            }
                            "$prefix${line.content}"
                        })
                )
                _isShareButtonEnabled.postValue(true)
            }
        }
    }

    fun onHeaderClicked() {
        viewModelScope.launch(tagManagerContext) {
            if (_isProgressBarVisible.value == false) {
                shouldShowMetadata = !shouldShowMetadata
                refreshUi()
            }
        }
    }

    fun onItemClicked(lineIndex: Int) {
        viewModelScope.launch(tagManagerContext) {
            if (_isProgressBarVisible.value == false) {
                if (collapsedLineIndices.contains(lineIndex)) {
                    collapsedLineIndices.remove(lineIndex)
                } else {
                    collapsedLineIndices.add(lineIndex)
                }
                refreshUi()
            }
        }
    }

    private suspend fun refreshUi() = withContext(tagManagerContext) {
        if (jsonLines.isNotEmpty()) {
            _isProgressBarVisible.postValue(true)
            _items.postValue(mutableListOf<NetworkLogDetailListItem>().apply {
                add(TitleViewHolder.UiModel(title = title))
                add(MetadataHeaderViewHolder.UiModel(isCollapsed = !shouldShowMetadata))
                if (shouldShowMetadata) {
                    add(MetadataDetailsViewHolder.UiModel(metadata.trim()))
                }
                var levelToSkip = Int.MAX_VALUE
                var collapsedItemCount = 0
                lateinit var lastCollapsedLine: Line
                val linesToAdd = jsonLines.mapNotNull { line ->
                    if (collapsedLineIndices.contains(line.index)) {
                        levelToSkip = min(levelToSkip, line.level)
                    }
                    when {
                        line.level == levelToSkip -> {
                            if (!collapsedLineIndices.contains(line.index)) {
                                levelToSkip = Int.MAX_VALUE
                            }
                            if (line.level == levelToSkip) {
                                line.copy(content = line.content).also {
                                    lastCollapsedLine = it
                                    collapsedItemCount = 0
                                }
                            } else {
                                lastCollapsedLine.content += " ${if (line.content.startsWith("]")) collapsedItemCount else "…"} ${line.content}"
                                null
                            }
                        }
                        line.level < levelToSkip -> line
                        else -> null.also {
                            if (line.level == levelToSkip + 1 && line.content.startsWith("{")) {
                                collapsedItemCount++
                            }
                        }
                    }
                }
                val searchQueryRegex = searchQuery.value.let {
                    if (!_shouldShowSearch.value || it.isEmpty()) null else {
                        if (isCaseSensitive.value != true) {
                            it.toRegex(setOf(RegexOption.LITERAL, RegexOption.IGNORE_CASE))
                        } else {
                            it.toRegex(RegexOption.LITERAL)
                        }
                    }
                }
                _matchCount.value = 0
                val newLineIndices = mutableListOf<Int>()
                addAll(
                    linesToAdd.map { line ->
                        LineViewHolder.UiModel(
                            lineIndex = line.index,
                            content = line.content.let { content ->
                                searchQueryRegex?.let { regex ->
                                    val searchQueryLength = regex.pattern.length
                                    SpannableString(content).apply {
                                        val matches = regex.findAll(content).map { it.range.first }.toList()
                                        _matchCount.value += matches.size
                                        if (matches.isNotEmpty()) {
                                            setSpan(StyleSpan(Typeface.BOLD), 0, content.length - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                                            newLineIndices.add(line.index)
                                        }
                                        matches.forEach { startIndex ->
                                            setSpan(UnderlineSpan(), startIndex, startIndex + searchQueryLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                                        }
                                    }
                                } ?: content
                            },
                            level = line.level,
                            hasCollapsingContent = hasCollapsingContent,
                            isClickable = line.index != jsonLines.lastIndex && line.level < jsonLines[line.index + 1].level,
                            isCollapsed = collapsedLineIndices.contains(line.index),
                        )
                    }
                )
                searchResultLineIndices = newLineIndices
            })
            _isProgressBarVisible.postValue(false)
        }
    }

    private data class Line(
        val index: Int,
        var content: String,
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

    companion object {
        private const val TITLE_NEW_LINE_PREFIX = "\n        "

        fun createTitle(
            isOutgoing: Boolean,
            url: String
        ) = "${if (isOutgoing) "↑" else "↓"} ${url.replace("?", "$TITLE_NEW_LINE_PREFIX?").replace("&", "$TITLE_NEW_LINE_PREFIX&")}"

        fun createMetadata(
            context: Context,
            headers: List<String>,
            timestamp: Long?,
            duration: Long?
        ): String {
            val textHeaders = context.text(BeagleCore.implementation.appearance.networkLogTexts.headers)
            val textTimestamp = context.text(BeagleCore.implementation.appearance.networkLogTexts.timestamp)
            val textDuration = context.text(BeagleCore.implementation.appearance.networkLogTexts.duration)
            return "\n\n• ${textHeaders}:${headers.formatHeaders(context)}"
                .let { text -> timestamp?.let { text.append("\n• ${textTimestamp}: ${BeagleCore.implementation.appearance.logLongTimestampFormatter(it)}") } ?: text }
                .let { text -> duration?.let { text.append("\n• ${textDuration}: ${max(0, it)} ms") } ?: text }
                .toString()
        }

        fun formatJson(json: String, indentation: Int) = try {
            if (json.isJson()) {
                val obj = JSONTokener(json).nextValue()
                if (obj is JSONObject) obj.toString(indentation) else (obj as? JSONArray)?.toString(indentation) ?: (obj as String)
            } else json
        } catch (e: JSONException) {
            json
        } ?: json

        fun createLogFileContents(
            title: String,
            metadata: String,
            formattedJson: String
        ) = title.replace(TITLE_NEW_LINE_PREFIX, "")
            .append(metadata)
            .append("\n\n$formattedJson")
            .toString()

        private fun List<String>.formatHeaders(context: Context) =
            if (isNullOrEmpty()) " [${context.text(BeagleCore.implementation.appearance.networkLogTexts.none)}]" else joinToString("") { header -> "\n    • $header" }

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
}