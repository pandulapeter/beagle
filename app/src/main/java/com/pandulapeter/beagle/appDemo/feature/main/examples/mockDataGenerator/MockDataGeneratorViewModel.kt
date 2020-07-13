package com.pandulapeter.beagle.appDemo.feature.main.examples.mockDataGenerator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.examples.mockDataGenerator.list.LoremIpsumViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.examples.mockDataGenerator.list.MockDataGeneratorListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class MockDataGeneratorViewModel : ListViewModel<MockDataGeneratorListItem>() {

    private val _items = MutableLiveData<List<MockDataGeneratorListItem>>()
    override val items: LiveData<List<MockDataGeneratorListItem>> = _items
    var generatedText: String? = null

    fun updateItems(
        minimumWordCount: Int,
        maximumWordCount: Int,
        shouldStartWithLoremIpsum: Boolean,
        shouldGenerateSentence: Boolean,
        generatedText: String
    ) {
        _items.value = listOf(
            TextViewHolder.UiModel(R.string.case_study_mock_data_generator_text_1),
            LoremIpsumViewHolder.UiModel(generatedText),
            TextViewHolder.UiModel(R.string.case_study_mock_data_generator_text_2),
            CodeSnippetViewHolder.UiModel(
                id = CODE_SNIPPET_ID,
                codeSnippet = "LoremIpsumGeneratorButtonModule(\n" +
                        "    text = \"Generate text\",\n" +
                        "    minimumWordCount = $minimumWordCount,\n" +
                        "    maximumWordCount = $maximumWordCount,\n" +
                        "    shouldStartWithLoremIpsum = $shouldStartWithLoremIpsum,\n" +
                        "    shouldGenerateSentence = $shouldGenerateSentence,\n" +
                        "    onLoremIpsumReady = ::setText\n" +
                        ")"
            ),
            TextViewHolder.UiModel(R.string.case_study_mock_data_generator_text_3)
        )
    }

    companion object {
        private const val CODE_SNIPPET_ID = "codeSnippet"
    }
}