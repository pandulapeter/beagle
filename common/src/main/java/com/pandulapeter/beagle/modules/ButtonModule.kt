package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module


/**
 * Displays a simple button.
 *
 * @param text - The text to display on the button.
 * @param id - A unique identifier for the module. [Module.randomId] by default.
 * @param onButtonPressed - Callback called when the user presses the button.
 */
data class ButtonModule(
    val text: Text,
    override val id: String = Module.randomId,
    val onButtonPressed: () -> Unit
) : Module<ButtonModule> {

    constructor(
        text: CharSequence,
        id: String = Module.randomId,
        onButtonPressed: () -> Unit
    ) : this(
        text = Text.CharSequence(text),
        id = id,
        onButtonPressed = onButtonPressed
    )
}