package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.util.createTextModuleFromType
import com.pandulapeter.beagle.core.util.performOnHide
import com.pandulapeter.beagle.modules.BugReportButtonModule

internal class BugReportButtonDelegate : Module.Delegate<BugReportButtonModule> {

    override fun createCells(module: BugReportButtonModule): List<Cell<*>> = listOf(
        createTextModuleFromType(
            type = module.type,
            id = module.id,
            text = module.text,
            isEnabled = module.isEnabled,
            icon = module.icon,
            onItemSelected = {
                module.onButtonPressed()
                performOnHide {
                    BeagleCore.implementation.openBugReportingScreen(
                        shouldShowGallerySection = module.shouldShowGallerySection,
                        shouldShowNetworkLogsSection = module.shouldShowNetworkLogsSection,
                        logLabelSectionsToShow = module.logLabelSectionsToShow,
                        shouldShowMetadataSection = module.shouldShowMetadataSection,
                        buildInformation = module.buildInformation,
                        descriptionTemplate = module.descriptionTemplate,
                        onBugReportReady = module.onBugReportReady
                    )
                }
            }
        )
    )
}