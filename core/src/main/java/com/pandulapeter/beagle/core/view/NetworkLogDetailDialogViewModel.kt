package com.pandulapeter.beagle.core.view

import android.app.Application
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.core.util.extension.text
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import kotlin.math.max

internal class NetworkLogDetailDialogViewModel(application: Application) : AndroidViewModel(application) {

    private val textHeaders = application.text(BeagleCore.implementation.appearance.networkLogTexts.headers)
    private val textNone = application.text(BeagleCore.implementation.appearance.networkLogTexts.none)
    private val textTimestamp = application.text(BeagleCore.implementation.appearance.networkLogTexts.timestamp)
    private val textDuration = application.text(BeagleCore.implementation.appearance.networkLogTexts.duration)
    private val _isProgressBarVisible = MutableLiveData(true)
    val isProgressBarVisible: LiveData<Boolean> = _isProgressBarVisible
    private val _areDetailsEnabled = MutableLiveData(false)
    val areDetailsEnabled: LiveData<Boolean> = _areDetailsEnabled
    private val _formattedContents = MutableLiveData<CharSequence>("")
    val formattedContents: LiveData<CharSequence> = _formattedContents
    private var title: CharSequence = ""
    private var details: CharSequence = ""
    private var formattedJson: CharSequence = ""

    init {
        areDetailsEnabled.observeForever { refreshUi() }
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
        formattedJson = payload.formatToJson()
        refreshUi()
        _isProgressBarVisible.postValue(false)
    }

    fun onToggleDetailsButtonPressed() {
        _areDetailsEnabled.value = !(areDetailsEnabled.value ?: true)
    }

    private fun refreshUi() = _formattedContents.postValue(SpannableString(
        title.run { if (_areDetailsEnabled.value == true) append(details) else this }
            .append("\n\n${formattedJson}")
    ).apply { setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE) })

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
}