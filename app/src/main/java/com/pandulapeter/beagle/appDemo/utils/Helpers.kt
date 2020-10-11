package com.pandulapeter.beagle.appDemo.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pandulapeter.beagle.modules.LongTextModule
import com.pandulapeter.beagle.modules.TextModule

fun createTextModule(
    @StringRes textResourceId: Int,
    @DrawableRes icon: Int? = null,
    id: String = "text_$textResourceId"
) = TextModule(
    id = id,
    text = textResourceId,
    icon = icon
)

fun createSectionHeaderModule(
    @StringRes titleResourceId: Int,
    id: String = "sectionHeader_$titleResourceId"
) = TextModule(
    id = id,
    text = titleResourceId,
    type = TextModule.Type.SECTION_HEADER
)

fun createButtonModule(
    @StringRes titleResourceId: Int,
    onItemSelected: () -> Unit
) = TextModule(
    id = "button_$titleResourceId",
    text = titleResourceId,
    type = TextModule.Type.BUTTON,
    onItemSelected = onItemSelected
)

fun createLongTextModule(
    @StringRes titleResourceId: Int,
    @StringRes textResourceId: Int,
    id: String = "text_$textResourceId"
) = LongTextModule(
    id = id,
    title = titleResourceId,
    text = textResourceId
)