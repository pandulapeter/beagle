package com.pandulapeter.beagle.core.util.model

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.commonBase.model.LogEntry
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SerializableLogEntry(
    @Json(name = "id") override val id: String,
    @Json(name = "label") val label: String?,
    @Json(name = "message") val message: String,
    @Json(name = "payload") val payload: String?,
    @Json(name = "timestamp") val timestamp: Long
) : BeagleListItemContract {

    override val title = message.toText()

    fun getFormattedContents(timestampFormatter: (Long) -> CharSequence): CharSequence = "[${timestampFormatter(timestamp)}] $message".let { text ->
        SpannableString(payload.let { if (it == null) text else "$text\n\n$it" }).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, text.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }

    fun toLogEntry() = LogEntry(
        id = id,
        label = label,
        message = message,
        payload = payload,
        timestamp = timestamp
    )
}