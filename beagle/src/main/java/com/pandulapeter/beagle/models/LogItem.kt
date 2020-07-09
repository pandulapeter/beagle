package com.pandulapeter.beagle.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
@Parcelize
internal data class LogItem(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val message: String,
    val tag: String?,
    val payload: String?
) : Parcelable