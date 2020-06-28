package com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.BeagleListItemContractImplementation
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationDetailFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.MultipleSelectionListModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.android.ext.android.inject

class FeatureTogglesFragment : InspirationDetailFragment<FeatureTogglesViewModel, ListItem>(R.string.case_study_feature_toggles_title) {

    override val viewModel by inject<FeatureTogglesViewModel>()

    override fun createAdapter() = BaseAdapter<ListItem>(viewModel.viewModelScope)

    override fun getBeagleModules(): List<Module<*>> = listOf(
        TextModule(text = getString(R.string.case_study_feature_toggles_hint_1)),
        LabelModule(title = getString(R.string.case_study_feature_toggles_switches)),
        SwitchModule(text = getString(R.string.case_study_feature_toggles_toggle_1), onValueChanged = {}),
        SwitchModule(text = getString(R.string.case_study_feature_toggles_toggle_2), onValueChanged = {}),
        LabelModule(title = getString(R.string.case_study_feature_toggles_check_boxes)),
        CheckBoxModule(text = getString(R.string.case_study_feature_toggles_toggle_3), onValueChanged = {}),
        CheckBoxModule(text = getString(R.string.case_study_feature_toggles_toggle_4), onValueChanged = {}),
        TextModule(text = getString(R.string.case_study_feature_toggles_hint_2)),
        MultipleSelectionListModule(
            title = getText(R.string.case_study_feature_toggles_check_box_group),
            items = listOf(
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_check_box_1)),
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_check_box_2)),
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_check_box_3))
            ),
            isExpandedInitially = true,
            initiallySelectedItemIds = emptySet(),
            onSelectionChanged = {}
        ),
        TextModule(text = getString(R.string.case_study_feature_toggles_hint_3)),
        SingleSelectionListModule(
            title = getText(R.string.case_study_feature_toggles_radio_button_group),
            items = listOf(
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_radio_button_1)),
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_radio_button_2)),
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_radio_button_3))
            ),
            isExpandedInitially = true,
            initiallySelectedItemId = getString(R.string.case_study_feature_toggles_radio_button_1),
            onSelectionChanged = {}
        )
    )

    companion object {
        fun newInstance() = FeatureTogglesFragment()
    }
}