package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity

internal class BugReportManager {

    fun openBugReportingScreen() {
        BeagleCore.implementation.currentActivity?.run {
            startActivity(BugReportActivity.newIntent(this, ""))
        }
    }
}