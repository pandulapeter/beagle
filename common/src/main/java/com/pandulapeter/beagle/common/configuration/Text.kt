package com.pandulapeter.beagle.common.configuration

import androidx.annotation.StringRes

/**
 * Used as a generic way to provide texts, either as CharSequence or resource ID.
 */
sealed class Text {

    data class CharSequence(val charSequence: kotlin.CharSequence) : Text()

    data class ResourceId(@StringRes val resourceId: Int) : Text()
}