package com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.BeagleListItemContractImplementation
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationDetailFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.list.FeatureTogglesAdapter
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.list.FeatureTogglesListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.listeners.UpdateListener
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.MultipleSelectionListModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.android.ext.android.inject

class FeatureTogglesFragment : InspirationDetailFragment<FeatureTogglesViewModel, FeatureTogglesListItem>(R.string.case_study_feature_toggles_title) {

    override val viewModel by inject<FeatureTogglesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Beagle.addUpdateListener(
            listener = object : UpdateListener {
                override fun onContentsChanged() = viewModel.refreshItems()
            },
            lifecycleOwner = this
        )
    }

    override fun createAdapter() = FeatureTogglesAdapter(
        scope = viewModel.viewModelScope,
        onSectionHeaderSelected = viewModel::onSectionHeaderSelected,
        onResetButtonPressed = ::resetAll,
        onBulkApplySwitchToggled = { isEnabled ->
            viewModel.isBulkApplyEnabled = isEnabled
            refreshBeagle()
        })

    override fun getBeagleModules(): List<Module<*>> = listOf(
        TextModule(text = getString(R.string.case_study_feature_toggles_hint_1)),
        LabelModule(title = getString(R.string.case_study_feature_toggles_switches)),
        SwitchModule(
            id = TOGGLE_1_ID,
            text = getString(R.string.case_study_feature_toggles_toggle_1),
            shouldBePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        SwitchModule(
            id = TOGGLE_2_ID,
            text = getString(R.string.case_study_feature_toggles_toggle_2),
            shouldBePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        LabelModule(title = getString(R.string.case_study_feature_toggles_check_boxes)),
        CheckBoxModule(
            id = TOGGLE_3_ID,
            text = getString(R.string.case_study_feature_toggles_toggle_3),
            shouldBePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        CheckBoxModule(
            id = TOGGLE_4_ID,
            text = getString(R.string.case_study_feature_toggles_toggle_4),
            shouldBePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        TextModule(text = getString(R.string.case_study_feature_toggles_hint_2)),
        MultipleSelectionListModule(
            id = CHECK_BOX_GROUP_ID,
            title = getText(R.string.case_study_feature_toggles_check_box_group),
            items = listOf(
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_check_box_1)),
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_check_box_2)),
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_check_box_3))
            ),
            isExpandedInitially = true,
            shouldBePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            initiallySelectedItemIds = emptySet(),
            onSelectionChanged = { viewModel.refreshItems() }
        ),
        TextModule(text = getString(R.string.case_study_feature_toggles_hint_3)),
        SingleSelectionListModule(
            id = RADIO_BUTTON_GROUP_ID,
            title = getText(R.string.case_study_feature_toggles_radio_button_group),
            items = listOf(
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_radio_button_1)),
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_radio_button_2)),
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_radio_button_3))
            ),
            isExpandedInitially = true,
            shouldBePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            initiallySelectedItemId = getString(R.string.case_study_feature_toggles_radio_button_1),
            onSelectionChanged = { viewModel.refreshItems() }
        )
    )

    override fun onListUpdated() {
        if (viewModel.shouldSetAppBarToNotLifted()) {
            binding.appBar.setLifted(false)
        }
    }

    private fun resetAll() {
        Beagle.find<SwitchModule>(TOGGLE_1_ID)?.setCurrentValue(Beagle, false)
        Beagle.find<SwitchModule>(TOGGLE_2_ID)?.setCurrentValue(Beagle, false)
        Beagle.find<CheckBoxModule>(TOGGLE_3_ID)?.setCurrentValue(Beagle, false)
        Beagle.find<CheckBoxModule>(TOGGLE_4_ID)?.setCurrentValue(Beagle, false)
        Beagle.find<MultipleSelectionListModule<BeagleListItemContractImplementation>>(CHECK_BOX_GROUP_ID)?.setCurrentValue(Beagle, emptySet())
        Beagle.find<SingleSelectionListModule<BeagleListItemContractImplementation>>(RADIO_BUTTON_GROUP_ID)?.setCurrentValue(Beagle, getString(R.string.case_study_feature_toggles_radio_button_1))
    }

    companion object {
        const val TOGGLE_1_ID = "toggle1"
        const val TOGGLE_2_ID = "toggle2"
        const val TOGGLE_3_ID = "toggle3"
        const val TOGGLE_4_ID = "toggle4"
        const val CHECK_BOX_GROUP_ID = "checkBoxes"
        const val RADIO_BUTTON_GROUP_ID = "radioButtons"

        fun newInstance() = FeatureTogglesFragment()
    }
}