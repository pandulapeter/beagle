package com.pandulapeter.beagle.appDemo.data.model

import androidx.annotation.StringRes
import com.pandulapeter.beagle.appDemo.R

enum class CaseStudy(
    @StringRes val title: Int,
    @StringRes val description: Int,
    val isReady: Boolean = true,
    val id: String = "caseStudy_$title"
) {
    SIMPLE_SETUP(
        title = R.string.case_study_simple_setup_title,
        description = R.string.case_study_simple_setup_description
    ),
    STATIC_DATA(
        title = R.string.case_study_static_data_title,
        description = R.string.case_study_static_data_description
    ),
    FEATURE_FLAGS(
        title = R.string.case_study_feature_flags_title,
        description = R.string.case_study_feature_flags_description
    ),
    NETWORK_REQUEST_INTERCEPTOR(
        title = R.string.case_study_network_request_interceptor_title,
        description = R.string.case_study_network_request_interceptor_description
    ),
    ANALYTICS(
        title = R.string.case_study_analytics_title,
        description = R.string.case_study_analytics_description
    ),
    AUTHENTICATION(
        title = R.string.case_study_authentication_title,
        description = R.string.case_study_authentication_description
    ),
    MOCK_DATA_GENERATOR(
        title = R.string.case_study_mock_data_generator_title,
        description = R.string.case_study_mock_data_generator_description
    ),
    OVERLAY(
        title = R.string.case_study_overlay_title,
        description = R.string.case_study_overlay_description
    ),
    BUG_REPORTER(
        title = R.string.case_study_bug_reporting_tool_title,
        description = R.string.case_study_bug_reporting_tool_description,
        isReady = false
    )
}