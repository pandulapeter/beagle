package com.pandulapeter.beagle.core.util

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract

internal data class LogEntry(
    override val id: String,
    val label: String?,
    val message: CharSequence,
    val payload: CharSequence?,
    val timestamp: Long
) : BeagleListItemContract {

    override val title = Text.CharSequence(message)

    fun getFormattedContents(timestampFormatter: (Long) -> CharSequence): CharSequence = "[${timestampFormatter(timestamp)}] $message".let { text ->
        SpannableString(payload.let { if (it == null) text else "$text\n\n$it" }).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, text.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }
}