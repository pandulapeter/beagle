package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule.Companion.ID


/**
 * Displays a button that generates a random text and returns it in a callback lambda.
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. Optional, "Generate Lorem Ipsum" by default.
 * @param minimumWordCount - The minimum number of words that will be in the generated text. 5 by default.
 * @param maximumWordCount - The maximum number of words that will be in the generated text. 20 by default.
 * @param shouldStartWithLoremIpsum - Whether or not the first two words of the text should be "Lorem Ipsum" (or just "Lorem" in case [maximumWordCount] is 1). True by default.
 * @param shouldGenerateSentences - Whether or not the generated text should be capitalized and contain periods. False by default.
 * @param onLoremIpsumReady - Callback called when the user presses the button. It is invoked with the generated String, as its argument.
 */
data class LoremIpsumGeneratorButtonModule(
    val text: CharSequence = "Generate Lorem Ipsum",
    val minimumWordCount: Int = 5,
    val maximumWordCount: Int = 20,
    val shouldStartWithLoremIpsum: Boolean = true,
    val shouldGenerateSentences: Boolean = false,
    val onLoremIpsumReady: (generatedText: String) -> Unit
) : Module<LoremIpsumGeneratorButtonModule> {

    override val id: String = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "loremIpsumGeneratorButton"
    }
}