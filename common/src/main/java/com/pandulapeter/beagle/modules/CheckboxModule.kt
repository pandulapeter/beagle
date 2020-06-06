package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.PersistableModule
import java.util.UUID

/**
 * Displays a simple checkbox.
 *
 * @param id - A unique identifier for the module. Must be a unique constant for the save / load feature to work (see [shouldBePersisted]]). Optional, random string by default.
 * @param text - The text to display on the switch.
 * @param color - The resolved color for the text. Optional, color from theme is used by default.
 * @param initialValue - Whether or not the checkbox is checked initially. Optional, false by default. If [shouldBePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param shouldBePersisted - Can be used to enable or disable persisting the value on the local storage. This will only work if the module has a unique, constant ID. Optional, false by default.
 * @param onValueChanged - Callback triggered when the user toggles the switch. TODO: In case of persisted values, this will also get called the first time the module is added.
 */
data class CheckboxModule(
    override val id: String = "switch_${UUID.randomUUID()}",
    val text: CharSequence,
    @ColorInt val color: Int? = null,
    override val initialValue: Boolean = false,
    override val shouldBePersisted: Boolean = false,
    override val onValueChanged: (Boolean) -> Unit
) : PersistableModule<Boolean, CheckboxModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}