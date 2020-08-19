package com.pandulapeter.beagle.appDemo.feature.main.examples.featureFlags

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.model.BeagleListItemContractImplementation
import com.pandulapeter.beagle.appDemo.feature.main.examples.featureFlags.list.BulkApplySwitchViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.examples.featureFlags.list.CurrentStateViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.examples.featureFlags.list.FeatureFlagsListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.featureFlags.list.ResetButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.SectionHeaderViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.SpaceViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.MultipleSelectionListModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SliderModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextInputModule
import kotlin.properties.Delegates

class FeatureFlagsViewModel : ListViewModel<FeatureFlagsListItem>() {

    private val _items = MutableLiveData<List<FeatureFlagsListItem>>()
    override val items: LiveData<List<FeatureFlagsListItem>> = _items
    val toggle1 get() = Beagle.find<SwitchModule>(FeatureFlagsFragment.TOGGLE_1_ID)
    val toggle2 get() = Beagle.find<SwitchModule>(FeatureFlagsFragment.TOGGLE_2_ID)
    val toggle3 get() = Beagle.find<CheckBoxModule>(FeatureFlagsFragment.TOGGLE_3_ID)
    val toggle4 get() = Beagle.find<CheckBoxModule>(FeatureFlagsFragment.TOGGLE_4_ID)
    val multipleSelectionOptions get() = Beagle.find<MultipleSelectionListModule<BeagleListItemContractImplementation>>(FeatureFlagsFragment.CHECK_BOX_GROUP_ID)
    val singleSelectionOption get() = Beagle.find<SingleSelectionListModule<BeagleListItemContractImplementation>>(FeatureFlagsFragment.RADIO_BUTTON_GROUP_ID)
    val slider get() = Beagle.find<SliderModule>(FeatureFlagsFragment.SLIDER)
    val textInput get() = Beagle.find<TextInputModule>(FeatureFlagsFragment.TEXT_INPUT)
    private var selectedSection by Delegates.observable<Section?>(null) { _, _, _ -> refreshItems() }
    var isBulkApplyEnabled = false
        set(value) {
            field = value
            toggle1?.resetPendingChanges(Beagle)
            toggle2?.resetPendingChanges(Beagle)
            toggle3?.resetPendingChanges(Beagle)
            toggle4?.resetPendingChanges(Beagle)
            multipleSelectionOptions?.resetPendingChanges(Beagle)
            singleSelectionOption?.resetPendingChanges(Beagle)
            slider?.resetPendingChanges(Beagle)
            textInput?.resetPendingChanges(Beagle)
            refreshItems()
        }

    init {
        refreshItems()
    }

    fun onSectionHeaderSelected(uiModel: SectionHeaderViewHolder.UiModel) {
        Section.fromResourceId(uiModel.titleResourceId).let {
            selectedSection = if (it == selectedSection) null else it
        }
    }

    fun refreshItems() {
        _items.postValue(mutableListOf<FeatureFlagsListItem>().apply {
            addTopSection()
            addSwitchSection()
            addCheckBoxSection()
            addMultipleSelectionListSection()
            addSingleSelectionListSection()
            addSliderSection()
            addTextInputSection()
            addQueryingAndChangingTheCurrentValueSection()
            addPersistingStateSection()
            addBulkApplySection()
        })
    }

