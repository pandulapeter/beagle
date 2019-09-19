package com.pandulapeter.debugMenu.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
internal data class NetworkEvent(
    val id: String = UUID.randomUUID().toString(),
    val isOutgoing: Boolean,
    val url: String,
    val body: String,
    val timestamp: Long = System.currentTimeMillis(),
    val duration: Long? = null
) : Parcelable