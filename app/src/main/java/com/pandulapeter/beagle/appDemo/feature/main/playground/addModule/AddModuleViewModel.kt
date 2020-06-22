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
            ModuleViewHolder.UiModel(R.string.add_module_button),
            ModuleViewHolder.UiModel(R.string.add_module_check_box),
            ModuleViewHolder.UiModel(R.string.add_module_divider),
            ModuleViewHolder.UiModel(R.string.add_module_item_list),
            ModuleViewHolder.UiModel(R.string.add_module_key_value_list),
            ModuleViewHolder.UiModel(R.string.add_module_label),
            ModuleViewHolder.UiModel(R.string.add_module_long_text),
            ModuleViewHolder.UiModel(R.string.add_module_multiple_selection_list),
            ModuleViewHolder.UiModel(R.string.add_module_padding),
            ModuleViewHolder.UiModel(R.string.add_module_single_selection_list),
            ModuleViewHolder.UiModel(R.string.add_module_switch),
            ModuleViewHolder.UiModel(R.string.add_module_text),
            TextViewHolder.UiModel(R.string.add_module_unique_modules),
            ModuleViewHolder.UiModel(R.string.add_module_animation_duration_switch),
            ModuleViewHolder.UiModel(R.string.add_module_app_info),
            ModuleViewHolder.UiModel(R.string.add_module_device_info),
            ModuleViewHolder.UiModel(R.string.add_module_force_crash_button),
            ModuleViewHolder.UiModel(R.string.add_module_header)
        )
    )
}