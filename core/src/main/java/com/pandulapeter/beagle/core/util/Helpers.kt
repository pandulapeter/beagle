package com.pandulapeter.beagle.core.util

import androidx.annotation.DrawableRes
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.listeners.VisibilityListener
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.modules.TextModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

internal fun performOnHide(action: () -> Unit) {
    val listener = object : VisibilityListener {
        override fun onHidden() {
            val reference = this
            action()
            GlobalScope.launch {
                delay(100)
                BeagleCore.implementation.removeVisibilityListener(reference)
            }
        }
    }
    if (BeagleCore.implementation.hide()) {
        BeagleCore.implementation.addInternalVisibilityListener(listener)
    } else {
        listener.onHidden()
    }
}

internal fun createTextModuleFromType(
    type: TextModule.Type,
    id: String,
    text: Text,
    isEnabled: Boolean,
    @DrawableRes icon: Int?,
    onItemSelected: (() -> Unit)?
) = when (type) {
    TextModule.Type.NORMAL,
    TextModule.Type.SECTION_HEADER -> TextCell(
        id = id,
        text = text,
        isEnabled = isEnabled,
        isSectionHeader = type == TextModule.Type.SECTION_HEADER,
        icon = icon,
        onItemSelected = onItemSelected
    )
    TextModule.Type.BUTTON -> ButtonCell(
        id = id,
        text = text,
        isEnabled = isEnabled,
        icon = icon,
        onButtonPressed = onItemSelected
    )
}

internal fun getFolder(rootFolder: File, name: String): File {
    val folder = File(rootFolder, name)
    folder.mkdirs()
    return folder
}