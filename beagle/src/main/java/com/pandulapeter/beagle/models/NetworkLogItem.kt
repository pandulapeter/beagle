package com.pandulapeter.beagle.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
internal data class NetworkLogItem(
    val id: String = UUID.randomUUID().toString(),
    val isOutgoing: Boolean,
    val url: String,
    val body: String,
    val headers: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis(),
    val duration: Long? = null
) : Parcelable