package com.pandulapeter.beagle.appDemo.data

import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.model.CaseStudy

class CaseStudyRepository {

    val dataSet = listOf(
        CaseStudy(
            title = R.string.case_study_authentication_title,
            description = R.string.case_study_authentication_description
        )
    )
}