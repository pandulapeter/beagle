package com.pandulapeter.beagle.appDemo.data

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
    ),
    FEATURE_TOGGLES(
        title = R.string.case_study_feature_toggles_title,
        description = R.string.case_study_feature_toggles_description
    ),
    ANALYTICS(
        title = R.string.case_study_analytics_title,
        description = R.string.case_study_analytics_description
    ),
    NETWORK_REQUEST_INTERCEPTOR(
        title = R.string.case_study_network_request_interceptor_title,
        description = R.string.case_study_network_request_interceptor_description
    ),
    ENVIRONMENT_SWITCHER(
        title = R.string.case_study_environment_switcher_title,
        description = R.string.case_study_environment_switcher_description
    ),
    MOCK_DATA_GENERATOR(
        title = R.string.case_study_mock_data_generator_title,
        description = R.string.case_study_mock_data_generator_description
    ),
    BUG_REPORTER(
        title = R.string.case_study_bug_reporting_tool_title,
        description = R.string.case_study_bug_reporting_tool_description
    )
}