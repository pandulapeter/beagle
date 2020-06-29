package com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.BeagleListItemContractImplementation
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.list.CurrentStateViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.list.FeatureTogglesListItem
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

    init {
        updateItems()
    }

    fun updateItems() {
        _items.value = mutableListOf<FeatureTogglesListItem>().apply {
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_text_1))
            //TODO: The values are incorrect the first time (modules are not yet added to Beagle)
            add(
                CurrentStateViewHolder.UiModel(
                    toggle1 = Beagle.find<SwitchModule>(FeatureTogglesFragment.TOGGLE_1_ID)?.getCurrentValue(Beagle) == true,
                    toggle2 = Beagle.find<SwitchModule>(FeatureTogglesFragment.TOGGLE_2_ID)?.getCurrentValue(Beagle) == true,
                    toggle3 = Beagle.find<CheckBoxModule>(FeatureTogglesFragment.TOGGLE_3_ID)?.getCurrentValue(Beagle) == true,
                    toggle4 = Beagle.find<CheckBoxModule>(FeatureTogglesFragment.TOGGLE_4_ID)?.getCurrentValue(Beagle) == true,
                    multipleSelectionOptions = Beagle.find<MultipleSelectionListModule<BeagleListItemContractImplementation>>(FeatureTogglesFragment.CHECK_BOX_GROUP_ID)?.getCurrentValue(Beagle)?.toList().orEmpty(),
                    singleSelectionOption = Beagle.find<SingleSelectionListModule<BeagleListItemContractImplementation>>(FeatureTogglesFragment.RADIO_BUTTON_GROUP_ID)?.getCurrentValue(Beagle)
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
            add(TextViewHolder.UiModel(R.string.case_study_feature_toggles_persisting_state))
        }
    }
}