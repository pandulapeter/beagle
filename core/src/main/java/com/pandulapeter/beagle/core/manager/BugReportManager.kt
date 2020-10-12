package com.pandulapeter.beagle.core.manager

import android.content.Intent
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.performOnHide
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity

internal class BugReportManager {

    private val currentActivity get() = BeagleCore.implementation.currentActivity

    fun openBugReportingScreen() = performOnHide { currentActivity?.run { startActivity(Intent(this, BugReportActivity::class.java)) } }
}