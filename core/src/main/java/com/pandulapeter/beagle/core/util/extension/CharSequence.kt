package com.pandulapeter.beagle.core.util.extension

import android.text.TextUtils

internal fun CharSequence.append(charSequence: CharSequence) = TextUtils.concat(this, charSequence) ?: "$this$charSequence"