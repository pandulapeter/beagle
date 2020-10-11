package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule.Companion.DEFAULT_TEXT
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule.Companion.ID


/**
 * Displays a button that generates a random text and returns it in a callback lambda.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. [DEFAULT_TEXT] by default.
 * @param minimumWordCount - The minimum number of words that will be in the generated text. [DEFAULT_MINIMUM_WORD_COUNT] by default.
 * @param maximumWordCount - The maximum number of words that will be in the generated text. [DEFAULT_MAXIMUM_WORD_COUNT] by default.
 * @param shouldStartWithLoremIpsum - Whether or not the first two words of the text should be "Lorem Ipsum" (or just "Lorem" in case [maximumWordCount] is 1). [DEFAULT_SHOULD_START_WITH_LOREM_IPSUM] by default.
 * @param shouldGenerateSentence - Whether or not the generated text should be capitalized and end with a period. [DEFAULT_SHOULD_GENERATE_SENTENCE] by default.
 * @param onLoremIpsumReady - Callback called when the user presses the button. It is invoked with the generated String, as its argument.
 */
data class LoremIpsumGeneratorButtonModule(
    val text: Text = Text.CharSequence(DEFAULT_TEXT),
    val minimumWordCount: Int = DEFAULT_MINIMUM_WORD_COUNT,
    val maximumWordCount: Int = 20,
    val shouldStartWithLoremIpsum: Boolean = DEFAULT_SHOULD_START_WITH_LOREM_IPSUM,
    val shouldGenerateSentence: Boolean = DEFAULT_SHOULD_GENERATE_SENTENCE,
    val onLoremIpsumReady: (generatedText: String) -> Unit
) : Module<LoremIpsumGeneratorButtonModule> {

    override val id: String = ID

    companion object {
        const val ID = "loremIpsumGeneratorButton"
        private const val DEFAULT_TEXT = "Generate Lorem Ipsum"
        private const val DEFAULT_MINIMUM_WORD_COUNT = 5
        private const val DEFAULT_MAXIMUM_WORD_COUNT = 20
        private const val DEFAULT_SHOULD_START_WITH_LOREM_IPSUM = true
        private const val DEFAULT_SHOULD_GENERATE_SENTENCE = false
    }
}