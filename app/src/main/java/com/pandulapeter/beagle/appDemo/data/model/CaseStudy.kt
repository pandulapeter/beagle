package com.pandulapeter.beagle.appDemo.data.model

import androidx.annotation.StringRes
import com.pandulapeter.beagle.appDemo.R
import java.util.UUID

enum class CaseStudy(
    val id: String = UUID.randomUUID().toString(),
    @StringRes val title: Int,
    @StringRes val description: Int
) {

    BASIC_SETUP(
        title = R.string.case_study_basic_setup_title,
        description = R.string.case_study_basic_setup_description
    ),
    AUTHENTICATION(
        title = R.string.case_study_authentication_title,
        description = R.string.case_study_authentication_description
    )
}