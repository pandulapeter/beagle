package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule.Companion.ID


/**
 * Displays a button that generates a random text and returns it in a callback lambda.
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. Optional, "Generate Lorem Ipsum" by default.
 * @param onLoremIpsumReady - Callback called when the user presses the button. It is invoked with the generated String, as its argument.
 */
data class LoremIpsumGeneratorButtonModule(
    val text: CharSequence = "Generate Lorem Ipsum",
    val onLoremIpsumReady: (generatedText: String) -> Unit
) : Module<LoremIpsumGeneratorButtonModule> {

    override val id: String = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "loremIpsumGeneratorButton"
    }
}