    private fun MutableList<FeatureFlagsListItem>.addTopSection() {
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_text_1))
        add(
            CurrentStateViewHolder.UiModel(
                toggle1 = toggle1?.getCurrentValue(Beagle) == true,
                toggle2 = toggle2?.getCurrentValue(Beagle) == true,
                toggle3 = toggle3?.getCurrentValue(Beagle) == true,
                toggle4 = toggle4?.getCurrentValue(Beagle) == true,
                multipleSelectionOptions = multipleSelectionOptions?.getCurrentValue(Beagle)?.toList().orEmpty(),
                singleSelectionOption = singleSelectionOption?.getCurrentValue(Beagle),
                slider = slider?.getCurrentValue(Beagle) ?: 0,
                text = textInput?.getCurrentValue(Beagle).orEmpty()
            )
        )
        add(SpaceViewHolder.UiModel())
    }

    private fun MutableList<FeatureFlagsListItem>.addSwitchSection() = addSection(Section.SWITCH) {
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_switch_description_1))
        add(
            CodeSnippetViewHolder.UiModel(
                "SwitchModule(\n" +
                        "    text = \"Text on the switch\", \n" +
                        "    onValueChanged = { isChecked -> TODO() }\n" +
                        ")"
            )
        )
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_switch_description_2))
    }

    private fun MutableList<FeatureFlagsListItem>.addCheckBoxSection() = addSection(Section.CHECK_BOX) {
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_check_box_description))
        add(
            CodeSnippetViewHolder.UiModel(
                "CheckBoxModule(\n" +
                        "    text = \"Text on the check box\", \n" +
                        "    onValueChanged = { isChecked -> TODO() }\n" +
                        ")"
            )
        )
    }

    private fun MutableList<FeatureFlagsListItem>.addMultipleSelectionListSection() = addSection(Section.MULTIPLE_SELECTION_LIST) {
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_multiple_selection_list_description_1))
        add(
            CodeSnippetViewHolder.UiModel(
                "data class Option(\n" +
                        "    override val id: String,\n" +
                        "    override val title: String \n" +
                        ") : BeagleListItemContract"
            )
        )
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_multiple_selection_list_description_2))
        add(
            CodeSnippetViewHolder.UiModel(
                "MultipleSelectionListModule(\n" +
                        "    title = \"Text in the header\",\n" +
                        "    items = listOf(\n" +
                        "        Option(id = \"option1\", title = \"Option 1\"),\n" +
                        "        Option(id = \"option2\", title = \"Option 2\"),\n" +
                        "        Option(id = \"option3\", title = \"Option 3\")\n" +
                        "    ),\n" +
                        "    initiallySelectedItemIds = emptySet(),\n" +
                        "    onSelectionChanged = { selectedItems -> TODO() }\n" +
                        ")"
            )
        )
    }

    private fun MutableList<FeatureFlagsListItem>.addSingleSelectionListSection() = addSection(Section.SINGLE_SELECTION_LIST) {
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_single_selection_list_description_1))
        add(
            CodeSnippetViewHolder.UiModel(
                "SingleSelectionListModule(\n" +
                        "    title = \"Text in the header\",\n" +
                        "    items = listOf(\n" +
                        "        Option(id = \"option1\", title = \"Option 1\"),\n" +
                        "        Option(id = \"option2\", title = \"Option 2\"),\n" +
                        "        Option(id = \"option3\", title = \"Option 3\")\n" +
                        "    ),\n" +
                        "    initiallySelectedItemId = \"option1\",\n" +
                        "    onSelectionChanged = { selectedItem -> TODO() }\n" +
                        ")"
            )
        )
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_single_selection_list_description_2))
    }

    private fun MutableList<FeatureFlagsListItem>.addSliderSection() = addSection(Section.SLIDER) {
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_slider_description_1))
        add(
            CodeSnippetViewHolder.UiModel(
                "SliderModule(\n" +
                        "    text = { currentValue -> \"Current value: \$currentValue\"},\n" +
                        "    onValueChanged = { newValue -> TODO() }\n" +
                        "),"
            )
        )
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_slider_description_2))
    }

    private fun MutableList<FeatureFlagsListItem>.addTextInputSection() = addSection(Section.TEXT_INPUT) {
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_text_input_description_1))
        add(
            CodeSnippetViewHolder.UiModel(
                "TextInputModule(\n" +
                        "    text = { currentValue -> \"Current value: \$currentValue\" },\n" +
                        "    onValueChanged = { newValue -> TODO() }\n" +
                        ")"
            )
        )
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_text_input_description_2))
    }

    private fun MutableList<FeatureFlagsListItem>.addQueryingAndChangingTheCurrentValueSection() = addSection(Section.QUERYING_AND_CHANGING_THE_CURRENT_VALUE) {
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_querying_and_changing_the_current_value_description_1))
        add(CodeSnippetViewHolder.UiModel("module.getCurrentValue(Beagle)"))
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_querying_and_changing_the_current_value_description_2))
        add(CodeSnippetViewHolder.UiModel("module.setCurrentValue(Beagle, newValue)"))
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_querying_and_changing_the_current_value_description_3))
        add(ResetButtonViewHolder.UiModel())
        add(SpaceViewHolder.UiModel())
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_querying_and_changing_the_current_value_description_4))
        add(CodeSnippetViewHolder.UiModel("Beagle.find<ModuleType>(moduleId)"))
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_querying_and_changing_the_current_value_description_5))
    }

    private fun MutableList<FeatureFlagsListItem>.addPersistingStateSection() = addSection(Section.PERSISTING_STATE) {
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_persisting_state_description))
    }

    private fun MutableList<FeatureFlagsListItem>.addBulkApplySection() = addSection(Section.BULK_APPLY) {
        add(TextViewHolder.UiModel(if (isBulkApplyEnabled) R.string.case_study_feature_flags_bulk_apply_description_1_on else R.string.case_study_feature_flags_bulk_apply_description_1_off))
        add(BulkApplySwitchViewHolder.UiModel(isBulkApplyEnabled))
        add(TextViewHolder.UiModel(R.string.case_study_feature_flags_bulk_apply_description_2))
    }

    private fun MutableList<FeatureFlagsListItem>.addSection(section: Section, action: MutableList<FeatureFlagsListItem>.() -> Unit) = (selectedSection == section).also { isExpanded ->
        add(SectionHeaderViewHolder.UiModel(section.titleResourceId, isExpanded))
        if (isExpanded) {
            action()
            add(SpaceViewHolder.UiModel())
        }
    }

    private enum class Section(@StringRes val titleResourceId: Int) {
        SWITCH(R.string.case_study_feature_flags_switch),
        CHECK_BOX(R.string.case_study_feature_flags_check_box),
        MULTIPLE_SELECTION_LIST(R.string.case_study_feature_flags_multiple_selection_list),
        SINGLE_SELECTION_LIST(R.string.case_study_feature_flags_single_selection_list),
        SLIDER(R.string.case_study_feature_flags_slider),
        TEXT_INPUT(R.string.case_study_feature_flags_text_input),
        QUERYING_AND_CHANGING_THE_CURRENT_VALUE(R.string.case_study_feature_flags_querying_and_changing_the_current_value),
        PERSISTING_STATE(R.string.case_study_feature_flags_persisting_state),
        BULK_APPLY(R.string.case_study_feature_flags_bulk_apply);

        companion object {
            fun fromResourceId(@StringRes titleResourceId: Int?) = values().firstOrNull { it.titleResourceId == titleResourceId }
        }
    }
}