package com.pandulapeter.beagle.core.util.extension

import android.text.TextUtils

internal fun CharSequence.append(charSequence: CharSequence) = TextUtils.concat(this, charSequence) ?: "$this$charSequence"

internal val CharSequence.jsonLevel get() = if (isEmpty() || get(0) != ' ') 0 else indexOfFirst { it != ' ' }