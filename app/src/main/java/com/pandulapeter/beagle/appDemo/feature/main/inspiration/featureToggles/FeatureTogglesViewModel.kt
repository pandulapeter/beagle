package com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.BeagleListItemContractImplementation
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.list.CurrentStateViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.list.FeatureTogglesListItem
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.list.ResetButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.MultipleSelectionListModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule

class FeatureTogglesViewModel : ListViewModel<FeatureTogglesListItem>() {

    private val _items = MutableLiveData<List<FeatureTogglesListItem>>()
    override val items: LiveData<List<FeatureTogglesListItem>> = _items

    fun updateItems() {
        _items.value = mutableListOf<FeatureTogglesListItem>().apply {
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_text_1))
            add(
                CurrentStateViewHolder.UiModel(
                    toggle1 = Beagle.find<SwitchModule>(FeatureTogglesFragment.TOGGLE_1_ID)?.getCurrentValue(Beagle) == true,
                    toggle2 = Beagle.find<SwitchModule>(FeatureTogglesFragment.TOGGLE_2_ID)?.getCurrentValue(Beagle) == true,
                    toggle3 = Beagle.find<CheckBoxModule>(FeatureTogglesFragment.TOGGLE_3_ID)?.getCurrentValue(Beagle) == true,
                    toggle4 = Beagle.find<CheckBoxModule>(FeatureTogglesFragment.TOGGLE_4_ID)?.getCurrentValue(Beagle) == true,
                    multipleSelectionOptions = Beagle.find<MultipleSelectionListModule<BeagleListItemContractImplementation>>(FeatureTogglesFragment.CHECK_BOX_GROUP_ID)?.getCurrentValue(Beagle)
                        ?.toList().orEmpty(),
                    singleSelectionOption = Beagle.find<SingleSelectionListModule<BeagleListItemContractImplementation>>(FeatureTogglesFragment.RADIO_BUTTON_GROUP_ID)?.getCurrentValue(Beagle)
                )
            )
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_switch_description_1))
            add(
                CodeSnippetViewHolder.UiModel(
                    "SwitchModule(\n" +
                            "    text = \"Text on the switch\", \n" +
                            "    onValueChanged = { isChecked -> TODO() }\n" +
                            ")"
                )
            )
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_switch_description_2))
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_check_box_description))
            add(
                CodeSnippetViewHolder.UiModel(
                    "CheckBoxModule(\n" +
                            "    text = \"Text on the check box\", \n" +
                            "    onValueChanged = { isChecked -> TODO() }\n" +
                            ")"
                )
            )
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_multiple_selection_list_description_1))
            add(
                CodeSnippetViewHolder.UiModel(
                    "data class Option(\n" +
                            "    override val id: String,\n" +
                            "    override val title: String \n" +
                            ") : BeagleListItemContract"
                )
            )
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_multiple_selection_list_description_2))
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
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_single_selection_list_description_1))
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
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_single_selection_list_description_2))
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_querying_and_changing_the_current_value_1))
            add(CodeSnippetViewHolder.UiModel("module.getCurrentValue(Beagle)"))
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_querying_and_changing_the_current_value_2))
            add(CodeSnippetViewHolder.UiModel("module.setCurrentValue(Beagle, newValue)"))
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_querying_and_changing_the_current_value_3))
            add(ResetButtonViewHolder.UiModel())
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_querying_and_changing_the_current_value_4))
            add(CodeSnippetViewHolder.UiModel("Beagle.find<ModuleType>(moduleId)"))
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_querying_and_changing_the_current_value_5))
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_persisting_state))
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_bulk_apply))
        }
    }
}