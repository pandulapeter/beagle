package com.pandulapeter.beagle.core.util.extension

import android.widget.TextView
import com.pandulapeter.beagle.common.configuration.Text

internal fun TextView.setText(text: Text) = when (text) {
    is Text.CharSequence -> this.text = text.charSequence
    is Text.ResourceId -> setText(text.resourceId)
}