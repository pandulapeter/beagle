package com.pandulapeter.beagle.appDemo.data

import com.pandulapeter.beagle.appDemo.data.model.CaseStudy

class CaseStudyRepository {

    val dataSet = (0..9).map { index ->
        CaseStudy(
            title = "Case Study ${index + 1}",
            description = "Description for case study ${index + 1}"
        )
    }
}