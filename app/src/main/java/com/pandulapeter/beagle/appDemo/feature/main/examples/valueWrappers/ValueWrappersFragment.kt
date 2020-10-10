package com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.model.BeagleListItemContractImplementation
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesDetailFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers.list.ValueWrappersAdapter
import com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers.list.ValueWrappersListItem
import com.pandulapeter.beagle.appDemo.utils.createSectionHeaderModule
import com.pandulapeter.beagle.appDemo.utils.createTextModule
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.listeners.UpdateListener
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.MultipleSelectionListModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SliderModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextInputModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class ValueWrappersFragment : ExamplesDetailFragment<ValueWrappersViewModel, ValueWrappersListItem>(R.string.case_study_value_wrappers_title) {

    override val viewModel by viewModel<ValueWrappersViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Beagle.addUpdateListener(
            listener = object : UpdateListener {
                override fun onContentsChanged() = viewModel.refreshItems()
            },
            lifecycleOwner = this
        )
    }

    override fun createAdapter() = ValueWrappersAdapter(
        scope = viewModel.viewModelScope,
        onSectionHeaderSelected = viewModel::onSectionHeaderSelected,
        onCurrentStateCardPressed = { Beagle.show() },
        onResetButtonPressed = ::resetAll,
        onEnableAllModulesSwitchToggled = { isEnabled ->
            viewModel.areModulesEnabled = isEnabled
            refreshBeagle()
        },
        onBulkApplySwitchToggled = { isEnabled ->
            viewModel.isBulkApplyEnabled = isEnabled
            refreshBeagle()
        })

    override fun getBeagleModules(): List<Module<*>> = listOf(
        createTextModule(R.string.case_study_value_wrappers_hint_1),
        createSectionHeaderModule(R.string.case_study_value_wrappers_switches),
        SwitchModule(
            id = TOGGLE_1_ID,
            text = getString(R.string.case_study_value_wrappers_toggle_1),
            isValuePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            isEnabled = viewModel.areModulesEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        SwitchModule(
            id = TOGGLE_2_ID,
            text = getString(R.string.case_study_value_wrappers_toggle_2),
            isValuePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            isEnabled = viewModel.areModulesEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        createSectionHeaderModule(R.string.case_study_value_wrappers_check_boxes),
        CheckBoxModule(
            id = TOGGLE_3_ID,
            text = getString(R.string.case_study_value_wrappers_toggle_3),
            isValuePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            isEnabled = viewModel.areModulesEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        CheckBoxModule(
            id = TOGGLE_4_ID,
            text = getString(R.string.case_study_value_wrappers_toggle_4),
            isValuePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            isEnabled = viewModel.areModulesEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        DividerModule("divider1"),
        createTextModule(R.string.case_study_value_wrappers_hint_2),
        MultipleSelectionListModule(
            id = CHECK_BOX_GROUP_ID,
            title = getText(R.string.case_study_value_wrappers_check_box_group),
            items = listOf(
                BeagleListItemContractImplementation(getString(R.string.case_study_value_wrappers_check_box_1)),
                BeagleListItemContractImplementation(getString(R.string.case_study_value_wrappers_check_box_2)),
                BeagleListItemContractImplementation(getString(R.string.case_study_value_wrappers_check_box_3))
            ),
            isExpandedInitially = true,
            isValuePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            initiallySelectedItemIds = emptySet(),
            isEnabled = viewModel.areModulesEnabled,
            onSelectionChanged = { viewModel.refreshItems() }
        ),
        DividerModule("divider2"),
        createTextModule(R.string.case_study_value_wrappers_hint_3),
        SingleSelectionListModule(
            id = RADIO_BUTTON_GROUP_ID,
            title = getText(R.string.case_study_value_wrappers_radio_button_group),
            items = listOf(
                BeagleListItemContractImplementation(getString(R.string.case_study_value_wrappers_radio_button_1)),
                BeagleListItemContractImplementation(getString(R.string.case_study_value_wrappers_radio_button_2)),
                BeagleListItemContractImplementation(getString(R.string.case_study_value_wrappers_radio_button_3))
            ),
            isExpandedInitially = true,
            isValuePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            initiallySelectedItemId = getString(R.string.case_study_value_wrappers_radio_button_1),
            isEnabled = viewModel.areModulesEnabled,
            onSelectionChanged = { viewModel.refreshItems() }
        ),
        DividerModule("divider3"),
        createTextModule(R.string.case_study_value_wrappers_hint_4),
        createSectionHeaderModule(R.string.case_study_value_wrappers_slider_label),
        SliderModule(
            id = SLIDER,
            text = { getString(R.string.case_study_value_wrappers_slider_title, it) },
            isValuePersisted = true,
            initialValue = SLIDER_DEFAULT_VALUE,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            isEnabled = viewModel.areModulesEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        createTextModule(R.string.case_study_value_wrappers_hint_5),
        createSectionHeaderModule(R.string.case_study_value_wrappers_text_input_label),
        TextInputModule(
            id = TEXT_INPUT,
            text = { Text.CharSequence(getString(R.string.case_study_value_wrappers_text_input_title, it)) },
            initialValue = TEXT_DEFAULT_VALUE,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            isValuePersisted = true,
            isEnabled = viewModel.areModulesEnabled,
            onValueChanged = { viewModel.refreshItems() }
        )
    )

    private fun resetAll() {
        viewModel.toggle1?.setCurrentValue(Beagle, false)
        viewModel.toggle2?.setCurrentValue(Beagle, false)
        viewModel.toggle3?.setCurrentValue(Beagle, false)
        viewModel.toggle4?.setCurrentValue(Beagle, false)
        viewModel.multipleSelectionOptions?.setCurrentValue(Beagle, emptySet())
        viewModel.singleSelectionOption?.setCurrentValue(Beagle, getString(R.string.case_study_value_wrappers_radio_button_1))
        viewModel.slider?.setCurrentValue(Beagle, SLIDER_DEFAULT_VALUE)
        viewModel.textInput?.setCurrentValue(Beagle, TEXT_DEFAULT_VALUE)
        binding.recyclerView.showSnackbar(R.string.case_study_value_wrappers_state_reset)
    }

    companion object {
        const val TOGGLE_1_ID = "toggle1"
        const val TOGGLE_2_ID = "toggle2"
        const val TOGGLE_3_ID = "toggle3"
        const val TOGGLE_4_ID = "toggle4"
        const val CHECK_BOX_GROUP_ID = "checkBoxes"
        const val RADIO_BUTTON_GROUP_ID = "radioButtons"
        const val SLIDER = "slider"
        const val TEXT_INPUT = "textInput"
        const val SLIDER_DEFAULT_VALUE = 5
        const val TEXT_DEFAULT_VALUE = "Hello world"

        fun newInstance() = ValueWrappersFragment()
    }
}