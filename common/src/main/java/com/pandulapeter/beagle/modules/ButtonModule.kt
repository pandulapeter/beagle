package com.pandulapeter.beagle.modules

import androidx.annotation.StringRes
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.Module


/**
 * Displays a simple button.
 *
 * @param text - The text to display on the button.
 * @param id - A unique identifier for the module. [BeagleContract.randomId] by default.
 * @param onButtonPressed - Callback invoked when the user presses the button.
 */
@Suppress("unused")
@Deprecated("Use a TextModule with the TextModule.Type.BUTTON type instead - this class will be removed.")
data class ButtonModule(
    val text: Text,
    override val id: String = BeagleContract.randomId,
    val onButtonPressed: () -> Unit
) : Module<ButtonModule> {

    constructor(
        text: CharSequence,
        id: String = BeagleContract.randomId,
        onButtonPressed: () -> Unit
    ) : this(
        text = Text.CharSequence(text),
        id = id,
        onButtonPressed = onButtonPressed
    )

    constructor(
        @StringRes text: Int,
        id: String = BeagleContract.randomId,
        onButtonPressed: () -> Unit
    ) : this(
        text = Text.ResourceId(text),
        id = id,
        onButtonPressed = onButtonPressed
    )
}