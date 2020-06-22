package com.pandulapeter.beagle.appDemo.feature.main.playground.addModule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.list.AddModuleListItem
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.list.ModuleViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class AddModuleViewModel : ListViewModel<AddModuleListItem>() {

    override val items: LiveData<List<AddModuleListItem>> = MutableLiveData(
        listOf(
            TextViewHolder.UiModel(R.string.add_module_generic_modules),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_button, descriptionResourceId = R.string.add_module_button_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_check_box, descriptionResourceId = R.string.add_module_check_box_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_divider, descriptionResourceId = R.string.add_module_divider_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_item_list, descriptionResourceId = R.string.add_module_item_list_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_key_value_list, descriptionResourceId = R.string.add_module_key_value_list_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_label, descriptionResourceId = R.string.add_module_label_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_long_text, descriptionResourceId = R.string.add_module_long_text_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_multiple_selection_list, descriptionResourceId = R.string.add_module_multiple_selection_list_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_padding, descriptionResourceId = R.string.add_module_padding_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_single_selection_list, descriptionResourceId = R.string.add_module_single_selection_list_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_switch, descriptionResourceId = R.string.add_module_switch_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_text, descriptionResourceId = R.string.add_module_text_description),
            TextViewHolder.UiModel(R.string.add_module_unique_modules),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_animation_duration_switch, descriptionResourceId = R.string.add_module_animation_duration_switch_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_app_info, descriptionResourceId = R.string.add_module_app_info_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_device_info, descriptionResourceId = R.string.add_module_device_info_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_force_crash_button, descriptionResourceId = R.string.add_module_force_crash_button_description),
            ModuleViewHolder.UiModel(titleResourceId = R.string.add_module_header, descriptionResourceId = R.string.add_module_header_description)
        )
    )
}