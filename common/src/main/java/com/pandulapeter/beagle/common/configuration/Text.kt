package com.pandulapeter.beagle.common.configuration

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

/**
 * Used as a generic way to provide texts, either as CharSequence or resource ID.
 *
 * Resource ID-s are resolved again with the current context every time the View is inflated to achieve localization support.
 */
sealed class Text : Parcelable {

    @Parcelize
    data class CharSequence(val charSequence: kotlin.CharSequence) : Text()

    @Parcelize
    data class ResourceId(@StringRes val resourceId: Int) : Text()

    /**
     * For internal use only.
     */
    var suffix: kotlin.CharSequence = ""
        private set

    /**
     * For internal use only.
     */
    fun withSuffix(suffix: kotlin.CharSequence) = this.also {
        this.suffix = suffix
    }
}