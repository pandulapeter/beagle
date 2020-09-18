package com.pandulapeter.beagle.appDemo.feature.main.examples.staticData

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.SectionHeaderViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.SpaceViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import kotlin.properties.Delegates

class StaticDataViewModel : ListViewModel<ListItem>() {

    private val _items = MutableLiveData(emptyList<ListItem>())
    override val items: LiveData<List<ListItem>> = _items
    private var selectedSection by Delegates.observable<Section?>(null) { _, _, _ -> refreshItems() }

    init {
        refreshItems()
    }

    fun onSectionHeaderSelected(uiModel: SectionHeaderViewHolder.UiModel) {
        Section.fromResourceId(uiModel.titleResourceId).let {
            selectedSection = if (it == selectedSection) null else it
        }
    }

    private fun refreshItems() {
        _items.value = mutableListOf<ListItem>().apply {
            addTopSection()
            addHeaderSection()
            addTextSection()
            addLongTextSection()
            addSectionHeaderSection()
            addPaddingSection()
            addDividerSection()
            addKeyValueListSection()
            addLoadingIndicatorSection()
        }
    }

    private fun MutableList<ListItem>.addTopSection() {
        add(TextViewHolder.UiModel(R.string.case_study_static_data_top_1))
        add(
            CodeSnippetViewHolder.UiModel(
                "Beagle.set(\n" +
                        "    HeaderModule(\n" +
                        "        title = headerTitle,\n" +
                        "        subtitle = headerSubtitle,\n" +
                        "        text = headerText\n" +
                        "    ),\n" +
                        "    TextModule(text = \"…\"),\n" +
                        "    LongTextModule(\n" +
                        "        title = \"…\",\n" +
                        "        text = \"…\"\n" +
                        "    ),\n" +
                        "    SectionHeaderModule(title = \"…\"),\n" +
                        "    PaddingModule(),\n" +
                        "    DividerModule()\n" +
                        "    KeyValueListModule(\n" +
                        "        title = \"…\",\n" +
                        "        pairs = listOf(\n" +
                        "            \"Key 1\" to \"Value 1\",\n" +
                        "            \"Key 2\" to \"Value 2\",\n" +
                        "            \"Key 3\" to \"Value 3\"\n" +
                        "        )\n" +
                        "    )\n" +
                        "    LoadingIndicatorModule()\n" +
                        ")"
            )
        )
        add(TextViewHolder.UiModel(R.string.case_study_static_data_top_2))
    }

    private fun MutableList<ListItem>.addHeaderSection() = addSection(Section.HEADER) {
        add(TextViewHolder.UiModel(R.string.case_study_static_data_header_2))
        add(
            CodeSnippetViewHolder.UiModel(
                "val headerTitle = \"\${getString(R.string.app_name)} v\${BuildConfig.VERSION_NAME} (\${BuildConfig.VERSION_CODE})\",\n" +
                        "val headerSubtitle = BuildConfig.APPLICATION_ID,\n" +
                        "val headerText = \"Built on \${BuildConfig.BUILD_DATE}\""
            )
        )
        add(TextViewHolder.UiModel(R.string.case_study_static_data_header_3))
        add(
            CodeSnippetViewHolder.UiModel(
                "android {\n" +
                        "    …\n" +
                        "    defaultConfig {\n" +
                        "        …\n" +
                        "        buildConfigField(\"String\", \"BUILD_DATE\", \"\\\"\${new Date ().format(\"yyyy-MM-dd\")}\\\"\")\n" +
                        "    }\n" +
                        "}"
            )
        )
        add(TextViewHolder.UiModel(R.string.case_study_static_data_header_4))
    }

    private fun MutableList<ListItem>.addTextSection() = addSection(Section.TEXT) {
        add(TextViewHolder.UiModel(R.string.case_study_static_data_text_2))
    }

    private fun MutableList<ListItem>.addLongTextSection() = addSection(Section.LONG_TEXT) {
        add(TextViewHolder.UiModel(R.string.case_study_static_data_long_text_2))
    }

    private fun MutableList<ListItem>.addSectionHeaderSection() = addSection(Section.LABEL) {
        add(TextViewHolder.UiModel(R.string.case_study_static_data_section_header_2))
    }

    private fun MutableList<ListItem>.addPaddingSection() = addSection(Section.PADDING) {
        add(TextViewHolder.UiModel(R.string.case_study_static_data_padding_2))
    }

    private fun MutableList<ListItem>.addDividerSection() = addSection(Section.DIVIDER) {
        add(TextViewHolder.UiModel(R.string.case_study_static_data_divider_2))
    }

    private fun MutableList<ListItem>.addKeyValueListSection() = addSection(Section.KEY_VALUE_LIST) {
        add(TextViewHolder.UiModel(R.string.case_study_static_data_key_value_list_2))
    }

    private fun MutableList<ListItem>.addLoadingIndicatorSection() = addSection(Section.LOADING_INDICATOR) {
        add(TextViewHolder.UiModel(R.string.case_study_static_data_loading_indicator_2))
    }

    private fun MutableList<ListItem>.addSection(section: Section, action: MutableList<ListItem>.() -> Unit) = (selectedSection == section).also { isExpanded ->
        add(SectionHeaderViewHolder.UiModel(section.titleResourceId, isExpanded))
        if (isExpanded) {
            action()
            add(SpaceViewHolder.UiModel())
        }
    }

    private enum class Section(@StringRes val titleResourceId: Int) {
        HEADER(R.string.case_study_static_data_header_1),
        TEXT(R.string.case_study_static_data_text_1),
        LONG_TEXT(R.string.case_study_static_data_long_text_1),
        LABEL(R.string.case_study_static_data_section_header_1),
        PADDING(R.string.case_study_static_data_padding_1),
        DIVIDER(R.string.case_study_static_data_divider_1),
        KEY_VALUE_LIST(R.string.case_study_static_data_key_value_list_1),
        LOADING_INDICATOR(R.string.case_study_static_data_loading_indicator_1);

        companion object {
            fun fromResourceId(@StringRes titleResourceId: Int?) = values().firstOrNull { it.titleResourceId == titleResourceId }
        }
    }
}