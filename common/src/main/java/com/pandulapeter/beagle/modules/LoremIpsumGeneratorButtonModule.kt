package com.pandulapeter.beagle.modules

import androidx.annotation.DrawableRes
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule.Companion.DEFAULT_ICON
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule.Companion.DEFAULT_IS_ENABLED
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule.Companion.DEFAULT_MAXIMUM_WORD_COUNT
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule.Companion.DEFAULT_MINIMUM_WORD_COUNT
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule.Companion.DEFAULT_SHOULD_GENERATE_SENTENCE
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule.Companion.DEFAULT_SHOULD_START_WITH_LOREM_IPSUM
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule.Companion.DEFAULT_TEXT
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule.Companion.DEFAULT_TYPE
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
 * @param type - Specify a [TextModule.Type] to apply a specific appearance. [DEFAULT_TYPE] by default.
 * @param icon - A drawable resource ID that will be tinted and displayed before the text, or null to display no icon. [DEFAULT_ICON] by default.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. [DEFAULT_IS_ENABLED] by default.
 * @param onLoremIpsumReady - Callback invoked when the user presses the button. It is invoked with the generated String, as its argument.
 */
@Suppress("unused")
data class LoremIpsumGeneratorButtonModule(
    val text: Text = Text.CharSequence(DEFAULT_TEXT),
    val minimumWordCount: Int = DEFAULT_MINIMUM_WORD_COUNT,
    val maximumWordCount: Int = DEFAULT_MAXIMUM_WORD_COUNT,
    val shouldStartWithLoremIpsum: Boolean = DEFAULT_SHOULD_START_WITH_LOREM_IPSUM,
    val shouldGenerateSentence: Boolean = DEFAULT_SHOULD_GENERATE_SENTENCE,
    val type: TextModule.Type = DEFAULT_TYPE,
    @DrawableRes val icon: Int? = DEFAULT_ICON,
    val isEnabled: Boolean = DEFAULT_IS_ENABLED,
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
        private val DEFAULT_TYPE = TextModule.Type.BUTTON
        private val DEFAULT_ICON: Int? = null
        private const val DEFAULT_IS_ENABLED = true
    }
}