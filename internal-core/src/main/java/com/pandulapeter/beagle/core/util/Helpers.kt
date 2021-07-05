package com.pandulapeter.beagle.core.util

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.annotation.DrawableRes
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.core.list.cells.SectionHeaderCell
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.core.list.delegates.DeviceInfoDelegate
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.modules.TextModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

inline fun runOnUiThread(crossinline action: () -> Any?) {
    GlobalScope.launch(Dispatchers.Main) { action() }
}

internal fun createTextModuleFromType(
    type: TextModule.Type,
    id: String,
    text: Text,
    isEnabled: Boolean,
    @DrawableRes icon: Int?,
    onItemSelected: (() -> Unit)?
) = when (type) {
    TextModule.Type.NORMAL -> TextCell(
        id = id,
        text = text,
        isEnabled = isEnabled,
        icon = icon,
        onItemSelected = onItemSelected
    )
    TextModule.Type.SECTION_HEADER -> SectionHeaderCell(
        id = id,
        text = text,
        isEnabled = isEnabled,
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

fun List<Uri>.toPaths(folder: File): List<String> = folder.canonicalPath.let { path -> map { "$path/${it.realPath}" } }

val Uri.realPath get() = path?.split("/")?.lastOrNull()

internal fun List<Pair<Text, String>>.generateBuildInformation(context: Context): CharSequence {
    var text: CharSequence = ""
    forEachIndexed { index, (keyText, value) ->
        val key = context.text(keyText)
        if (key.isNotBlank() && value.isNotBlank()) {
            text = text.append(SpannableString("$key: $value".let { if (index == lastIndex) it else "$it\n" }).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, key.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            })
        }
    }
    return text
}

internal fun generateDeviceInformation(context: Context): CharSequence {
    var text: CharSequence = ""
    //TODO: Should be configurable
    DeviceInfoDelegate.getDeviceInfo(
        shouldShowManufacturer = true,
        shouldShowModel = true,
        shouldShowResolutionsPx = true,
        shouldShowResolutionsDp = true,
        shouldShowDensity = true,
        shouldShowAndroidVersion = true
    ).also { sections ->
        val lastIndex = sections.lastIndex
        sections.forEachIndexed { index, (keyText, value) ->
            val key = context.text(keyText)
            text = text.append(SpannableString("$key: $value".let { if (index == lastIndex) it else "$it\n" }).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, key.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            })
        }
    }
    return text
}