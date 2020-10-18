package com.pandulapeter.beagle.core.manager

import android.net.Uri
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.performOnHide
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity

internal class BugReportManager {

    private val currentActivity get() = BeagleCore.implementation.currentActivity

    fun openBugReportingScreen(
        shouldShowGallerySection: Boolean,
        shouldShowNetworkLogsSection: Boolean,
        logLabelSectionsToShow: List<String?>,
        shouldShowMetadataSection: Boolean,
        descriptionTemplate: String,
        onBugReportReady: ((Uri) -> Unit)?
    ) = performOnHide {
        //TODO: Handle onBugReportReady
        currentActivity?.run {
            startActivity(
                BugReportActivity.newIntent(
                    context = this,
                    shouldShowGallerySection = shouldShowGallerySection,
                    shouldShowNetworkLogsSection = shouldShowNetworkLogsSection,
                    logLabelSectionsToShow = logLabelSectionsToShow,
                    shouldShowMetadataSection = shouldShowMetadataSection,
                    descriptionTemplate = descriptionTemplate
                )
            )
        }
    }
}