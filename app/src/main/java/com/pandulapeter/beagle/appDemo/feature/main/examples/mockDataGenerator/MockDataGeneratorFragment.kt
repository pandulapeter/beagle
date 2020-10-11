package com.pandulapeter.beagle.appDemo.feature.main.examples.mockDataGenerator

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesDetailFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.mockDataGenerator.list.MockDataGeneratorAdapter
import com.pandulapeter.beagle.appDemo.feature.main.examples.mockDataGenerator.list.MockDataGeneratorListItem
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule
import com.pandulapeter.beagle.modules.PaddingModule
import com.pandulapeter.beagle.modules.SliderModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class MockDataGeneratorFragment : ExamplesDetailFragment<MockDataGeneratorViewModel, MockDataGeneratorListItem>(R.string.case_study_mock_data_generator_title) {

    override val viewModel by viewModel<MockDataGeneratorViewModel>()
    private val minimumWordCountSlider by lazy {
        SliderModule(
            text = { Text.CharSequence(getString(R.string.case_study_mock_data_generator_minimum_word_count, it)) },
            minimumValue = 1,
            maximumValue = 5,
            initialValue = 3,
            onValueChanged = { refreshBeagle() }
        )
    }
    private val minimumWordCount get() = minimumWordCountSlider.getCurrentValue(Beagle) ?: 1
    private val maximumWordCountSlider by lazy {
        SliderModule(
            text = { Text.CharSequence(getString(R.string.case_study_mock_data_generator_maximum_word_count, it)) },
            minimumValue = 6,
            maximumValue = 20,
            initialValue = 8,
            onValueChanged = { refreshBeagle() }
        )
    }
    private val maximumWordCount get() = maximumWordCountSlider.getCurrentValue(Beagle) ?: 20
    private val shouldStartWithLoremIpsumCheckBox by lazy {
        CheckBoxModule(
            text = { Text.ResourceId(R.string.case_study_mock_data_generator_start_with_lorem_ipsum) },
            initialValue = true,
            onValueChanged = { refreshBeagle() }
        )
    }
    private val shouldStartWithLoremIpsum get() = shouldStartWithLoremIpsumCheckBox.getCurrentValue(Beagle) == true
    private val shouldGenerateSentenceCheckBox by lazy {
        CheckBoxModule(
            text = { Text.ResourceId(R.string.case_study_mock_data_generator_generate_sentence) },
            initialValue = true,
            onValueChanged = { refreshBeagle() }
        )
    }
    private val shouldGenerateSentence get() = shouldGenerateSentenceCheckBox.getCurrentValue(Beagle) == true

    override fun createAdapter() = MockDataGeneratorAdapter(viewModel.viewModelScope) { Beagle.show() }

    override fun getBeagleModules(): List<Module<*>> = listOf(
        minimumWordCountSlider,
        maximumWordCountSlider,
        shouldStartWithLoremIpsumCheckBox,
        shouldGenerateSentenceCheckBox,
        PaddingModule(),
        LoremIpsumGeneratorButtonModule(
            text = Text.ResourceId(R.string.case_study_mock_data_generator_generate_text),
            minimumWordCount = minimumWordCount,
            maximumWordCount = maximumWordCount,
            shouldStartWithLoremIpsum = shouldStartWithLoremIpsum,
            shouldGenerateSentence = shouldGenerateSentence,
            onLoremIpsumReady = {
                viewModel.generatedText = it
                updateUi()
            }
        )
    ).also { updateUi() }

    private fun updateUi() = viewModel.updateItems(
        minimumWordCount = minimumWordCount,
        maximumWordCount = maximumWordCount,
        shouldStartWithLoremIpsum = shouldStartWithLoremIpsum,
        shouldGenerateSentence = shouldGenerateSentence,
        generatedText = viewModel.generatedText ?: getString(R.string.case_study_mock_data_generator_default_hint)
    )

    companion object {
        fun newInstance() = MockDataGeneratorFragment()
    }
}