package com.pandulapeter.beagle.appDemo.data

import androidx.annotation.StringRes
import com.pandulapeter.beagle.appDemo.R
import java.util.UUID

enum class CaseStudy(
    val id: String = UUID.randomUUID().toString(),
    @StringRes val title: Int,
    @StringRes val description: Int,
    val isReady: Boolean = true
) {
    SIMPLE_SETUP(
        title = R.string.case_study_simple_setup_title,
        description = R.string.case_study_simple_setup_description
    ),
    STATIC_DATA(
        title = R.string.case_study_static_data_title,
        description = R.string.case_study_static_data_description
    ),
    FEATURE_TOGGLES(
        title = R.string.case_study_feature_toggles_title,
        description = R.string.case_study_feature_toggles_description
    ),
    AUTHENTICATION(
        title = R.string.case_study_authentication_title,
        description = R.string.case_study_authentication_description
    ),
    NETWORK_REQUEST_INTERCEPTOR(
        title = R.string.case_study_network_request_interceptor_title,
        description = R.string.case_study_network_request_interceptor_description,
        isReady = false
    ),
    ANALYTICS(
        title = R.string.case_study_analytics_title,
        description = R.string.case_study_analytics_description,
        isReady = false
    ),
    ENVIRONMENT_SWITCHER(
        title = R.string.case_study_environment_switcher_title,
        description = R.string.case_study_environment_switcher_description,
        isReady = false
    ),
    MOCK_DATA_GENERATOR(
        title = R.string.case_study_mock_data_generator_title,
        description = R.string.case_study_mock_data_generator_description,
        isReady = false
    ),
    BUG_REPORTER(
        title = R.string.case_study_bug_reporting_tool_title,
        description = R.string.case_study_bug_reporting_tool_description,
        isReady = false
    )
}