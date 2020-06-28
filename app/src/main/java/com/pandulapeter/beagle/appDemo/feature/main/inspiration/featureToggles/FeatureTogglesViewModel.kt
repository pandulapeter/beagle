package com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.SwitchModule

class FeatureTogglesViewModel : ListViewModel<ListItem>() {

    private val _items = MutableLiveData<List<ListItem>>()
    override val items: LiveData<List<ListItem>> = _items

    init {
        updateItems()
    }

    fun updateItems() {
        _items.value = mutableListOf<ListItem>().apply {
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_text_1))
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_current_state))
            add(
                TextViewHolder.UiModel(
                    id = FeatureTogglesFragment.TOGGLE_1_ID,
                    textResourceId = if (Beagle.find<SwitchModule>(FeatureTogglesFragment.TOGGLE_1_ID)?.getCurrentValue(Beagle) == true)
                        R.string.case_study_feature_toggles_state_toggle_1_on
                    else
                        R.string.case_study_feature_toggles_state_toggle_1_off
                )
            )
            add(
                TextViewHolder.UiModel(
                    id = FeatureTogglesFragment.TOGGLE_2_ID,
                    textResourceId = if (Beagle.find<SwitchModule>(FeatureTogglesFragment.TOGGLE_2_ID)?.getCurrentValue(Beagle) == true)
                        R.string.case_study_feature_toggles_state_toggle_2_on
                    else
                        R.string.case_study_feature_toggles_state_toggle_2_off
                )
            )
            add(
                TextViewHolder.UiModel(
                    id = FeatureTogglesFragment.TOGGLE_3_ID,
                    textResourceId = if (Beagle.find<CheckBoxModule>(FeatureTogglesFragment.TOGGLE_3_ID)?.getCurrentValue(Beagle) == true)
                        R.string.case_study_feature_toggles_state_toggle_3_on
                    else
                        R.string.case_study_feature_toggles_state_toggle_3_off
                )
            )
            add(
                TextViewHolder.UiModel(
                    id = FeatureTogglesFragment.TOGGLE_4_ID,
                    textResourceId = if (Beagle.find<CheckBoxModule>(FeatureTogglesFragment.TOGGLE_4_ID)?.getCurrentValue(Beagle) == true)
                        R.string.case_study_feature_toggles_state_toggle_4_on
                    else
                        R.string.case_study_feature_toggles_state_toggle_4_off
                )
            )
            add(
                TextViewHolder.UiModel(
                    id = "checkBox",
                    textResourceId = R.string.case_study_feature_toggles_state_selected_check_box_options
                )
            )
            add(
                TextViewHolder.UiModel(
                    id = "radioButton",
                    textResourceId = R.string.case_study_feature_toggles_state_selected_radio_button_option
                )
            )
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_switch_description))
            add(CodeSnippetViewHolder.UiModel("SwitchModule(text = \"Text\", onValueChanged = { update() }),"))
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_check_box_description))
            add(CodeSnippetViewHolder.UiModel("CheckBoxModule(text = \"Text\", onValueChanged = { update() }),"))
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_multiple_selection_list_description))
            add(CodeSnippetViewHolder.UiModel("MultipleSelectionListModule() //TODO"))
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_single_selection_list_description))
            add(CodeSnippetViewHolder.UiModel("SingleSelectionListModule() //TODO"))
        }
    }
}