package com.pandulapeter.beagle.core.util.model

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CrashLogEntry(
    @Json(name = "id") override val id: String,
    @Json(name = "exception") val exception: String,
    @Json(name = "stacktrace") val stacktrace: String,
    @Json(name = "timestamp") val timestamp: Long
) : BeagleListItemContract {

    override val title: Text = exception.toText()

    fun getFormattedTitle(formatter: (Long) -> CharSequence): String = "[${formatter(timestamp)}] $exception"

    fun getFormattedContents(formatter: (Long) -> CharSequence): CharSequence = getFormattedTitle(formatter).let { text ->
        SpannableString(stacktrace.let { "$text\n\n$it" }).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, text.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }
}