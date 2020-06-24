package com.pandulapeter.beagle.appDemo.feature.main.playground.addModule

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.ModuleRepository
import com.pandulapeter.beagle.appDemo.data.ModuleWrapper
import com.pandulapeter.beagle.appDemo.data.SampleListItem
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.list.AddModuleListItem
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.list.ModuleViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule
import com.pandulapeter.beagle.modules.ButtonModule
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.DeviceInfoModule
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.ForceCrashButtonModule
import com.pandulapeter.beagle.modules.HeaderModule
import com.pandulapeter.beagle.modules.ItemListModule
import com.pandulapeter.beagle.modules.KeyValueListModule
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.LongTextModule
import com.pandulapeter.beagle.modules.MultipleSelectionListModule
import com.pandulapeter.beagle.modules.PaddingModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule

class AddModuleViewModel(private val moduleRepository: ModuleRepository) : ListViewModel<AddModuleListItem>() {

    override val items: LiveData<List<AddModuleListItem>> = MutableLiveData(
        listOf(
            TextViewHolder.UiModel(R.string.add_module_generic_modules),
            createModuleUiModel(
                titleResourceId = R.string.add_module_button,
                descriptionResourceId = R.string.add_module_button_description,
                module = ButtonModule(text = "Button", onButtonPressed = {})
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_check_box,
                descriptionResourceId = R.string.add_module_check_box_description,
                module = CheckBoxModule(text = "CheckBox", initialValue = false, onValueChanged = {})
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_divider,
                descriptionResourceId = R.string.add_module_divider_description,
                module = DividerModule()
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_item_list,
                descriptionResourceId = R.string.add_module_item_list_description,
                module = ItemListModule(
                    title = "ItemList",
                    items = listOf(
                        SampleListItem("Item 1"),
                        SampleListItem("Item 2"),
                        SampleListItem("Item 3")
                    ),
                    onItemSelected = {}
                )
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_key_value_list,
                descriptionResourceId = R.string.add_module_key_value_list_description,
                module = KeyValueListModule(
                    title = "KeyValueList",
                    pairs = listOf(
                        "Key 1" to "Value 1",
                        "Key 2" to "Value 2",
                        "Key 3" to "Value 3"
                    )
                )
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_label,
                descriptionResourceId = R.string.add_module_label_description,
                module = LabelModule(title = "Label")
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_long_text,
                descriptionResourceId = R.string.add_module_long_text_description,
                module = LongTextModule(
                    title = "LongText",
                    text = "This is a longer piece of text that only becomes visible when the user expands the header."
                )
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_multiple_selection_list,
                descriptionResourceId = R.string.add_module_multiple_selection_list_description,
                module = MultipleSelectionListModule(
                    title = "MultipleSelectionList",
                    items = listOf(
                        SampleListItem("Checkbox 1"),
                        SampleListItem("Checkbox 2"),
                        SampleListItem("Checkbox 3")
                    ),
                    initiallySelectedItemIds = emptySet(),
                    onSelectionChanged = {}
                )
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_padding,
                descriptionResourceId = R.string.add_module_padding_description,
                module = PaddingModule()
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_single_selection_list,
                descriptionResourceId = R.string.add_module_single_selection_list_description,
                module = SingleSelectionListModule(
                    title = "SingleSelectionList",
                    items = listOf(
                        SampleListItem("Radio button 1"),
                        SampleListItem("Radio button 2"),
                        SampleListItem("Radio button 3")
                    ),
                    initiallySelectedItemId = null,
                    onSelectionChanged = {}
                )
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_switch,
                descriptionResourceId = R.string.add_module_switch_description,
                module = SwitchModule(text = "Switch", onValueChanged = {})
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_text,
                descriptionResourceId = R.string.add_module_text_description,
                module = TextModule(text = "Text")
            ),
            TextViewHolder.UiModel(R.string.add_module_unique_modules),
            createModuleUiModel(
                titleResourceId = R.string.add_module_animation_duration_switch,
                descriptionResourceId = R.string.add_module_animation_duration_switch_description,
                module = AnimationDurationSwitchModule()
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_app_info,
                descriptionResourceId = R.string.add_module_app_info_description,
                module = AppInfoButtonModule()
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_device_info,
                descriptionResourceId = R.string.add_module_device_info_description,
                module = DeviceInfoModule()
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_force_crash_button,
                descriptionResourceId = R.string.add_module_force_crash_button_description,
                module = ForceCrashButtonModule()
            ),
            createModuleUiModel(
                titleResourceId = R.string.add_module_header,
                descriptionResourceId = R.string.add_module_header_description,
                module = HeaderModule(
                    title = "Header title",
                    subtitle = "Header subtitle"
                )
            )
        )
    )

    fun onModuleSelected(moduleWrapper: ModuleWrapper) = moduleRepository.addModule(moduleWrapper)

    private fun createModuleUiModel(
        @StringRes titleResourceId: Int,
        @StringRes descriptionResourceId: Int,
        module: Module<*>
    ) = ModuleViewHolder.UiModel(
        moduleWrapper = ModuleWrapper(
            titleResourceId = titleResourceId,
            module = module
        ),
        descriptionResourceId = descriptionResourceId,
        isEnabled = !Beagle.contains(module.id)
    )
}