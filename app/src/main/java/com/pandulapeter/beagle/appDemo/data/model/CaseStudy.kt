package com.pandulapeter.beagle.appDemo.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class CaseStudy(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String
) : Parcelable