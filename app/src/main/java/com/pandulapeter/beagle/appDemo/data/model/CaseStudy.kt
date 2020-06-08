package com.pandulapeter.beagle.appDemo.data.model

import androidx.annotation.StringRes
import java.util.UUID

data class CaseStudy(
    val id: String = UUID.randomUUID().toString(),
    @StringRes val title: Int,
    @StringRes val description: Int
)