package com.pandulapeter.beagle.core.manager

import android.app.Application
import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity

internal class BugReportManager {

    var onBugReportReady: ((Uri?) -> Unit)? = null
    private val currentActivity get() = BeagleCore.implementation.currentActivity

    fun openBugReportingScreen(
        shouldShowGallerySection: Boolean,
        shouldShowNetworkLogsSection: Boolean,
        logLabelSectionsToShow: List<String?>,
        shouldShowMetadataSection: Boolean,
        buildInformation: (Application?) -> List<Pair<Text, String>>,
        descriptionTemplate: Text,
        onBugReportReady: ((Uri?) -> Unit)?
    ) {
        this.onBugReportReady = onBugReportReady
        currentActivity?.run {
            startActivity(
                BugReportActivity.newIntent(
                    context = this,
                    shouldShowGallerySection = shouldShowGallerySection,
                    shouldShowNetworkLogsSection = shouldShowNetworkLogsSection,
                    logLabelSectionsToShow = logLabelSectionsToShow,
                    shouldShowMetadataSection = shouldShowMetadataSection,
                    buildInformation = buildInformation(currentActivity?.application).generateBuildInformation(),
                    descriptionTemplate = descriptionTemplate
                )
            )
        }
    }

    private fun List<Pair<Text, String>>.generateBuildInformation(): CharSequence {
        var text: CharSequence = ""
        forEachIndexed { index, (keyText, value) ->
            val key = currentActivity?.text(keyText) ?: ""
            if (key.isNotBlank() && value.isNotBlank()) {
                text = text.append(SpannableString("$key: $value".let { if (index == lastIndex) it else "$it\n" }).apply {
                    setSpan(StyleSpan(Typeface.BOLD), 0, key.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                })
            }
        }
        return text
    }
}