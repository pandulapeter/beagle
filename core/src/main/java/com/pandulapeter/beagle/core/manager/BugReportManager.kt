package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.performOnHide
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity

internal class BugReportManager {

    private val currentActivity get() = BeagleCore.implementation.currentActivity

    fun openBugReportingScreen(
        shouldShowGallerySection: Boolean,
        shouldShowNetworkLogsSection: Boolean,
        logTagSectionsToShow: List<String?>,
        descriptionTemplate: String
    ) = performOnHide {
        currentActivity?.run {
            startActivity(
                BugReportActivity.newIntent(
                    context = this,
                    shouldShowGallerySection = shouldShowGallerySection,
                    shouldShowNetworkLogsSection = shouldShowNetworkLogsSection,
                    logTagSectionsToShow = logTagSectionsToShow,
                    descriptionTemplate = descriptionTemplate
                )
            )
        }
    }
}