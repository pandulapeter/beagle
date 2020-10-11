package com.pandulapeter.beagle.core.util.extension

import android.widget.TextView
import com.pandulapeter.beagle.common.configuration.Text

internal fun TextView.setText(text: Text?) = text?.let {
    this.text = context.text(text)
} ?: Unit