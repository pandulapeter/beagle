package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.PersistableModule
import java.util.UUID

/**
 * Allows the user to adjust a numeric value.
 *
 * @param id - A unique identifier for the module. Must be a unique constant for the save / load feature to work (see [isPersisted]]). Optional, random string by default.
 * @param text - A lambda that returns the name that should appear above the slider in function of its current value.
 * @param minimumValue - The smallest value supported by the slider. 0 by default.
 * @param maximumValue - The largest value supported by the slider. 10 by default.
 * @param initialValue - The initial value of the slider. By default it's the same as the slider's minimum value. If [isPersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param isPersisted - Can be used to enable or disable persisting the value on the local storage. This will only work if the module has a unique, constant ID. Optional, false by default.
 * @param shouldRequireConfirmation - Can be used to enable or disable bulk apply. When enabled, changes made to the module by the user only take effect after a confirmation step. Optional, false by default.
 * @param onValueChanged - Callback triggered when the user changes the current value. In case of persisted values, this will also get called the first time the module is added.
 */
data class SliderModule(
    override val id: String = UUID.randomUUID().toString(),
    val text: (Int) -> CharSequence,
    val minimumValue: Int = 0,
    val maximumValue: Int = 10,
    override val initialValue: Int = minimumValue,
    override val isPersisted: Boolean = false,
    override val shouldRequireConfirmation: Boolean = false,
    override val onValueChanged: (Int) -> Unit
) : PersistableModule<Int, SliderModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}